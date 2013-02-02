package org.tmatesoft.sqljet2.internal.system;

import java.io.File;
import java.io.IOException;

public interface FileSystem {

	String getFileSystemName();

	String getFullPath(String path) throws Trouble;

	FileStream open(String path, OpenPermission permission) throws Trouble;

	boolean delete(File path, boolean sync) throws Trouble;

	boolean checkPermission(final File path, final AccessPermission permission)
			throws Trouble;

	String getTempFile() throws Trouble;

	enum OpenPermission {
		READONLY, READWRITE, CREATE
	}

	enum AccessPermission {
		EXISTS, READONLY, READWRITE
	}

}
