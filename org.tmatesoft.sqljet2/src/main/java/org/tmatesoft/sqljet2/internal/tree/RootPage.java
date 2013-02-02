package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.Page;

public interface RootPage extends Page {

	interface Header {

	}

	Header getHeader();

	FreeList getFreeList();

	interface SchemaTree extends Table {

	}

	SchemaTree getSchemaTree();

}
