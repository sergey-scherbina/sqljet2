package org.tmatesoft.sqljet2.internal.tree;


public interface Node extends TreePage {
	
	enum NodeType {
		Root, TableTrunk, TableLeaf, IndexTrunk, IndexLeaf
	}

	NodeType getNodeType();

}
