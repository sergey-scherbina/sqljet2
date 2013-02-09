package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.Tree;


public interface Root extends Tree.Page {

	Header getHeader();

	Schema getSchema();

	interface Header {

	}

	interface Schema {

	}

}
