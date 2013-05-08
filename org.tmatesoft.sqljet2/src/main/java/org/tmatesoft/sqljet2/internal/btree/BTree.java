package org.tmatesoft.sqljet2.internal.btree;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class BTree implements Iterable<BTreeRecord> {

	public static Iterator<BTreeRecord> newIterator(final Page page) {
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

	private final Page page;

	public BTree(final Page page) {
		this.page = page;
	}

	public Iterator<BTreeRecord> iterator() {
		return newIterator(page);
	}

	private static class Cells {

		protected final Page page;
		private int index = 0;

		public Cells(final Page page) {
			this.page = page;
		}

		protected int getCellsCount() {
			return PageHeader.getCellsCount(page);
		}

		protected Pointer getCell(final int index) {
			return PageHeader.getCell(page, index);
		}

		protected boolean hasNextCell() {
			return index < getCellsCount();
		}

		protected Pointer nextCell() {
			if (hasNextCell()) {
				return getCell(index++);
			} else {
				throw new NoSuchElementException();
			}
		}
	}

	private static abstract class LeafIterator extends Cells implements
			Iterator<BTreeRecord> {

		public LeafIterator(final Page page) {
			super(page);
		}

		public boolean hasNext() {
			return hasNextCell();
		}

		public BTreeRecord next() {
			try {
				final Pointer cell = nextCell();
				return new BTreeRecord(getPayload(cell));
			} catch (Trouble e) {
				throw new RuntimeException(e);
			}
		}

		protected abstract Pointer getPayload(final Pointer cell);

		public void remove() {
		}
	}

	private static class TableLeafIterator extends LeafIterator {

		public TableLeafIterator(final Page page) {
			super(page);
		}

		protected Pointer getPayload(final Pointer cell) {
			return cell.getMemory().getPointer(
					TableLeafCell.getPayloadOffset(cell.getMemory(),
							cell.getAddress()));
		}
	}

	private static class IndexLeafIterator extends LeafIterator {

		public IndexLeafIterator(final Page page) {
			super(page);
		}

		protected Pointer getPayload(final Pointer cell) {
			return cell.getMemory().getPointer(
					IndexLeafCell.getPayloadOffset(cell.getMemory(),
							cell.getAddress()));
		}
	}

	private static abstract class TrunkIterator extends Cells implements
			Iterator<BTreeRecord> {

		private Iterator<BTreeRecord> current = null;
		private Iterator<BTreeRecord> last = null;

		public TrunkIterator(final Page page) {
			super(page);
		}

		public boolean hasNext() {
			return (last != null && last.hasNext())
					|| (current != null && current.hasNext()) || hasNextCell()
					|| last == null || last.hasNext();
		}

		public BTreeRecord next() {
			if (last != null) {
				return last.next();
			} else if (current != null && current.hasNext()) {
				return current.next();
			}
			try {
				if (hasNextCell()) {
					current = newIterator(getChildPage(getChild(nextCell())));
					return current.next();
				} else {
					last = newIterator(getChildPage(PageHeader
							.getRightMostChildPageNumber(page)));
					return last.next();
				}
			} catch (Trouble e) {
				throw new RuntimeException(e);
			}
		}

		protected abstract int getChild(final Pointer cell);

		private Page getChildPage(final int pageNumber) throws Trouble {
			return page.getPager().readPage(pageNumber);
		}

		public void remove() {
		}
	}

	private static class TableTrunkIterator extends TrunkIterator {

		public TableTrunkIterator(final Page page) {
			super(page);
		}

		protected int getChild(final Pointer cell) {
			return TableTrunkCell.getLeftChild(cell.getMemory(),
					cell.getAddress());
		}

	}

	private static class IndexTrunkIterator extends TrunkIterator {

		public IndexTrunkIterator(final Page page) {
			super(page);
		}

		protected int getChild(final Pointer cell) {
			return IndexTrunkCell.getLeftChild(cell.getMemory(),
					cell.getAddress());
		}

	}

}
