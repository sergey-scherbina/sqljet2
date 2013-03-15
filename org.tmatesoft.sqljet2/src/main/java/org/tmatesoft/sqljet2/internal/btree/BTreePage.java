package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.btree.BTreePageHeader.Def;
import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.StructDef;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.StructDef.SignedByte;
import org.tmatesoft.sqljet2.internal.system.StructDef.SignedInt;
import org.tmatesoft.sqljet2.internal.system.StructDef.UnsignedByte;
import org.tmatesoft.sqljet2.internal.system.StructDef.UnsignedShort;
import org.tmatesoft.sqljet2.internal.system.Trouble.Code;

import static org.tmatesoft.sqljet2.internal.btree.BTreePageHeader.*;

abstract public class BTreePage {

	public static final BTreePage getBTreePage(final Pager pager,
			final int pageNumber) throws Trouble {
		return getBTreePage(pager, pageNumber, null, 0);
	}

	public static final BTreePage getBTreePage(final Pager pager,
			final int pageNumber, final BTreeTrunkPage parent,
			final int parentCellNumber) throws Trouble {
		final Page page = pager.readPage(pageNumber);
		if (isTrunkPage(page)) {
			return new BTreeTrunkPage(page, parent, parentCellNumber);
		} else {
			return new BTreeLeafPage(page, parent, parentCellNumber);
		}
	}

	protected final BTreeTrunkPage parent;
	protected final int parentCellNumber;

	protected final Page page;
	protected final Pointer data;

	public BTreePage(final Page page) {
		this(page, null, 0);
	}

	public BTreePage(final Page page, final BTreeTrunkPage parent,
			final int parentCellNumber) {
		this.parent = parent;
		this.parentCellNumber = parentCellNumber;
		this.page = page;
		this.data = page.getData().getBegin();
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

	public int getCellOffset(int cellNumber) {
		final int offset = getCellsOffset(page) + cellNumber * 2;
		return getData().getUnsignedShort(offset);
	}

	public Pointer getCell(int cellNumber) {
		return getData().getPointer(getCellOffset(cellNumber));
	}

	abstract public BTreeLeafPage getFirstLeafPage() throws Trouble;

	abstract public BTreeLeafPage getLastLeafPage() throws Trouble;

}
