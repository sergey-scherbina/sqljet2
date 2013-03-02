package org.tmatesoft.sqljet2.internal.btree.impl;

import org.tmatesoft.sqljet2.internal.btree.BTree;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class BTreeImpl implements BTree {

	private final Pager pager;

	private final int rootPageNumber;

	private final BTreePage rootPage;

	private BTreePage leafPage = null;

	private int leafCellNumber = 0;

	public BTreeImpl(final Pager pager, final int rootPageNumber)
			throws Trouble {
		this.pager = pager;
		this.rootPageNumber = rootPageNumber;
		this.rootPage = new BTreePage(pager.readPage(rootPageNumber));
		leafCellNumber = 0;
		leafPage = rootPage.getFirstLeafPage();
	}

	public Pager getPager() {
		return pager;
	}

	public int getRootPageNumber() {
		return rootPageNumber;
	}

	public BTreePage getRootPage() {
		return rootPage;
	}

	public void begin() throws Trouble {
		leafCellNumber = 0;
		leafPage = rootPage.getFirstLeafPage();
	}

	public void end() throws Trouble {
		leafPage = rootPage.getLastLeafPage();
		if (leafPage != null) {
			leafCellNumber = leafPage.getCellsCount()-1;
		}
	}

	public boolean isEmpty() {
		return leafPage == null;
	}

	public boolean next() throws Trouble {
		if (++leafCellNumber >= leafPage.getCellsCount()) {
			leafPage = leafPage.getNextLeafPage();
			leafCellNumber = 0;
		}
		return leafPage != null;
	}

	public boolean prev() throws Trouble {
		if (--leafCellNumber < 0) {
			leafPage = leafPage.getPrevLeafPage();
			if (leafPage != null) {
				leafCellNumber = leafPage.getCellsCount()-1;
			} else {
				leafCellNumber = 0;
			}
		}
		return leafPage != null;
	}

	public Pointer getCell() {
		return isEmpty() ? null : leafPage.getCell(leafCellNumber);
	}

}
