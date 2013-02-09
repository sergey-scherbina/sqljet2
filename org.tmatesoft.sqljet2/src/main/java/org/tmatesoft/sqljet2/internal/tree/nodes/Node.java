package org.tmatesoft.sqljet2.internal.tree.nodes;

import org.tmatesoft.sqljet2.internal.system.MemoryBlock;
import org.tmatesoft.sqljet2.internal.system.VarInt;
import org.tmatesoft.sqljet2.internal.tree.TreePage;

public interface Node<C extends Node.Cell> extends TreePage {

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
