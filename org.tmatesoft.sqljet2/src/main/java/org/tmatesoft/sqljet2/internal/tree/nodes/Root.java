package org.tmatesoft.sqljet2.internal.tree.nodes;

import org.tmatesoft.sqljet2.internal.tree.TreePage;


public interface Root extends TreePage {

	Header getHeader();

	Schema getSchema();

	interface Header {

	}

	interface Schema {

	}

}
