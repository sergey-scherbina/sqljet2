package org.tmatesoft.sqljet2.internal.pager.impl;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.pager.PagerCache;
import org.tmatesoft.sqljet2.internal.system.FileStream;
import org.tmatesoft.sqljet2.internal.system.FileSystem;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.MemoryBlock;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.impl.ArrayMemory;
import org.tmatesoft.sqljet2.internal.system.impl.DefaultFileSystem;

public class FilePager implements Pager {

	private final PagerCache cache;
	private final FileSystem fs;

	private FileStream stream;

	private int pageSize = DEFAULT_PAGESIZE;

	public FilePager() {
		this(new HeapPagerCache(), new DefaultFileSystem());
	}

	public FilePager(final FileSystem fs) {
		this(new HeapPagerCache(), fs);
	}

	public FilePager(final PagerCache cache) {
		this(cache, new DefaultFileSystem());
	}

	public FilePager(final PagerCache cache, final FileSystem fs) {
		this.cache = cache;
		this.fs = fs;
	}

	public PagerCache getPagerCache() {
		return cache;
	}

	public FileSystem getFileSystem() {
		return fs;
	}

	public void open(final String fileName, final OpenPermission permission)
			throws Trouble {
		this.stream = fs.open(fileName, permission);
	}

	public void close() throws Trouble {
		if (stream != null) {
			stream.close();
			stream = null;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPagesCount() throws Trouble {
		return (int) stream.getSize() / pageSize;
	}

	public int getLockBytePageNumber() {
		return LOCKPAGE_BEGIN / pageSize;
	}

	public boolean isLockBytePageNumber(int pageNumber) {
		int pageOffset = pageNumber * pageSize;
		return pageOffset > LOCKPAGE_BEGIN && pageOffset < LOCKPAGE_END;
	}

	private MemoryBlock read(int position, int count) throws Trouble {
		final MemoryBlock mem = new ArrayMemory(count);
		stream.read(position, mem.getBegin(), count);
		return mem;
	}

	public MemoryBlock readHeader(int count) throws Trouble {
		return read(0, count);
	}

	public Page readPage(int pageNumber) throws Trouble {
		{
			final Page page = lookupPage(pageNumber);
			if (page != null) {
				return page;
			}
		}
		{
			final MemoryBlock data = read(pageSize * (pageNumber - 1), pageSize);
			final Page page = new MemoryPage(this, pageNumber, data);
			cache.putPage(page);
			return page;
		}
	}

	public Page lookupPage(int pageNumber) {
		return cache.getPage(pageNumber);
	}

}
