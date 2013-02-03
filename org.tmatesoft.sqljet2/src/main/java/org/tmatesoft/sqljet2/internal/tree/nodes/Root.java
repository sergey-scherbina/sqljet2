package org.tmatesoft.sqljet2.internal.tree.nodes;

import org.tmatesoft.sqljet2.internal.tree.Node;
import org.tmatesoft.sqljet2.internal.tree.pages.FreeList;


public interface Root extends Node {

	interface Header {

	}

	Header getHeader();

	FreeList getFreeList();

	interface Schema extends Table {

	}

	Schema getSchema();

}
