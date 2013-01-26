package org.tmatesoft.sqljet2.internal;

import java.io.IOException;

import org.tmatesoft.sqljet2.internal.FileSystem.OpenPermission;

public interface Pager {

	void open(FileSystem fs, String fileName, OpenPermission permission)
			throws IOException;

	void close() throws IOException;

	int getPageSize();

	void setPageSize(int pageSize);

	int getPagesCount() throws IOException;

	Memory readFileHeader(final int count) throws IOException;

	Page readPage(int pageNumber);

	Page lookupPage(int pageNumber);

}
