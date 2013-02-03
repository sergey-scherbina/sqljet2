package org.tmatesoft.sqljet2.internal.tree.nodes;

public interface IndexNode extends Node {

	interface Leaf extends LeafNode<Leaf.Cell> {
		interface Cell {
		}
	}

	interface Trunk extends TrunkNode<Trunk.Cell> {
		interface Cell {
		}
	}

}
