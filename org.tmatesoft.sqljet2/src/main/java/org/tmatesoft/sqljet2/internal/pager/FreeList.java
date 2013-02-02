package org.tmatesoft.sqljet2.internal.pager;


public interface FreeList extends Page {

	interface Trunk extends FreeList {

	}

	interface Leaf extends FreeList {

	}

}
