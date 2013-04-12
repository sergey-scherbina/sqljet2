package org.tmatesoft.sqljet2.internal.btree2;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.StructDef;
import org.tmatesoft.sqljet2.internal.system.StructDef.SignedByte;
import org.tmatesoft.sqljet2.internal.system.StructDef.SignedInt;
import org.tmatesoft.sqljet2.internal.system.StructDef.UnsignedByte;
import org.tmatesoft.sqljet2.internal.system.StructDef.UnsignedShort;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.Trouble.Code;

public class PageHeader {

	public interface Def {

		int ROOT_PAGENUMBER = 1;
		int ROOT_HEADER = 100;

		byte TRUNK_INDEX = 2;
		byte TRUNK_TABLE = 5;
		byte LEAF_INDEX = 10;
		byte LEAF_TABLE = 13;

		int HEADER_SIZE_LEAFPAGE = 8;
		int HEADER_SIZE_TRUNKPAGE = 12;

		StructDef def = new StructDef();

		SignedByte pageType = def.signedByte();

		UnsignedShort firstFreeBlockOffset = def.unsignedShort();

		UnsignedShort cellsCount = def.unsignedShort();

		UnsignedShort cellsAreaOffset = def.unsignedShort();

		UnsignedByte fragmentedBytesCount = def.unsignedByte();

		SignedInt rightMostChildPageNumber = def.signedInt();

	}

	private PageHeader() {
	}

	private static Memory getData(final Page page) {
		return page.getData();
	}

	public static boolean isRootPage(final Page page) {
		return page.getPageNumber() == Def.ROOT_PAGENUMBER;
	}

	public static final int getHeaderOffset(final Page page) {
		return isRootPage(page) ? Def.ROOT_HEADER : 0;
	}

	public static byte getPageType(final Page page) {
		return Def.pageType.get(getData(page), getHeaderOffset(page));
	}

	public static int getFirstFreeBlockOffset(final Page page) {
		return Def.firstFreeBlockOffset.get(getData(page),
				getHeaderOffset(page));
	}

	public static int getCellsCount(final Page page) {
		return Def.cellsCount.get(getData(page), getHeaderOffset(page));
	}

	public static int getCellsAreaOffset(final Page page) {
		return Def.cellsAreaOffset.get(getData(page), getHeaderOffset(page));
	}

	public static short getFragmentedBytesCount(final Page page) {
		return Def.fragmentedBytesCount.get(getData(page),
				getHeaderOffset(page));
	}

	public static int getRightMostChildPageNumber(final Page page)
			throws Trouble {
		if (!isTrunkPage(page))
			throw new Trouble(Code.ERROR);
		return Def.rightMostChildPageNumber.get(getData(page),
				getHeaderOffset(page));
	}

	public static boolean isTrunkPage(final Page page) {
		final byte t = getPageType(page);
		return t == Def.TRUNK_INDEX || t == Def.TRUNK_TABLE;
	}

	public static boolean isLeafPage(final Page page) {
		return !isTrunkPage(page);
	}

	public static boolean isTablePage(final Page page) {
		final byte t = getPageType(page);
		return t == Def.TRUNK_TABLE || t == Def.TRUNK_TABLE;
	}

	public static boolean isIndexPage(final Page page) {
		return !isTablePage(page);
	}

	public static int getHeaderSize(final Page page) {
		return isTrunkPage(page) ? Def.HEADER_SIZE_TRUNKPAGE
				: Def.HEADER_SIZE_LEAFPAGE;
	}

	public static int getCellsOffset(final Page page) {
		return getHeaderOffset(page) + getHeaderSize(page);
	}

}
