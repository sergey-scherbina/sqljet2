package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import static org.tmatesoft.sqljet2.internal.btree.BTreePageHeader.*;

public class BTreeTrunkPage extends BTreePage {

	public BTreeTrunkPage(final Page page) {
		this(page, null, 0);
	}

	public BTreeTrunkPage(Page page, BTreeTrunkPage parent, int parentCellNumber) {
		super(page, parent, parentCellNumber);
	}

	public int getChildPageNumber(final int cellNumber) throws Trouble {
		if (cellNumber <= 0) {
			return 0;
		}
		if (cellNumber < getCellsCount(page)) {
			return getData().getInt(getCellOffset(cellNumber));
		} else {
			return getRightMostChildPageNumber(page);
		}
	}

	public BTreePage getChildPage(final int cellNumber) throws Trouble {
		return BTreePage.getBTreePage(page.getPager(),
				getChildPageNumber(cellNumber), this, cellNumber);
	}

	public BTreeLeafPage getFirstLeafPage() throws Trouble {
		return getChildPage(0).getFirstLeafPage();
	}

	public BTreeLeafPage getLastLeafPage() throws Trouble {
		return getChildPage(getCellsCount(page)).getLastLeafPage();
	}

}
