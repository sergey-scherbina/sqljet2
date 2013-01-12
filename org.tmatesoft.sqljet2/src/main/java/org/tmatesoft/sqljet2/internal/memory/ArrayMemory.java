package org.tmatesoft.sqljet2.internal.memory;

final public class ArrayMemory extends AbstractMemory {

	private final byte[] array;

	public ArrayMemory(final byte[] array) {
		this.array = array;
	}

	public ArrayMemory(final int size) {
		this(new byte[size]);
	}

	final public byte get(final int address) {
		return array[address];
	}

	final public void put(final int address, final byte value) {
		array[address] = value;
	}

	final public short getShort(final int address) {
		return getShort(this, address);
	}

	final public void putShort(int address, short value) {
		putShort(this, address, value);
	}

	final public int getInt(int address) {
		return getInt(this, address);
	}

	final public void putInt(final int address, final int value) {
		putInt(this, address, value);
	}

	final public long getLong(final int address) {
		return getLong(this, address);
	}

	final public void putLong(final int address, final long value) {
		putLong(this, address, value);
	}

	final public char getChar(int address) {
		return getChar(this, address);
	}

	final public void putChar(int address, char value) {
		putChar(this, address, value);
	}

	final public float getFloat(int address) {
		return getFloat(this, address);
	}

	final public void putFloat(int address, float value) {
		putFloat(this, address, value);
	}

	final public double getDouble(int address) {
		return getDouble(this, address);
	}

	final public void putDouble(int address, double value) {
		putDouble(this, address, value);
	}

}
