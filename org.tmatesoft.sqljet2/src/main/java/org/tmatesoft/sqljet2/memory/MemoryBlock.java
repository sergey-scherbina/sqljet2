package org.tmatesoft.sqljet2.memory;

public interface MemoryBlock extends Memory {

	int getSize();

	Pointer getBegin();

	Pointer getEnd();

}
