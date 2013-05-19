package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class BTree2 {
	private final Pager pager;

	public BTree2(final Pager pager) {
		this.pager = pager;
	}

	private Page getPage(final int pageNumber) {
		try {
			return pager.readPage(pageNumber);
		} catch (Trouble e) {
			throw new RuntimeException(e);
		}
	}

}
