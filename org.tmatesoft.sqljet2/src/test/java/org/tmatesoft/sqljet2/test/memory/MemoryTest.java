package org.tmatesoft.sqljet2.test.memory;

import static org.junit.Assert.*;

import org.junit.Test;
import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.MemoryBlock;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.impl.ArrayMemory;
import org.tmatesoft.sqljet2.internal.system.impl.BufferMemory;

public class MemoryTest {

	private void testMemory(final MemoryBlock m) {
		final Pointer p = m.getBegin();
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
		final ArrayMemory m = new ArrayMemory(Memory.SIZE_LONG);
		testMemory(m);
	}

	@Test
	public void testBufferMemory() {
		final BufferMemory m = new BufferMemory(Memory.SIZE_LONG);
		testMemory(m);
	}

	@Test
	public void testDirectBufferMemory() {
		final BufferMemory m = new BufferMemory(Memory.SIZE_LONG, true);
		testMemory(m);
	}

}
