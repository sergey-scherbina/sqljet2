package org.tmatesoft.sqljet2.internal.btree.impl;

import java.io.UnsupportedEncodingException;

import org.tmatesoft.sqljet2.internal.btree.BTreeRecord;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.Trouble.Code;
import org.tmatesoft.sqljet2.internal.system.VarInt;
import org.tmatesoft.sqljet2.internal.system.impl.AbstractMemory;

public class BTreeRecordImpl implements BTreeRecord {

	private final Object NULL = new Object();

	private final Pointer pointer;

	private int headerSize = 0;
	private int columnsCount = 0;
	private int[] columnsTypes;
	private int[] columnsOffsets;
	private Object[] values;

	public BTreeRecordImpl(final Pointer pointer) throws Trouble {
		this.pointer = pointer;
		parse();
	}

	private void parse() throws Trouble {
		headerSize = (int) VarInt.varInt.getValue(pointer, 0);
		columnsTypes = parseColumns(pointer, headerSize,
				VarInt.varInt.getBytesCount(headerSize), 0);
		columnsCount = columnsTypes.length;
		columnsOffsets = new int[columnsCount];
		values = new Object[columnsCount];
		int offset = headerSize;
		for (int i = 0; i < columnsCount; i++) {
			columnsOffsets[i] = offset;
			offset += getTypeSize(columnsTypes[i]);
		}
	}

	private static int[] parseColumns(final Pointer pointer,
			final long headerSize, final int offset, final int column) {
		if (offset < headerSize) {
			final long columnType = VarInt.varInt.getValue(pointer, offset);
			final int[] columns = parseColumns(pointer, headerSize, offset
					+ VarInt.varInt.getBytesCount(columnType), column + 1);
			columns[column] = (int) columnType;
			return columns;
		} else {
			return new int[column];
		}
	}

	public int getColumnsCount() {
		return columnsTypes.length;
	}

	public int getType(final int column) throws Trouble {
		if (column > getColumnsCount())
			throw new Trouble(Code.ERROR);
		return columnsTypes[column];
	}

	public Object getValue(final int column) throws Trouble {
		if (column > getColumnsCount())
			throw new Trouble(Code.ERROR);
		if (values[column] == null)
			values[column] = readValue(column);
		return values[column] != NULL ? values[column] : null;
	}

	private int getTypeSize(final int type) throws Trouble {
		switch (type) {
		case 0:
			return 0;
		case 1:
			return 1;
		case 2:
			return 2;
		case 3:
			return 3;
		case 4:
			return 4;
		case 5:
			return 6;
		case 6:
			return 8;
		case 7:
			return 8;
		case 8:
			return 0;
		case 9:
			return 0;
		case 10:
		case 11:
			throw new Trouble(Code.ERROR);
		default:
			if (type >= 12 && isEven(type))
				return (type - 12) / 2;
			if (type >= 13 && !isEven(type))
				return (type - 13) / 2;
		}
		throw new Trouble(Code.ERROR);
	}

	private static boolean isEven(int n) {
		return n % 2 == 0;
	}

	private Object readValue(int column) throws Trouble {
		final int type = getType(column);
		final int offset = columnsOffsets[column];
		switch (type) {
		case 0:
			return NULL;
		case 1:
			return pointer.getByte(offset);
		case 2:
			return pointer.getShort(offset);
		case 3:
			return (pointer.getByte(offset) << 16)
					| (pointer.getUnsignedByte(offset + 1) << 8)
					| pointer.getUnsignedByte(offset + 2);
		case 4:
			return pointer.getInt(offset);
		case 5:
			long x = (pointer.getUnsignedByte(offset) << 8)
					| pointer.getUnsignedByte(offset + 1);
			int y = (pointer.getUnsignedByte(offset + 2) << 24)
					| (pointer.getUnsignedByte(offset + 3) << 16)
					| (pointer.getUnsignedByte(offset + 4) << 8)
					| pointer.getUnsignedByte(offset + 5);
			return ((long) (short) x << 32) | AbstractMemory.toUnsignedInt(y);
		case 6:
			return pointer.getLong(offset);
		case 7:
			return pointer.getDouble(offset);
		case 8:
			return 0;
		case 9:
			return 1;
		case 10:
		case 11:
			throw new Trouble(Code.ERROR);
		default:
			if (type >= 12 && isEven(type)) {
				final int size = (type - 12) / 2;
				byte[] b = new byte[size];
				pointer.getBytes(offset, b);
				return b;
			}
			if (type >= 13 && !isEven(type)) {
				final int size = (type - 13) / 2;
				byte[] b = new byte[size];
				pointer.getBytes(offset, b);
				try {
					return new String(b, "US-ASCII");
				} catch (UnsupportedEncodingException e) {
					throw new Trouble(e);
				}
			}
		}
		throw new Trouble(Code.ERROR);
	}

}
