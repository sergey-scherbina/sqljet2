package org.tmatesoft.sqljet2.internal;

public interface Page {

	Pager getPager();

	int getPageNumber();

	MemoryBlock getData();

	Tree.Node getNode();

}
