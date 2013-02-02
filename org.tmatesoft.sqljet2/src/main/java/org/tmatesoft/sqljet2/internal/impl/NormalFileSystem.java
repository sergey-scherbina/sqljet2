package org.tmatesoft.sqljet2.internal.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.tmatesoft.sqljet2.internal.system.FileStream;
import org.tmatesoft.sqljet2.internal.system.FileSystem;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;

public class NormalFileSystem implements FileSystem {

	public String getFileSystemName() {
		return "default";
	}

	public String getFullPath(final String path) throws Trouble {
		return new File(path).getAbsolutePath();
	}

	public FileStream open(final String path, final OpenPermission permission)
			throws Trouble {
		// TODO draft implementation
		if (path != null) {
			try {
				return new NormalFile(new RandomAccessFile(path, "rw"));
			} catch (IOException e) {
				throw new Trouble(e);
			}
		} else {
			return open(getTempFile(), permission);
		}
	}

	public boolean delete(File path, boolean sync) throws Trouble {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkPermission(File path, AccessPermission permission)
			throws Trouble {
		// TODO Auto-generated method stub
		return false;
	}

	public String getTempFile() throws Trouble {
		// TODO Auto-generated method stub
		return null;
	}

}
