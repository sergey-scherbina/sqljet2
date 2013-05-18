package org.tmatesoft.sqljet2.internal.system.impl;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.tmatesoft.sqljet2.internal.system.FileStream;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class NormalFile implements FileStream {

	public interface Streamable {

		int read(int address, RandomAccessFile stream, long position, int count)
				throws IOException;

		void write(int address, RandomAccessFile stream, long position, int count)
				throws IOException;
	}

	private final RandomAccessFile stream;

	public NormalFile(final RandomAccessFile stream) {
		this.stream = stream;
	}

	public void close() throws Trouble {
		try {
			stream.close();
		} catch (IOException e) {
			throw new Trouble(e);
		}
	}

	public int read(long position, Pointer pointer, int count) throws Trouble {
		try {
			if (pointer.getMemory() instanceof Streamable) {
				return ((Streamable) pointer.getMemory()).read(
						pointer.getAddress(), stream, position, count);
			} else {
				final byte[] b = new byte[count];
				stream.seek(position);
				final int read = stream.read(b);
				pointer.putBytes(b);
				return read;
			}
		} catch (IOException e) {
			throw new Trouble(e);
		}
	}

	public void write(long position, Pointer pointer, int count) throws Trouble {
		try {
			if (pointer.getMemory() instanceof Streamable) {
				((Streamable) pointer.getMemory()).write(pointer.getAddress(),
						stream, position, count);
			} else {
				final byte[] b = new byte[count];
				pointer.getBytes(b);
				stream.seek(position);
				stream.write(b);
			}
		} catch (IOException e) {
			throw new Trouble(e);
		}
	}

	public void sync(final boolean full) throws Trouble {
		try {
			stream.getChannel().force(full);
		} catch (IOException e) {
			throw new Trouble(e);
		}
	}

	public void truncate(final long size) throws Trouble {
		try {
			stream.setLength(size);
		} catch (IOException e) {
			throw new Trouble(e);
		}
	}

	public long getSize() throws Trouble {
		try {
			return stream.length();
		} catch (IOException e) {
			throw new Trouble(e);
		}
	}

	public int getSectorSize() {
		return SECTOR_SIZE;
	}

	// TODO mock stub - to implement
	private LockType lockType = LockType.NONE;

	public LockType getLockType() {
		// TODO mock stub - to implement
		return lockType;
	}

	public boolean lock(final LockType lockType) throws Trouble {
		// TODO mock stub - to implement
		this.lockType = lockType;
		return true;
	}

	public boolean unlock(final LockType lockType) throws Trouble {
		// TODO mock stub - to implement
		this.lockType = lockType;
		return true;
	}

	public boolean checkReservedLock() throws Trouble {
		// TODO mock stub - to implement
		return lockType == LockType.RESERVED;
	}

}
