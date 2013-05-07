package org.tmatesoft.sqljet2.internal.btree;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class BTree implements Iterable<BTreeRecord> {

	public static Iterator<BTreeRecord> newIterator(final Page page) {
		if (PageHeader.isLeafPage(page)) {
			return new LeafIterator(page);
		} else {
			return new TrunkIterator(page);
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

	private static class LeafIterator extends Cells implements
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
				return new BTreeRecord(cell.getMemory().getPointer(
						TableLeafCell.getPayloadOffset(cell.getMemory(),
								cell.getAddress())));
			} catch (Trouble e) {
				throw new RuntimeException(e);
			}
		}

		public void remove() {
		}
	}

	private static class TrunkIterator extends Cells implements
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

		private Page getChildPage(final int pageNumber) throws Trouble {
			return page.getPager().readPage(pageNumber);
		}

		public BTreeRecord next() {
			if (last != null) {
				return last.next();
			} else if (current != null && current.hasNext()) {
				return current.next();
			}
			try {
				if (hasNextCell()) {
					final Pointer cell = nextCell();
					final int child = TableTrunkCell.getLeftChild(
							cell.getMemory(), cell.getAddress());
					current = newIterator(getChildPage(child));
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

		public void remove() {
		}
	}
}
