package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.system.MemoryBlock;

public interface Index {
	
	int getKey(MemoryBlock data);

	interface Leaf extends Node<Node.Cell.Payload> {
	}

	interface Trunk extends Node.Trunk<Trunk.Cell> {
		interface Cell extends Node.Trunk.Cell, Node.Cell.Payload {
		}
	}

}
