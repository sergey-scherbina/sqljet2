package org.tmatesoft.sqljet2.internal.pager;

import org.tmatesoft.sqljet2.internal.system.FileSystem;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public interface Pager {

	void open(FileSystem fs, String fileName, OpenPermission permission)
			throws Trouble;

	void close() throws Trouble;

	int getPageSize();

	void setPageSize(int pageSize);

	int getPagesCount() throws Trouble;

	int getLockBytePageNumber();

	boolean isLockBytePageNumber();

	Memory readHeader(final int count) throws Trouble;

	Page readPage(int pageNumber);

	Page lookupPage(int pageNumber);

}
