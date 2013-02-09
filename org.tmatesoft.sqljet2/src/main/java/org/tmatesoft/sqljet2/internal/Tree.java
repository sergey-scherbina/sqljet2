package org.tmatesoft.sqljet2.internal;

import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.tree.Root;

public interface Tree {

	void open(String fileName) throws Trouble;

	void close();

	Root getRoot();

	interface Page {

		enum PageType {
			Node, Overflow, PointerMap, FreeList, LockByte
		}

		Pager.Page getPage();

		PageType getPageType();

	}

}
