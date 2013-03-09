package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class BTreeLeafPage extends BTreePage {

	public BTreeLeafPage(final Page page) {
		this(page, null, 0);
	}

	public BTreeLeafPage(Page page, BTreeTrunkPage parent, int parentCellNumber) {
		super(page, parent, parentCellNumber);
	}

	public BTreeLeafPage getNextLeafPage() throws Trouble {
		if (getParent() == null)
			return null;
		return (BTreeLeafPage) getParent().getChildPage(
				getParentCellNumber() + 1);
	}

	public BTreeLeafPage getPrevLeafPage() throws Trouble {
		if (getParent() == null)
			return null;
		return (BTreeLeafPage) getParent().getChildPage(
				getParentCellNumber() - 1);
	}

	public BTreeLeafPage getFirstLeafPage() throws Trouble {
		if (getParent() == null) {
			return this;
		} else {
			return getParent().getFirstLeafPage();
		}
	}

	public BTreeLeafPage getLastLeafPage() throws Trouble {
		if (getParent() == null) {
			return this;
		} else {
			return getParent().getLastLeafPage();
		}
	}

}
