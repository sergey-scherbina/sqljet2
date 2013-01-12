package org.tmatesoft.sqljet2.internal;

public interface MemoryBlock extends Memory {

	int getSize();

	Pointer getBegin();

	Pointer getEnd();

	void fill(byte value);
	
	void clear();

}
