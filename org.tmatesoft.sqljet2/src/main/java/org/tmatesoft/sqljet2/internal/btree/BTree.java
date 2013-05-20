package org.tmatesoft.sqljet2.internal.btree;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.VarInt;
import org.tmatesoft.sqljet2.internal.system.Trouble.Code;

public class BTree {
	private final Pager pager;
	private final int usableSize;
	private final PayloadSizes payloadSizes;

	public BTree(final Pager pager) {
		this.pager = pager;
		usableSize = pager.getPageSize(); // TODO - reservedSize
		payloadSizes = new PayloadSizes(usableSize);
	}

	public void close() throws Trouble {
		pager.close();
	}

	private Page getPage(final int pageNumber) {
		try {
			return pager.readPage(pageNumber);
		} catch (Trouble e) {
			throw new RuntimeException(e);
		}
	}

	public long count(final int pageNumber) throws Trouble {
		if (pageNumber <= 0)
			return 0;
		final Page page = getPage(pageNumber);
		final int cells = PageHeader.getCellsCount(page);
		if (PageHeader.isLeafPage(page)) {
			return cells;
		} else {
			long count = 0;
			for (int i = 0; i < cells; ++i) {
				count += count(page.getData().getInt(
						PageHeader.getCellOffset(page, i)));
			}
			return count + count(PageHeader.getRightMostChildPageNumber(page));
		}
	}

	public interface Cursor extends Iterable<Entry> {
		long count() throws Trouble;
	}

	public Cursor cursor(final int pageNumber) {
		return new Cursor() {
			public Iterator<Entry> iterator() {
				return newIterator(pageNumber);
			}

			public long count() throws Trouble {
				return BTree.this.count(pageNumber);
			}
		};
	}

	public Iterable<Entry> page(final int pageNumber) {
		return new Iterable<Entry>() {
			public Iterator<Entry> iterator() {
				return newIterator(pageNumber);
			}
		};
	}

	public Iterator<Entry> newIterator(final int pageNumber) {
		final Page page = getPage(pageNumber);
		final boolean isLeaf = PageHeader.isLeafPage(page);
		final boolean isTable = PageHeader.isTablePage(page);
		if (isTable) {
			if (isLeaf) {
				return new TableLeafIterator(page);
			} else {
				return new TableTrunkIterator(page);
			}
		} else {
			if (isLeaf) {
				return new IndexLeafIterator(page);
			} else {
				return new IndexTrunkIterator(page);
			}
		}
	}

	public interface Entry {
		long getRowId() throws Trouble;

		BTreeRecord getRecord() throws Trouble;
	}

	private static abstract class RecordEntry implements Entry {
		private final Pointer pointer;
		private BTreeRecord record = null;

		protected RecordEntry(final Pointer pointer) {
			this.pointer = pointer;
		}

		public BTreeRecord getRecord() throws Trouble {
			if (record == null)
				record = new BTreeRecord(pointer);
			return record;
		}
	}

	private static class TableEntry extends RecordEntry {
		private final long rowId;

		public TableEntry(final long rowId, final Pointer pointer) {
			super(pointer);
			this.rowId = rowId;
		}

		public long getRowId() {
			return rowId;
		}
	}

	private static class IndexEntry extends RecordEntry {

		public IndexEntry(final Pointer pointer) {
			super(pointer);
		}

		public long getRowId() throws Trouble {
			final int last = getRecord().getColumnsCount() - 1;
			if (getRecord().isInteger(last))
				return getRecord().getInteger(last);
			throw new Trouble(Code.ERROR);
		}
	}

	private abstract class AbstractIterator implements Iterator<Entry> {
		protected final Page page;
		protected int index = 0;

		protected AbstractIterator(final Page page) {
			this.page = page;
		}

		protected Memory getData() {
			return page.getData();
		}

		protected int getCellsCount() {
			return PageHeader.getCellsCount(page);
		}

		protected int getCellOffset(final int index) {
			return PageHeader.getCellOffset(page, index);
		}

		protected boolean hasNextCell() {
			return index < getCellsCount();
		}

		protected int nextCellOffset() {
			if (hasNextCell()) {
				return getCellOffset(index++);
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static class TrunkCell {
		public static int getChild(final Memory m, final int a) {
			return m.getInt(a);
		}
	}

	private static class TableTrunkCell extends TrunkCell {
		public static long getIntKey(final Memory m, final int a) {
			return VarInt.getValue(m, a + Memory.SIZE_INT);
		}

		public static int getIntKeySize(final Memory m, final int a) {
			return getIntKeySize(getIntKey(m, a));
		}

		public static int getIntKeySize(final long intKey) {
			return VarInt.getBytesCount(intKey);
		}
	}

	private abstract class PayloadCell {
		protected final Memory m;
		protected final int a;

		protected final long payloadSize;
		protected final int payloadSizeOffset;
		protected final int payloadLocalSize;

		public PayloadCell(final Memory m, final int a) {
			this.m = m;
			this.a = a;
			final int offset = a + childPointerSize();
			payloadSize = VarInt.getValue(m, offset);
			payloadSizeOffset = offset + VarInt.getBytesCount(payloadSize);
			payloadLocalSize = payloadSizes.getLocal(isIndex(), payloadSize);
		}

		protected int payloadOffset() {
			return payloadSizeOffset + intKeySize();
		}

		protected boolean isOverflow() {
			return payloadSize > payloadLocalSize;
		}

		protected int overflowPageNumber() throws Trouble {
			if (isOverflow()) {
				return m.getInt(payloadOffset() + payloadLocalSize);
			} else {
				return 0;
			}
		}

		protected abstract boolean isIndex();

		protected abstract int childPointerSize();

		protected abstract int intKeySize();

		public Pointer payload() {
			if (!isOverflow()) {
				return m.getPointer(payloadOffset());
			} else {
				// TODO overflow
				throw new UnsupportedOperationException();
			}
		}

	}

	public class TableLeafCell extends PayloadCell {
		private final long intKey;
		private final int intKeySize;

		public TableLeafCell(final Memory m, final int a) {
			super(m, a);
			intKey = VarInt.getValue(m, payloadSizeOffset);
			intKeySize = VarInt.getBytesCount(intKey);
		}

		@Override
		protected boolean isIndex() {
			return false;
		}

		@Override
		protected int childPointerSize() {
			return 0;
		}

		@Override
		protected int intKeySize() {
			return intKeySize;
		}

		public long intKey() {
			return intKey;
		}
	}

	public class IndexLeafCell extends PayloadCell {
		public IndexLeafCell(final Memory m, final int a) {
			super(m, a);
		}

		@Override
		protected boolean isIndex() {
			return true;
		}

		@Override
		protected int childPointerSize() {
			return 0;
		}

		@Override
		protected int intKeySize() {
			return 0;
		}
	}

	public class IndexTrunkCell extends PayloadCell {
		public IndexTrunkCell(final Memory m, final int a) {
			super(m, a);
		}

		@Override
		protected boolean isIndex() {
			return true;
		}

		@Override
		protected int childPointerSize() {
			return Memory.SIZE_INT;
		}

		@Override
		protected int intKeySize() {
			return 0;
		}

		public int getChild() {
			return m.getInt(a);
		}

	}

	private abstract class LeafIterator extends AbstractIterator {
		protected LeafIterator(final Page page) {
			super(page);
		}

		public boolean hasNext() {
			return hasNextCell();
		}

		public Entry next() {
			try {
				return entry(nextCellOffset());
			} catch (Trouble e) {
				throw new RuntimeException(e);
			}
		}

		protected abstract Entry entry(int cellOffset) throws Trouble;
	}

	public class TableLeafIterator extends LeafIterator {
		protected TableLeafIterator(Page page) {
			super(page);
		}

		@Override
		protected Entry entry(int cellOffset) throws Trouble {
			final TableLeafCell cell = new TableLeafCell(getData(), cellOffset);
			return new TableEntry(cell.intKey(), cell.payload());
		}
	}

	public class IndexLeafIterator extends LeafIterator {
		protected IndexLeafIterator(Page page) {
			super(page);
		}

		@Override
		protected Entry entry(int cellOffset) throws Trouble {
			final IndexLeafCell cell = new IndexLeafCell(getData(), cellOffset);
			return new IndexEntry(cell.payload());
		}
	}

	private abstract class TrunkIterator extends AbstractIterator {
		private Iterator<Entry> current;
		private Iterator<Entry> last;

		protected TrunkIterator(final Page page) {
			super(page);
		}

		public boolean hasNext() {
			return (last != null && last.hasNext())
					|| (current != null && current.hasNext()) || hasNextCell()
					|| last == null || last.hasNext();
		}

		public Entry next() {
			if (last != null) {
				return last.next();
			} else if (current != null && current.hasNext()) {
				return current.next();
			}
			try {
				if (hasNextCell()) {
					current = newIterator(TrunkCell.getChild(getData(),
							nextCellOffset()));
					return current.next();
				} else {
					last = newIterator(PageHeader
							.getRightMostChildPageNumber(page));
					return last.next();
				}
			} catch (Trouble e) {
				throw new RuntimeException(e);
			}
		}

	}

	public class TableTrunkIterator extends TrunkIterator {
		protected TableTrunkIterator(Page page) {
			super(page);
		}
	}

	public class IndexTrunkIterator extends TrunkIterator {
		protected IndexTrunkIterator(Page page) {
			super(page);
		}
	}

}
