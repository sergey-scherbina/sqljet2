package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.Tree;

public interface FreeList extends Tree.Page {

	interface Trunk extends FreeList {

	}

	interface Leaf extends FreeList {

	}

}
