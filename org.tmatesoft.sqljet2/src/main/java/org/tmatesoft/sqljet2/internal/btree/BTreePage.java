package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

abstract public class BTreePage {

	public static final BTreePage getBTreePage(final Pager pager,
			final int pageNumber) throws Trouble {
		return getBTreePage(pager, pageNumber, null, 0);
	}

	public static final BTreePage getBTreePage(final Pager pager,
			final int pageNumber, final BTreeTrunkPage parent,
			final int parentCellNumber) throws Trouble {
		final Page page = pager.readPage(pageNumber);
		final BTreePageHeader header = new BTreePageHeader(page);
		if (header.isTrunkPage()) {
			return new BTreeTrunkPage(page, parent, parentCellNumber);
		} else {
			return new BTreeLeafPage(page, parent, parentCellNumber);
		}
	}

	protected final BTreeTrunkPage parent;
	protected final int parentCellNumber;

	protected final Page page;
	protected final Pointer data;

	protected final BTreePageHeader header;

	public BTreePage(final Page page) {
		this(page, null, 0);
	}

	public BTreePage(final Page page, final BTreeTrunkPage parent,
			final int parentCellNumber) {
		this.parent = parent;
		this.parentCellNumber = parentCellNumber;
		this.page = page;
		this.data = page.getData().getBegin();
		this.header = new BTreePageHeader(page);
	}

	public BTreeTrunkPage getParent() {
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

	abstract public BTreeLeafPage getFirstLeafPage() throws Trouble;

	abstract public BTreeLeafPage getLastLeafPage() throws Trouble;

}
