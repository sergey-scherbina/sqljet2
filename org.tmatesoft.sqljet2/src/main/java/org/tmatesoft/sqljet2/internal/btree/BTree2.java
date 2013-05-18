package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.pager.Pager;

public class BTree2 {
	private final Pager pager;
	private final PayloadSizes payloadSizes;

	public BTree2(final Pager pager) {
		this.pager = pager;
		final int usableSize = pager.getPageSize(); // TODO - reservedSize
		payloadSizes = new PayloadSizes(usableSize);
	}
	
}
