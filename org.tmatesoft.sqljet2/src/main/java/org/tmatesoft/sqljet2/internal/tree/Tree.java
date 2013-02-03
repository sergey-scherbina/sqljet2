package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.tree.nodes.RootNode;

public interface Tree {

	void open(String fileName) throws Trouble;

	void close();

	RootNode getRoot();

}
