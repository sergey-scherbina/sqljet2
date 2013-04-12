package org.tmatesoft.sqljet2.internal.btree2;

import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.VarInt;

public class TableTrunkCell {

	private TableTrunkCell() {
	}

	public static int getLeftChild(final Memory m, final int a) {
		return m.getInt(a);
	}

	public static long getRowId(final Memory m, final int a) {
		return VarInt.getValue(m, a + Memory.SIZE_INT);
	}

}
