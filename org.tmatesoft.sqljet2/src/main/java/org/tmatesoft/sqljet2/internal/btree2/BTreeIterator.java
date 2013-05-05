package org.tmatesoft.sqljet2.internal.btree2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.tmatesoft.sqljet2.internal.btree.BTreeRecord;
import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class BTreeIterator implements Iterable<BTreeRecord> {

	public static Iterator<BTreeRecord> newIterator(final Page page) {
		if (PageHeader.isLeafPage(page)) {
			return new LeafIterator(page);
		} else {
			return new TrunkIterator(page);
		}
	}

	protected final Page page;

	public BTreeIterator(final Page page) {
		this.page = page;
	}

	public Iterator<BTreeRecord> iterator() {
		return newIterator(page);
	}

	private static class CellsIterator implements Iterator<Pointer> {

		private final Page page;
		private int index = 0;

		public CellsIterator(final Page page) {
			this.page = page;
		}

		protected int getCellsCount() {
			return PageHeader.getCellsCount(page);
		}

		protected Pointer getCell(final int index) {
			return PageHeader.getCell(page, index);
		}

		public boolean hasNext() {
			return index < getCellsCount();
		}

		public Pointer next() {
			if (hasNext()) {
				return getCell(index++);
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
		}
	}

	public static class LeafIterator implements Iterator<BTreeRecord> {

		private final CellsIterator cells;

		public LeafIterator(final Page page) {
			cells = new CellsIterator(page);
		}

		public boolean hasNext() {
			return cells.hasNext();
		}

		public BTreeRecord next() {
			try {
				final Pointer cell = cells.next();
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

	public static class TrunkIterator implements Iterator<BTreeRecord> {

		private final CellsIterator cells;

		private Iterator<BTreeRecord> current = null;

		public TrunkIterator(final Page page) {
			cells = new CellsIterator(page);
		}

		public boolean hasNext() {
			return (current!=null && current.hasNext()) || cells.hasNext();
		}

		public BTreeRecord next() {
			if (current == null || !current.hasNext())
				try {
					final Pointer cell = cells.next();
					final int child = TableTrunkCell.getLeftChild(
							cell.getMemory(), cell.getAddress());
					current = newIterator(cells.page.getPager().readPage(child));
				} catch (Trouble e) {
					throw new RuntimeException(e);
				}
			return current.next();
		}

		public void remove() {
		}

	}

}
