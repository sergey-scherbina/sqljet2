package org.tmatesoft.sqljet2.internal.memory;

import org.tmatesoft.sqljet2.memory.Memory;
import org.tmatesoft.sqljet2.memory.Pointer;

public class MemoryPointer implements Pointer {

	private final Memory memory;
	private int address;

	public MemoryPointer(final Memory memory, final int address) {
		this.memory = memory;
		this.address = address;
	}

	final public Memory getMemory() {
		return memory;
	}

	final public int getAddress() {
		return address;
	}

	final public void setAddress(final int address) {
		this.address = address;
	}

	final public void move(final int offset) {
		this.address += offset;
	}

	final public Pointer getPointer(final int offset) {
		return memory.getPointer(offset);
	}

	final public Pointer getPointer() {
		return getPointer(0);
	}

	final public byte get(final int offset) {
		return memory.get(this.address + offset);
	}

	final public void put(final int offset, final byte value) {
		memory.put(this.address + offset, value);
	}

	final public short getShort(final int offset) {
		return memory.getShort(this.address + offset);
	}

	final public void putShort(final int offset, final short value) {
		memory.putShort(this.address + offset, value);
	}

	final public int getInt(final int offset) {
		return memory.getInt(this.address + offset);
	}

	final public void putInt(final int offset, final int value) {
		memory.putInt(this.address + offset, value);
	}

	final public long getLong(final int offset) {
		return memory.getLong(this.address + offset);
	}

	final public void putLong(final int offset, final long value) {
		memory.putLong(this.address + offset, value);
	}

	final public char getChar(final int offset) {
		return memory.getChar(address + offset);
	}

	final public void putChar(final int offset, final char value) {
		memory.putChar(address + offset, value);
	}

	final public float getFloat(final int offset) {
		return memory.getFloat(address + offset);
	}

	final public void putFloat(final int offset, final float value) {
		memory.putFloat(address + offset, value);
	}

	final public double getDouble(final int offset) {
		return memory.getChar(address + offset);
	}

	final public void putDouble(final int offset, final double value) {
		memory.putDouble(address + offset, value);
	}

	final public boolean getBoolean(final int offset) {
		return memory.getBoolean(this.address + offset);
	}

	final public void putBoolean(final int offset, final boolean value) {
		memory.putBoolean(this.address + offset, value);
	}

	public short getUnsignedByte(final int offset) {
		return memory.getUnsignedByte(this.address + offset);
	}

	final public void putUnsignedByte(final int offset, final short value) {
		memory.putUnsignedByte(this.address + offset, value);
	}

	final public int getUnsignedShort(final int offset) {
		return memory.getUnsignedShort(this.address + offset);
	}

	public void putUnsignedShort(final int offset, final int value) {
		memory.putUnsignedShort(this.address + offset, value);
	}

	final public long getUnsignedInt(final int offset) {
		return memory.getUnsignedInt(this.address + offset);
	}

	final public void putUnsignedInt(final int offset, final long value) {
		memory.putUnsignedInt(this.address + offset, value);
	}

	final public byte get() {
		return get(0);
	}

	final public void put(final byte value) {
		put(0, value);
	}

	final public short getShort() {
		return getShort(0);
	}

	final public void putShort(final short value) {
		putShort(0, value);
	}

	final public int getInt() {
		return getInt(0);
	}

	final public void putInt(final int value) {
		putInt(0);
	}

	final public long getLong() {
		return getLong(0);
	}

	final public void putLong(final long value) {
		putLong(0, value);
	}

	final public char getChar() {
		return getChar(0);
	}

	final public void putChar(final char value) {
		putChar(0, value);
	}

	final public float getFloat() {
		return getFloat(0);
	}

	final public void putFloat(final float value) {
		putFloat(0, value);
	}

	final public double getDouble() {
		return getDouble(0);
	}

	final public void putDouble(final double value) {
		putDouble(0, value);
	}

	final public boolean getBoolean() {
		return getBoolean(0);
	}

	final public void setBoolean(final boolean value) {
		putBoolean(0, value);
	}

	final public short getUnsignedByte() {
		return getUnsignedByte(0);
	}

	final public void putUnsignedByte(final short value) {
		putUnsignedByte(0, value);
	}

	final public int getUnsignedShort() {
		return getUnsignedShort(0);
	}

	final public void putUnsignedShort(final int value) {
		putUnsignedShort(0, value);
	}

	final public long getUnsignedInt() {
		return getUnsignedInt(0);
	}

	final public void putUnsignedInt(final long value) {
		putUnsignedInt(0, value);
	}

}
