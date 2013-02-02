package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.system.Trouble;

public interface Tree {

	void open(String fileName) throws Trouble;

	void close();

	Root getRoot();

}
