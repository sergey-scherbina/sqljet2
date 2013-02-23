package org.tmatesoft.sqljet2.internal.system;

public interface VarInt {

	int getValue(Pointer ptr);

	byte getBytesCount(long value);

	void setValue(Pointer ptr, long value);
	
}
