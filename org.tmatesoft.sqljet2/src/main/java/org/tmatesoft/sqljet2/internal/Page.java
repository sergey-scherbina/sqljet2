package org.tmatesoft.sqljet2.internal;

public interface Page {

	Pager getPager();

	int getPageNumber();

	MemoryBlock getData();

	enum Type {
		RootPage,

		LockByte,

		FreeListLeaf,

		FreeListTrunk,

		IndexLeaf,

		IndexTrunk,

		TableLeaf,

		TableTrunk,

		Overflow,

		PointerMap

	}

	Type getType();

}
