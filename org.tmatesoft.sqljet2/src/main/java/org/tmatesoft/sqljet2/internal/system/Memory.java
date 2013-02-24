package org.tmatesoft.sqljet2.internal.system;

public interface Memory {

	short MAX_UNSIGNED_BYTE = 0xFF;
	int MAX_UNSIGNED_SHORT = 0xFFFF;
	long MAX_UNSIGNED_INT = 0xFFFFFFFFL;

	int SIZE_BYTE = 1;
	int SIZE_SHORT = 2;
	int SIZE_INT = 4;
	int SIZE_LONG = 8;
	int SIZE_FLOAT = 4;
	int SIZE_DOUBLE = 8;

	byte getByte(int address);

	void putByte(int address, byte value);

	short getShort(int address);

	void putShort(int address, short value);

	int getInt(int address);

	void putInt(int address, int value);

	long getLong(int address);

	void putLong(int address, long value);

	char getChar(int address);

	void putChar(int address, char value);

	float getFloat(int address);

	void putFloat(int address, float value);

	double getDouble(int address);

	void putDouble(int address, double value);

	boolean getBoolean(int address);

	void putBoolean(int address, boolean value);

	short getUnsignedByte(int address);

	void putUnsignedByte(int address, short value);

	int getUnsignedShort(int address);

	void putUnsignedShort(int address, int value);

	long getUnsignedInt(int address);

	void putUnsignedInt(int address, long value);

	Pointer getPointer(int address);

	void getBytes(int address, byte[] to);

	void putBytes(int address, byte[] from);

}
