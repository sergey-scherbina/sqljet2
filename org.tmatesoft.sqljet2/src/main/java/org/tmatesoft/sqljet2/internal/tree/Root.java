package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.pager.FreeList;

public interface Root extends Table {

	interface Header {

	}

	Header getHeader();

	FreeList getFreeList();

	interface Schema extends Table {

	}

	Schema getSchema();

}
