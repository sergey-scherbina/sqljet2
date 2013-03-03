package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class BTreePage {

	private final BTreePage parent;
	private final int parentCellNumber;

	private final Page page;
	private final Pointer data;

	private final BTreePageHeader header;

	public BTreePage(final Page page) {
		this(page, null, 0);
	}

	public BTreePage(final Page page, final BTreePage parent,
			final int parentCellNumber) {
		this.parent = parent;
		this.parentCellNumber = parentCellNumber;
		this.page = page;
		this.data = page.getData().getBegin();
		this.header = new BTreePageHeader(page);
	}

	public BTreePage getParent() {
		return parent;
	}

	public int getParentCellNumber() {
		return parentCellNumber;
	}

	public Page getPage() {
		return page;
	}

	public Pointer getData() {
		return data;
	}
	
	public BTreePageHeader getHeader() {
		return header;
	}

	public int getCellOffset(int cellNumber) {
		return getData().getUnsignedShort(
				header.getHeaderOffset() + header.getHeaderSize() + cellNumber
						* 2);
	}

	public Pointer getCell(int cellNumber) {
		return getData().getPointer(getCellOffset(cellNumber));
	}

	public BTreePage getFirstLeafPage() throws Trouble {
		if (header.isLeafPage()) {
			if (getParent() == null) {
				return this;
			} else {
				return getParent().getFirstLeafPage();
			}
		} else {
			return getChildPage(0).getFirstLeafPage();
		}
	}

	public BTreePage getLastLeafPage() throws Trouble {
		if (header.isLeafPage()) {
			if (getParent() == null) {
				return this;
			} else {
				return getParent().getLastLeafPage();
			}
		} else {
			return getChildPage(header.getCellsCount()).getLastLeafPage();
		}
	}

	public int getChildPageNumber(final int cellNumber) throws Trouble {
		assert (header.isTrunkPage());
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
		assert (header.isTrunkPage());
		final int childPageNumber = getChildPageNumber(cellNumber);
		final Page childPage = getPage().getPager().readPage(childPageNumber);
		return new BTreePage(childPage, this, cellNumber);
	}

	public BTreePage getNextLeafPage() throws Trouble {
		assert (header.isLeafPage());
		if (getParent() == null)
			return null;
		return getParent().getChildPage(getParentCellNumber() + 1);
	}

	public BTreePage getPrevLeafPage() throws Trouble {
		assert (header.isLeafPage());
		if (getParent() == null)
			return null;
		return getParent().getChildPage(getParentCellNumber() - 1);
	}

}
