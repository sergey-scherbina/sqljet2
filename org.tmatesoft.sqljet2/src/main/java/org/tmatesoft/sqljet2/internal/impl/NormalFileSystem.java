package org.tmatesoft.sqljet2.internal.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.tmatesoft.sqljet2.internal.FileStream;
import org.tmatesoft.sqljet2.internal.FileStream.FileType;
import org.tmatesoft.sqljet2.internal.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.FileSystem;

public class NormalFileSystem implements FileSystem {

	public String getFileSystemName() {
		return "default";
	}

	public String getFullPath(final String path) throws IOException {
		return new File(path).getAbsolutePath();
	}

	public FileStream open(final String path, final FileType fileType,
			final OpenPermission permission) throws IOException {
		// TODO draft implementation
		if (path != null) {
			return new NormalFile(fileType, new RandomAccessFile(path, "rw"));
		} else {
			return open(getTempFile(), fileType, permission);
		}
	}

	public boolean delete(File path, boolean sync) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkPermission(File path, AccessPermission permission)
			throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getTempFile() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
