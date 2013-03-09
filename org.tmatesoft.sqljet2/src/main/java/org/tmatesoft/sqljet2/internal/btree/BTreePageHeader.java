package org.tmatesoft.sqljet2.internal.btree;

import static org.tmatesoft.sqljet2.internal.btree.BTreePageHeader.getHeaderOffset;
import static org.tmatesoft.sqljet2.internal.btree.BTreePageHeader.getHeaderSize;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.Trouble.Code;

/**
 * <pre>
 * B-tree Page Header Format
 * Offset	Size	Description
 * 0	1	A flag indicating the b-tree page type A value of 2 means the page is an interior index b-tree page. A value of 5 means the page is an interior table b-tree page. A value of 10 means the page is a leaf index b-tree page. A value of 13 means the page is a leaf table b-tree page. Any other value for the b-tree page type is an error.
 * 1	2	Byte offset into the page of the first freeblock
 * 3	2	Number of cells on this page
 * 5	2	Offset to the first byte of the cell content area. A zero value is used to represent an offset of 65536, which occurs on an empty root page when using a 65536-byte page size.
 * 7	1	Number of fragmented free bytes within the cell content area
 * 8	4	The right-most pointer (interior b-tree pages only)
 * </pre>
 */

public class BTreePageHeader {

	public static final int ROOT_PAGENUMBER = 1;
	public static final int ROOT_HEADER = 100;

	public static final byte TRUNK_INDEX = 2;
	public static final byte TRUNK_TABLE = 5;
	public static final byte LEAF_INDEX = 10;
	public static final byte LEAF_TABLE = 13;

	public static final int HEADER_SIZE_LEAFPAGE = 8;
	public static final int HEADER_SIZE_TRUNKPAGE = 12;

	public static final int HEADER_OFFSET_PAGETYPE = 0;
	public static final int HEADER_OFFSET_FIRSTFREEBLOCKOFFSET = 1;
	public static final int HEADER_OFFSET_CELLSCOUNT = 3;
	public static final int HEADER_OFFSET_CELLSAREAOFFSET = 5;
	public static final int HEADER_OFFSET_FRAGMENTEDBYTESCOUNT = 7;
	public static final int HEADER_OFFSET_RIGHTMOST = 8;

	private BTreePageHeader() {
	}

	private static Memory getData(final Page page) {
		return page.getData();
	}

	public static final int getHeaderOffset(final Page page) {
		return page.getPageNumber() == ROOT_PAGENUMBER ? ROOT_HEADER : 0;
	}

	public static byte getPageType(final Page page) {
		return getData(page).getByte(
				getHeaderOffset(page) + HEADER_OFFSET_PAGETYPE);
	}

	public static int getFirstFreeBlockOffset(final Page page) {
		return getData(page).getUnsignedShort(
				getHeaderOffset(page) + HEADER_OFFSET_FIRSTFREEBLOCKOFFSET);
	}

	public static int getCellsCount(final Page page) {
		return getData(page).getUnsignedShort(
				getHeaderOffset(page) + HEADER_OFFSET_CELLSCOUNT);
	}

	public static int getCellsAreaOffset(final Page page) {
		return getData(page).getUnsignedShort(
				getHeaderOffset(page) + HEADER_OFFSET_CELLSAREAOFFSET);
	}

	public static short getFragmentedBytesCount(final Page page) {
		return getData(page).getUnsignedByte(
				getHeaderOffset(page) + HEADER_OFFSET_FRAGMENTEDBYTESCOUNT);
	}

	public static int getRightMostChildPageNumber(final Page page)
			throws Trouble {
		if (isTrunkPage(page)) {
			return getData(page).getInt(
					getHeaderOffset(page) + HEADER_OFFSET_RIGHTMOST);
		}
		throw new Trouble(Code.ERROR);
	}

	public static boolean isTrunkPage(final Page page) {
		final byte t = getPageType(page);
		return t == TRUNK_INDEX || t == TRUNK_TABLE;
	}

	public static boolean isLeafPage(final Page page) {
		return !isTrunkPage(page);
	}

	public static boolean isTablePage(final Page page) {
		final byte t = getPageType(page);
		return t == TRUNK_TABLE || t == TRUNK_TABLE;
	}

	public static boolean isIndexPage(final Page page) {
		return !isTablePage(page);
	}

	public static int getHeaderSize(final Page page) {
		return isTrunkPage(page) ? HEADER_SIZE_TRUNKPAGE : HEADER_SIZE_LEAFPAGE;
	}

	public static int getCellsOffset(final Page page) {
		return getHeaderOffset(page) + getHeaderSize(page);
	}

}
