package org.tmatesoft.sqljet2.internal.system;

public final class VarInt {

	private VarInt() {
	}

	public final static byte getBytesCount(long v) {
		byte i = 0;
		do {
			i++;
			v >>= 7;
		} while (v != 0 && i < 9);
		return i;
	}

	public final static long getValue(Pointer p, int offset) {
		long l = 0;
		for (byte i = 0; i < 8; i++) {
			final int b = p.getUnsignedByte(i + offset);
			l = (l << 7) | (b & 0x7f);
			if ((b & 0x80) == 0) {
				return l;
			}
		}
		final int b = p.getUnsignedByte(8 + offset);
		l = (l << 8) | b;
		return l;
	}

	public final static void setValue(Pointer ptr, int offset, long value) {
		// TODO Auto-generated method stub

	}
}
