package org.tmatesoft.sqljet2.internal.tree.pages;

import org.tmatesoft.sqljet2.internal.tree.TreePage;

public interface FreeList extends TreePage {

	interface Trunk extends FreeList {

	}

	interface Leaf extends FreeList {

	}

}
