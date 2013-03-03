package org.tmatesoft.sqljet2.internal.pager.impl;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.MemoryBlock;

public class MemoryPage implements Page {

	private final Pager pager;

	private final int pageNumber;

	private final MemoryBlock data;

	public MemoryPage(final Pager pager, final int pageNumber,
			final MemoryBlock data) {
		this.pager = pager;
		this.pageNumber = pageNumber;
		this.data = data;
	}

	public Pager getPager() {
		return pager;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public MemoryBlock getData() {
		return data;
	}

}
