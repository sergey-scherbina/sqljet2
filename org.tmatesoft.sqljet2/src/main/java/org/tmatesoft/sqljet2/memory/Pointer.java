package org.tmatesoft.sqljet2.memory;

public interface Pointer extends Memory {

	Memory getMemory();

	Pointer getPointer();

	int getAddress();

	void setAddress(int address);

	void move(int offset);

	byte get();

	void put(byte value);

	short getShort();

	void putShort(short value);

	int getInt();

	void putInt(int value);

	long getLong();

	void putLong(long value);

	char getChar();

	void putChar(char value);

	float getFloat();

	void putFloat(float value);

	double getDouble();

	void putDouble(double value);

	boolean getBoolean();

	void setBoolean(boolean value);

	short getUnsignedByte();

	void putUnsignedByte(short value);

	int getUnsignedShort();

	void putUnsignedShort(int value);

	long getUnsignedInt();

	void putUnsignedInt(long value);

}