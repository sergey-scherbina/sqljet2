package org.tmatesoft.sqljet2.test;

import org.junit.Test;
import org.tmatesoft.sqljet2.internal.btree.BTreeRecord;
import org.tmatesoft.sqljet2.internal.btree.BTreeTable;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.pager.impl.FilePager;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.StructDef;
import org.tmatesoft.sqljet2.internal.system.StructDef.SignedByte;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import static org.junit.Assert.*;

public class TestDb {

	private interface TestStruct {
		StructDef def = new StructDef();

		SignedByte test0 = def.signedByte();
		SignedByte test1 = def.signedByte();
		SignedByte test2 = def.signedByte();
	}

	@Test
	public void testStruct() {
		assertEquals(2, TestStruct.test2.getOffset());
	}

	private final static String TEST_DB = "src/test/resources/db/testdb.sqlite";

	@Test
	public void testForward() throws Trouble {
		final Pager pager = new FilePager();
		pager.open(TEST_DB, OpenPermission.READONLY);
		try {
			final BTreeTable table = new BTreeTable(pager, 1);
			table.begin();
			do {
				final BTreeRecord r = new BTreeRecord(
						table.getNotOverflowData());
				for (int i = 0; i < r.getColumnsCount(); i++) {
					System.out.println(r.getValue(i));
				}
				System.out.println();
			} while (table.next());
		} finally {
			pager.close();
		}
	}

	@Test
	public void testBackward() throws Trouble {
		final Pager pager = new FilePager();
		pager.open(TEST_DB, OpenPermission.READONLY);
		try {
			final BTreeTable table = new BTreeTable(pager, 1);
			table.end();
			do {
				final BTreeRecord r = new BTreeRecord(
						table.getNotOverflowData());
				for (int i = 0; i < r.getColumnsCount(); i++) {
					System.out.println(r.getValue(i));
				}
				System.out.println();
			} while (table.prev());
		} finally {
			pager.close();
		}
	}

}
