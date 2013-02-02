package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.pager.FreeList;

public interface Root extends Tree.Node {

	interface Header {

	}

	Header getHeader();

	FreeList getFreeList();

	interface SchemaTree extends Table {

	}

	SchemaTree getSchemaTree();

}
