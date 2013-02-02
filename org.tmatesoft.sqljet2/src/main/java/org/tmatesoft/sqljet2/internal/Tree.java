package org.tmatesoft.sqljet2.internal;

import org.tmatesoft.sqljet2.internal.tree.Root;

public interface Tree {

	void open(String fileName) throws Trouble;

	void close();

	Root getRoot();

	interface Node {

		Page getPage();

		Type getType();

		enum Type {

			Root, LockByte,

			FreeListLeaf, FreeListTrunk,

			IndexLeaf, IndexTrunk,

			TableLeaf, TableTrunk,

			Overflow, PointerMap

		}

	}

}
