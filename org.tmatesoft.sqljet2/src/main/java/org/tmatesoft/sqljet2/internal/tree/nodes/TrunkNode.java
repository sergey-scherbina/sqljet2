package org.tmatesoft.sqljet2.internal.tree.nodes;

public interface TrunkNode<C> extends LeafNode<C> {

	int getRightMostPageNumber();

}
