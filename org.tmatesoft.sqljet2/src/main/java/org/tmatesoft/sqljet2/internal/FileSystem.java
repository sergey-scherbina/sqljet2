package org.tmatesoft.sqljet2.internal;

import java.io.File;
import java.io.IOException;

import org.tmatesoft.sqljet2.internal.FileStream.FileType;

public interface FileSystem {

	String getFileSystemName();

	String getFullPath(String path) throws IOException;

	FileStream open(String path, FileType fileType) throws IOException;

	boolean delete(File path, boolean sync) throws IOException;

	boolean checkPermission(final File path, final AccessPermission permission)
			throws IOException;

	String getTempFile() throws IOException;

	enum AccessPermission {
		EXISTS, READONLY, READWRITE
	}

}
