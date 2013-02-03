package org.tmatesoft.sqljet2.internal.pager.impl;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.PageType;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.FileSystem;
import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;

public class DefaultPager implements Pager {

	private static final int MIN_SECTOR_SIZE = 512;
	private static final int MAX_SECTOR_SIZE = 0x0100000;

	private static final int MAX_PAGE_NUMBER = 2147483647;

	public void open(FileSystem fs, String fileName, OpenPermission permission)
			throws Trouble {
		// TODO Auto-generated method stub

	}

	public void close() throws Trouble {
		// TODO Auto-generated method stub

	}

	public int getPageSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setPageSize(int pageSize) {
		// TODO Auto-generated method stub

	}

	public int getPagesCount() throws Trouble {
		// TODO Auto-generated method stub
		return 0;
	}

	public Memory readFileHeader(int count) throws Trouble {
		// TODO Auto-generated method stub
		return null;
	}

	public Page readPage(int pageNumber, PageType type) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page lookupPage(int pageNumber, PageType type) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLockBytePageNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isLockBytePageNumber() {
		// TODO Auto-generated method stub
		return false;
	}

}
