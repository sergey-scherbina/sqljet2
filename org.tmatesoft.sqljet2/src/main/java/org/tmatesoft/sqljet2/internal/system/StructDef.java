package org.tmatesoft.sqljet2.internal.system;

public final class StructDef {

	private int size = 0;

	public int getSize() {
		return size;
	}

	public SignedByte signedByte() {
		return new SignedByte();
	}

	public UnsignedByte unsignedByte() {
		return new UnsignedByte();
	}

	public SignedShort signedShort() {
		return new SignedShort();
	}

	public UnsignedShort unsignedShort() {
		return new UnsignedShort();
	}

	public SignedInt signedInt() {
		return new SignedInt();
	}

	public UnsignedInt unsignedInt() {
		return new UnsignedInt();
	}

	public Bytes bytes(final int count) {
		return new Bytes(count);
	}

	private final int offset(final int size) {
		final int offset = this.size;
		this.size += size;
		return offset;
	}

	private class Member {
		protected final int offset;

		public int getOffset() {
			return offset;
		}

		public Member(final int size) {
			this.offset = StructDef.this.offset(size);
		}
	}

	public final class SignedByte extends Member {
		public SignedByte() {
			super(Memory.SIZE_BYTE);
		}

		public final byte get(final Memory m, final int address) {
			return m.getByte(address + offset);
		}

		public final void set(final Memory m, final int address,
				final byte value) {
			m.putByte(address + offset, value);
		}
	}

	public final class UnsignedByte extends Member {
		public UnsignedByte() {
			super(Memory.SIZE_BYTE);
		}

		public final short get(final Memory m, final int address) {
			return m.getUnsignedByte(address + offset);
		}

		public final void set(final Memory m, final int address,
				final short value) {
			m.putUnsignedByte(address + offset, value);
		}
	}

	public final class SignedShort extends Member {
		public SignedShort() {
			super(Memory.SIZE_SHORT);
		}

		public final short get(final Memory m, final int address) {
			return m.getShort(address + offset);
		}

		public final void set(final Memory m, final int address,
				final short value) {
			m.putShort(address + offset, value);
		}
	}

	public final class UnsignedShort extends Member {
		public UnsignedShort() {
			super(Memory.SIZE_SHORT);
		}

		public final int get(final Memory m, final int address) {
			return m.getUnsignedShort(address + offset);
		}

		public final void set(final Memory m, final int address, final int value) {
			m.putUnsignedShort(address + offset, value);
		}
	}

	public final class SignedInt extends Member {
		public SignedInt() {
			super(Memory.SIZE_INT);
		}

		public final int get(final Memory m, final int address) {
			return m.getInt(address + offset);
		}

		public final void set(final Memory m, final int address, final int value) {
			m.putInt(address + offset, value);
		}
	}

	public final class UnsignedInt extends Member {
		public UnsignedInt() {
			super(Memory.SIZE_INT);
		}

		public final long get(final Memory m, final int address) {
			return m.getUnsignedInt(address + offset);
		}

		public final void set(final Memory m, final int address,
				final long value) {
			m.putUnsignedInt(address + offset, value);
		}
	}

	public final class Bytes extends Member {

		final private int count;

		public Bytes(final int count) {
			super(count);
			this.count = count;
		}

		public final byte[] get(final Memory m, final int address) {
			final byte[] to = new byte[count];
			get(m, address, to);
			return to;
		}

		public final void get(final Memory m, final int address, final byte[] to) {
			m.getBytes(address + offset, to);
		}

		public final void set(final Memory m, final int address,
				final long value) {
			m.putUnsignedInt(address + offset, value);
		}
	}

}
