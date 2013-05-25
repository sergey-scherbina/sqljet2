package org.tmatesoft.sqljet2.internal.system.impl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

final public class ArrayMemory extends AbstractMemory implements
		NormalFile.Streamable {

	private final byte[] array;

	public ArrayMemory(final byte[] array) {
		this.array = array;
	}

	public ArrayMemory(final int size) {
		this(new byte[size]);
	}

	public int getSize() {
		return array.length;
	}

	final public byte getByte(final int address) {
		return array[address];
	}

	final public void putByte(final int address, final byte value) {
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

	final public char getChar(final int address) {
		return getChar(this, address);
	}

	final public void putChar(final int address, final char value) {
		putChar(this, address, value);
	}

	final public float getFloat(final int address) {
		return getFloat(this, address);
	}

	final public void putFloat(final int address, final float value) {
		putFloat(this, address, value);
	}

	final public double getDouble(final int address) {
		return getDouble(this, address);
	}

	final public void putDouble(final int address, final double value) {
		putDouble(this, address, value);
	}

	final public void fill(final byte value) {
		Arrays.fill(array, value);
	}

	public void getBytes(final int address, final byte[] to) {
		System.arraycopy(array, address, to, 0, to.length);
	}

	public void putBytes(final int address, final byte[] from) {
		System.arraycopy(from, 0, array, address, from.length);
	}

	public int read(final int address, final RandomAccessFile stream,
			final long position, final int count) throws IOException {
		stream.seek(position);
		return stream.read(array, address, count);
	}

	public void write(final int address, final RandomAccessFile stream,
			final long position, final int count) throws IOException {
		stream.seek(position);
		stream.write(array, address, count);
	}

    public ByteBuffer getBuffer(int address, int size) {
        return ByteBuffer.wrap(array,address,size);
    }
}
