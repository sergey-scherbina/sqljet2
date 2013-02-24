package org.tmatesoft.sqljet2.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.tmatesoft.sqljet2.internal.btree.BTree;
import org.tmatesoft.sqljet2.internal.btree.BTreeTable;
import org.tmatesoft.sqljet2.internal.btree.impl.BTreeImpl;
import org.tmatesoft.sqljet2.internal.btree.impl.BTreeRecordImpl;
import org.tmatesoft.sqljet2.internal.btree.impl.BTreeTableImpl;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.pager.impl.PagerCacheImpl;
import org.tmatesoft.sqljet2.internal.pager.impl.PagerImpl;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.impl.DefaultFileSystem;

public class TestDb {

	private final static String TEST_DB = "src/test/resources/db/testdb.sqlite";

	@Test
	public void testDb() throws Trouble {
		final Pager pager = new PagerImpl(new PagerCacheImpl(),
				new DefaultFileSystem());
		pager.open(TEST_DB, OpenPermission.READONLY);
		try {
			final BTreeTable table = new BTreeTableImpl(pager, 0);
			table.begin();
			do {
				final BTreeRecordImpl r = new BTreeRecordImpl(table.getCell());
				for (int i = 0; i < r.getColumnsCount(); i++) {
					System.out.println(r.getValue(i).toString());
				}
			} while (table.next());
		} finally {
			pager.close();
		}
	}

}
