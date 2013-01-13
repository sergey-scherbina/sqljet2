package org.tmatesoft.sqljet2.internal.impl;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.tmatesoft.sqljet2.internal.FileStream;
import org.tmatesoft.sqljet2.internal.Memory;
import org.tmatesoft.sqljet2.internal.MemoryBlock;
import org.tmatesoft.sqljet2.internal.Pointer;

public class NormalFile implements FileStream {

	public interface Streamable {

		int read(int address, RandomAccessFile stream, int position, int count)
				throws IOException;

		void write(int address, RandomAccessFile stream, int position, int count)
				throws IOException;
	}

	private final FileType fileType;
	private final RandomAccessFile stream;

	public NormalFile(final FileType fileType, final RandomAccessFile stream) {
		this.fileType = fileType;
		this.stream = stream;
	}

	public FileType getFileType() {
		return fileType;
	}

	public void close() throws IOException {
		stream.close();
	}

	public int read(int position, Pointer pointer, int count)
			throws IOException {
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
	}

	public void write(int position, Pointer pointer, int count)
			throws IOException {
		if (pointer.getMemory() instanceof Streamable) {
			((Streamable) pointer.getMemory()).write(pointer.getAddress(),
					stream, position, count);
		} else {
			final byte[] b = new byte[count];
			pointer.getBytes(b);
			stream.seek(position);
			stream.write(b);
		}
	}

	public void sync(final boolean full) throws IOException {
		stream.getChannel().force(full);
	}

	public void truncate(final int size) throws IOException {
		stream.setLength(size);
	}

	public long getSize() throws IOException {
		return stream.length();
	}

	public LockType getLockType() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean lock(LockType lockType) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean unlock(LockType lockType) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkReservedLock() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public int getSectorSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
