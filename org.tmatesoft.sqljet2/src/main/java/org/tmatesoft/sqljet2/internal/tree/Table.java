package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.system.MemoryBlock;
import org.tmatesoft.sqljet2.internal.system.VarInt;

public interface Table {

	MemoryBlock getData(int key);

	interface Cell extends Node.Cell {
		VarInt getKey();
	}

	interface Leaf extends Node<Leaf.Cell> {
		interface Cell extends Table.Cell, Node.Cell.Payload {
		}
	}

	interface Trunk extends Node.Trunk<Trunk.Cell> {
		interface Cell extends Table.Cell, Node.Trunk.Cell {
		}
	}

}
