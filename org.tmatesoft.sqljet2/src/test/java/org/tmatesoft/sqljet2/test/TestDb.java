package org.tmatesoft.sqljet2.test;

import org.junit.Test;
import org.tmatesoft.sqljet2.internal.btree.BTreeTable;
import org.tmatesoft.sqljet2.internal.btree.impl.BTreeRecordImpl;
import org.tmatesoft.sqljet2.internal.btree.impl.BTreeTableImpl;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.pager.impl.HeapPagerCache;
import org.tmatesoft.sqljet2.internal.pager.impl.FilePager;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.impl.DefaultFileSystem;

public class TestDb {

	private final static String TEST_DB = "src/test/resources/db/testdb.sqlite";

	@Test
	public void testForward() throws Trouble {
		final Pager pager = new FilePager(new HeapPagerCache(),
				new DefaultFileSystem());
		pager.open(TEST_DB, OpenPermission.READONLY);
		try {
			final BTreeTable table = new BTreeTableImpl(pager, 1);
			table.begin();
			do {
				final BTreeRecordImpl r = new BTreeRecordImpl(
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
		final Pager pager = new FilePager(new HeapPagerCache(),
				new DefaultFileSystem());
		pager.open(TEST_DB, OpenPermission.READONLY);
		try {
			final BTreeTable table = new BTreeTableImpl(pager, 1);
			table.end();
			do {
				final BTreeRecordImpl r = new BTreeRecordImpl(
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
