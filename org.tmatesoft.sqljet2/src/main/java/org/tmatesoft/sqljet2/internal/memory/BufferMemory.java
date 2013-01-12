package org.tmatesoft.sqljet2.internal.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final public class BufferMemory extends AbstractMemory {

	private final ByteBuffer buffer;

	public BufferMemory(final ByteBuffer buffer) {
		this.buffer = buffer;
		buffer.order(ByteOrder.nativeOrder());
	}

	public BufferMemory(final byte[] array) {
		this(ByteBuffer.wrap(array));
	}

	public BufferMemory(final int size, final boolean direct) {
		this(direct ? ByteBuffer.allocateDirect(size) : ByteBuffer
				.allocate(size));
	}

	public BufferMemory(final int size) {
		this(size, false);
	}

	final public byte get(final int address) {
		return buffer.get(address);
	}

	final public void put(final int address, final byte value) {
		buffer.put(address, value);
	}

	final public short getShort(final int address) {
		return buffer.getShort(address);
	}

	final public void putShort(final int address, final short value) {
		buffer.putShort(address, value);
	}

	final public int getInt(final int address) {
		return buffer.getInt(address);
	}

	final public void putInt(final int address, final int value) {
		buffer.putInt(address, value);
	}

	final public long getLong(final int address) {
		return buffer.getLong(address);
	}

	final public void putLong(final int address, final long value) {
		buffer.putLong(address, value);
	}

	final public char getChar(int address) {
		return buffer.getChar(address);
	}

	final public void putChar(int address, char value) {
		buffer.putChar(address, value);
	}

	final public float getFloat(int address) {
		return buffer.getFloat(address);
	}

	final public void putFloat(int address, float value) {
		buffer.putFloat(address, value);
	}

	final public double getDouble(int address) {
		return buffer.getDouble(address);
	}

	final public void putDouble(int address, double value) {
		buffer.putDouble(address, value);
	}

}
