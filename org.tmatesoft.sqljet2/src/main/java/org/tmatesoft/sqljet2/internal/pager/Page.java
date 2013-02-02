package org.tmatesoft.sqljet2.internal.pager;

import org.tmatesoft.sqljet2.internal.system.MemoryBlock;

public interface Page {

	Pager getPager();

	int getPageNumber();

	MemoryBlock getData();

	Usage getUsage();

	interface Usage {

		Page getPage();

		Type getUsageType();

		enum Type {

			Root, Node, LockByte,

			FreeListTrunk, FreeListLeaf,

			Overflow, PointerMap

		}

	}

}
