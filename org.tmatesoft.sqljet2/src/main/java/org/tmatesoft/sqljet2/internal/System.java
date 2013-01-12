package org.tmatesoft.sqljet2.internal;

import java.nio.ByteBuffer;

public interface System {

	FileStream memoryFile();

	FileSystem getFileSystem(String name);

	void registerFileSystem(FileSystem fs, boolean isDefault);

	void unregisterFileSystem(FileSystem fs);

	MemoryBlock allocate(int size);

	MemoryBlock allocateDirect(int size);

	MemoryBlock wrap(byte[] array);

	MemoryBlock wrap(ByteBuffer buffer);

	byte[] randomness(final int numBytes);

	long sleep(final long microseconds);

	long currentTime();

}
