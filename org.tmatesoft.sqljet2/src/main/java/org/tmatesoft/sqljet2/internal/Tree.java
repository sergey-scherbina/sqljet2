package org.tmatesoft.sqljet2.internal;

import org.tmatesoft.sqljet2.internal.tree.RootPage;

public interface Tree {

	void open(String fileName) throws Trouble;

	void close();

	RootPage getRootPage();

}
