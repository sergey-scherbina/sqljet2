package org.tmatesoft.sqljet2.internal.tree.nodes;

import org.tmatesoft.sqljet2.internal.tree.TreePage;

public interface Node extends TreePage {

	enum NodeType {
		Root, TableTrunk, TableLeaf, IndexTrunk, IndexLeaf
	}

	NodeType getNodeType();
	
}
