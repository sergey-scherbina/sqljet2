package org.tmatesoft.sqljet2.test.memory;

import static org.junit.Assert.*;

import org.junit.Test;
import org.tmatesoft.sqljet2.internal.memory.ArrayMemory;
import org.tmatesoft.sqljet2.internal.memory.BufferMemory;
import org.tmatesoft.sqljet2.memory.Memory;
import org.tmatesoft.sqljet2.memory.Pointer;

public class MemoryTest {

	private void testMemory(final Memory m) {
		final Pointer p = m.getPointer(0);
		p.putUnsignedInt(Memory.MAX_UNSIGNED_INT);
		assertEquals(Memory.MAX_UNSIGNED_INT, p.getUnsignedInt());
		assertEquals(Memory.MAX_UNSIGNED_SHORT, p.getUnsignedShort());
		assertEquals(Memory.MAX_UNSIGNED_BYTE, p.getUnsignedByte());
		p.move(p.SIZE_SHORT);
		assertEquals(Memory.MAX_UNSIGNED_SHORT, p.getUnsignedShort());
		assertEquals(Memory.MAX_UNSIGNED_BYTE, p.getUnsignedByte());
		p.move(p.SIZE_BYTE);
		assertEquals(Memory.MAX_UNSIGNED_BYTE, p.getUnsignedByte());
		p.move(p.SIZE_BYTE);
		assertEquals(0, p.getUnsignedByte());
	}

	@Test
	public void testArrayMemory() {
		final ArrayMemory m = new ArrayMemory(1024);
		testMemory(m);
	}

	@Test
	public void testBufferMemory() {
		final BufferMemory m = new BufferMemory(1024);
		testMemory(m);
	}

	@Test
	public void testDirectBufferMemory() {
		final BufferMemory m = new BufferMemory(1024, true);
		testMemory(m);
	}

}
