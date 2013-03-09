package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import static org.tmatesoft.sqljet2.internal.btree.BTreePageHeader.*;

public class BTree {

	private final Pager pager;

	private final int rootPageNumber;

	private final BTreePage rootPage;

	private BTreeLeafPage leafPage = null;

	private int leafCellNumber = 0;

	public BTree(final Pager pager, final int rootPageNumber) throws Trouble {
		this.pager = pager;
		this.rootPageNumber = rootPageNumber;
		this.rootPage = BTreePage.getBTreePage(pager, rootPageNumber);
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
			leafCellNumber = getCellsCount(leafPage.getPage()) - 1;
		}
	}

	public boolean isEmpty() {
		return leafPage == null;
	}

	public boolean next() throws Trouble {
		if (++leafCellNumber >= getCellsCount(leafPage.getPage())) {
			leafPage = leafPage.getNextLeafPage();
			leafCellNumber = 0;
		}
		return leafPage != null;
	}

	public boolean prev() throws Trouble {
		if (--leafCellNumber < 0) {
			leafPage = leafPage.getPrevLeafPage();
			if (leafPage != null) {
				leafCellNumber = getCellsCount(leafPage.getPage()) - 1;
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
