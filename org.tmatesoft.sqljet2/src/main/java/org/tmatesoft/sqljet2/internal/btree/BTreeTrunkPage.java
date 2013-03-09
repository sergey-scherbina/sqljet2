package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.system.Trouble;

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
		if (cellNumber < header.getCellsCount()) {
			return getData().getInt(getCellOffset(cellNumber));
		} else {
			return header.getRightMostChildPageNumber();
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
		return getChildPage(header.getCellsCount()).getLastLeafPage();
	}

}
