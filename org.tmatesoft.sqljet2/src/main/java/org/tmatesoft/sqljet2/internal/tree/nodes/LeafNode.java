package org.tmatesoft.sqljet2.internal.tree.nodes;

public interface LeafNode<C> extends Node {

	int getCellsCount();

	C getCell(int index);

}
