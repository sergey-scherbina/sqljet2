package org.tmatesoft.sqljet2.test;

import org.junit.Test;
import org.tmatesoft.sqljet2.internal.btree.BTreeRecord;
import org.tmatesoft.sqljet2.internal.btree2.BTreeIterator.LeafIterator;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.pager.impl.FilePager;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class TestDb2 {

	private final static String TEST_DB = "src/test/resources/db/testdb.sqlite";

	@Test
	public void testIterator() throws Trouble {
		final Pager pager = new FilePager();
		pager.open(TEST_DB, OpenPermission.READONLY);
		try {
			final LeafIterator t = new LeafIterator(pager.readPage(1));
			while (t.hasNext()) {
				final BTreeRecord r = t.next();
				for (int i = 0; i < r.getColumnsCount(); i++) {
					System.out.println(r.getValue(i));
				}
				System.out.println();
			}
		} finally {
			pager.close();
		}
	}

}
