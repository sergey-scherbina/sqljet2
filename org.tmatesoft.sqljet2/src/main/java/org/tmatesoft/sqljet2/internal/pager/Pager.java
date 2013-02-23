package org.tmatesoft.sqljet2.internal.pager;

import org.tmatesoft.sqljet2.internal.system.FileSystem;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.MemoryBlock;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public interface Pager {

	int MIN_PAGESIZE = 512;
	int DEFAULT_PAGESIZE = 1024;
	int MAX_PAGESIZE = 65536;
	
	int MAX_PAGENUMBER = 2147483647;
	
	int LOCKPAGE_BEGIN = 1073741824;
	int LOCKPAGE_END = LOCKPAGE_BEGIN + MIN_PAGESIZE;
	
	PagerCache getPagerCache();
	
	FileSystem getFileSystem();
	
	void open(String fileName, OpenPermission permission) throws Trouble;

	void close() throws Trouble;

	int getPageSize();

	void setPageSize(int pageSize);

	int getPagesCount() throws Trouble;

	int getLockBytePageNumber();

	boolean isLockBytePageNumber(int pageNumber);

	MemoryBlock readHeader(final int count) throws Trouble;

	Page readPage(int pageNumber) throws Trouble;

	Page lookupPage(int pageNumber);

}
