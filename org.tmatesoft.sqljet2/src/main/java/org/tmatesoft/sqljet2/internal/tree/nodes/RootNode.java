package org.tmatesoft.sqljet2.internal.tree.nodes;

import org.tmatesoft.sqljet2.internal.tree.pages.FreeList;

public interface RootNode extends Node {

	interface Header {

	}

	Header getHeader();

	FreeList getFreeList();

	interface Schema {

	}

	Schema getSchema();

}
