package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.Tree;
import org.tmatesoft.sqljet2.internal.system.MemoryBlock;
import org.tmatesoft.sqljet2.internal.system.VarInt;

public interface Node<C extends Node.Cell> extends Tree.Page {

	enum Type {

		IndexTrunk(2), TableTrunk(5), IndexLeaf(10), TableLeaf(13);

		public final byte value;

		private Type(final int value) {
			this.value = (byte) value;
		}

	}

	Type getType();

	int getCellsCount();

	C getCell(int index);

	interface Cell {

		interface Payload extends Cell {

			VarInt getBytesCount();

			MemoryBlock getNotOverflowedPart();

			int getOverflowPageNumber();

		}

	}

	interface Trunk<C extends Trunk.Cell> extends Node<C> {

		int getRightMostPageNumber();

		interface Cell extends Node.Cell {

			int getLeftChildPageNumber();

		}

	}

}
