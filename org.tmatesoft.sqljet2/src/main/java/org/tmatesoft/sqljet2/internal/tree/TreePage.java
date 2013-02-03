package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.pager.Page;

public interface TreePage {

	enum PageType {
		Node, Overflow, PointerMap, FreeList, LockByte
	}

	Page getPage();

	PageType getPageType();

}
