package org.tmatesoft.sqljet2.internal;

public interface Page<T extends Page<T>> {

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

	T getPage();

}
