package org.tmatesoft.sqljet2.internal.btree.impl;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.Trouble.Code;

/**<pre>
B-tree Page Header Format
Offset	Size	Description
0	1	A flag indicating the b-tree page type A value of 2 means the page is an interior index b-tree page. A value of 5 means the page is an interior table b-tree page. A value of 10 means the page is a leaf index b-tree page. A value of 13 means the page is a leaf table b-tree page. Any other value for the b-tree page type is an error.
1	2	Byte offset into the page of the first freeblock
3	2	Number of cells on this page
5	2	Offset to the first byte of the cell content area. A zero value is used to represent an offset of 65536, which occurs on an empty root page when using a 65536-byte page size.
7	1	Number of fragmented free bytes within the cell content area
8	4	The right-most pointer (interior b-tree pages only)
</pre>*/

public class BTreePage {

	public static final int ROOT_PAGENUMBER = 1;
	public static final int ROOT_HEADER = 100;

	public static final byte TRUNK_INDEX = 2;
	public static final byte TRUNK_TABLE = 5;
	public static final byte LEAF_INDEX = 13;
	public static final byte LEAF_TABLE = 15;
	
	public static final int HEADER_SIZE_LEAFPAGE = 8;
	public static final int HEADER_SIZE_TRUNKPAGE = 12;

	public static final int HEADER_OFFSET_PAGETYPE = 0;
	public static final int HEADER_OFFSET_FIRSTFREEBLOCKOFFSET = 1;
	public static final int HEADER_OFFSET_CELLSNUMBER = 3;
	public static final int HEADER_OFFSET_CELLSAREAOFFSET = 5;
	public static final int HEADER_OFFSET_FRAGMENTEDBYTESCOUNT = 7;
	public static final int HEADER_OFFSET_RIGHTMOST = 8;

	private final Page page;
	private final Pointer data;

	public BTreePage(final Page page) {
		this.page = page;
		this.data = page.getData().getPointer(getHeaderOffset());
	}

	public Page getPage() {
		return page;
	}

	public final int getHeaderOffset() {
		return page.getPageNumber() == ROOT_PAGENUMBER ? ROOT_HEADER : 0;
	}

	public Pointer getData() {
		return data;
	}

	public byte getPageType() {
		return getData().getByte(HEADER_OFFSET_PAGETYPE);
	}

	public boolean isTrunkPage() {
		final byte t = getPageType();
		return t == TRUNK_INDEX || t == TRUNK_TABLE;
	}

	public boolean isTablePage() {
		final byte t = getPageType();
		return t == TRUNK_TABLE || t == TRUNK_TABLE;
	}

	public int getHeaderSize() {
		return isTrunkPage() ? HEADER_SIZE_TRUNKPAGE : HEADER_SIZE_LEAFPAGE;
	}

	public int getFirstFreeBlockOffset() {
		return getData().getUnsignedShort( HEADER_OFFSET_FIRSTFREEBLOCKOFFSET );
	}

	public int getCellsNumber() {
		return getData().getUnsignedShort( HEADER_OFFSET_CELLSNUMBER );
	}

	public int getCellsAreaOffset() {
		return getData().getUnsignedShort( HEADER_OFFSET_CELLSAREAOFFSET );
	}

	public short getFragmentedBytesCount() {
		return getData().getUnsignedByte( HEADER_OFFSET_FRAGMENTEDBYTESCOUNT );
	}

	public int getRightMostChildPageNumber() throws Trouble {
		if(isTrunkPage()) {
			return getData().getInt(HEADER_OFFSET_RIGHTMOST);
		}
		throw new Trouble(Code.ERROR);
	}
	
	public int getCellOffset(int cellNumber) {
		return getData().getUnsignedShort(getHeaderSize() + cellNumber * 2);
	}

	public Pointer getCellData(int cellNumber) {
		return getData().getPointer(getCellOffset(cellNumber));
	}

}
