package org.tmatesoft.sqljet2.internal.btree2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.tmatesoft.sqljet2.internal.btree.BTreeRecord;
import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public abstract class BTreeIterator implements Iterator<Pointer> {

	protected final Page page;

	protected BTreeIterator(final Page page) {
		this.page = page;
	}

	protected int getCellsCount() {
		return PageHeader.getCellsCount(page);
	}
	
	protected Pointer getCell(final int index) {
		return PageHeader.getCell(page,index);
	}

	public static class LeafIterator extends BTreeIterator {

		private int index = 0;

		public LeafIterator(Page root) {
			super(root);
		}

		public boolean hasNext() {
			return index < getCellsCount();
		}

		public Pointer next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			return getCell(index++);
		}

		public void remove() {
		}

	}

}
