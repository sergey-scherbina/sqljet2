package org.tmatesoft.sqljet2.internal.system;

public interface FileStream {

	int SECTOR_SIZE = 512;

	/* First byte past the 1GB boundary */
	long PENDING_BYTE = 0x40000000;
	long RESERVED_BYTE = (PENDING_BYTE + 1);
	long SHARED_FIRST = (PENDING_BYTE + 2);
	long SHARED_SIZE = 510;

	void close() throws Trouble;

	int read(int position, Pointer pointer, int count) throws Trouble;

	void write(int position, Pointer pointer, int count) throws Trouble;

	void sync(boolean full) throws Trouble;

	void truncate(int size) throws Trouble;

	long getSize() throws Trouble;

	LockType getLockType();

	boolean lock(LockType lockType) throws Trouble;

	boolean unlock(final LockType lockType) throws Trouble;

	boolean checkReservedLock() throws Trouble;

	int getSectorSize();

	enum LockType {

		/**
		 * Not locked
		 */
		NONE,

		/**
		 * Any number of processes may hold a SHARED lock simultaneously.
		 */
		SHARED,

		/**
		 * A single process may hold a RESERVED lock on a file at any time.
		 * Other processes may hold and obtain new SHARED locks.
		 */
		RESERVED,

		/**
		 * A single process may hold a PENDING lock on a file at any one time.
		 * Existing SHARED locks may persist, but no new SHARED locks may be
		 * obtained by other processes.
		 */
		PENDING,

		/**
		 * An EXCLUSIVE lock precludes all other locks.
		 */
		EXCLUSIVE
	}

}
