package org.tmatesoft.sqljet2.test;

import org.junit.Test;
import org.tmatesoft.sqljet2.internal.btree.BTreeRecord;
import org.tmatesoft.sqljet2.internal.btree2.BTreeIterator;
import org.tmatesoft.sqljet2.internal.btree2.BTreeIterator.LeafIterator;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.pager.impl.FilePager;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class TestIterators {

	private final static String TEST_DB = "src/test/resources/db/testdb.sqlite";

	@Test
	public void testSchema() throws Trouble {
		final Pager pager = new FilePager();
		pager.open(TEST_DB, OpenPermission.READONLY);
		try {
			for (final BTreeRecord r : new BTreeIterator(pager.readPage(1))) {
				for (int i = 0; i < r.getColumnsCount(); i++) {
					System.out.println(r.getValue(i));
				}
				System.out.println();
			}
		} finally {
			pager.close();
		}
	}
	
	@Test
	public void testData() throws Trouble {
		final Pager pager = new FilePager();
		pager.open(TEST_DB, OpenPermission.READONLY);
		try {
			for (final BTreeRecord s : new BTreeIterator(pager.readPage(1))) {
				final String type = (String) s.getValue(0);
				final String name = (String) s.getValue(1);
				final Number page = (Number) s.getValue(3);
				final String def = (String) s.getValue(4);
				if("table".equalsIgnoreCase(type)) {
					System.out.println();
					System.out.println(type);
					System.out.println(name);
					System.out.println(page);
					System.out.println(def);
					System.out.println();

					for (final BTreeRecord r : new BTreeIterator(pager.readPage(page.intValue()))) {
						for (int i = 0; i < r.getColumnsCount(); i++) {
							System.out.println(r.getValue(i));
						}
						System.out.println();
					}

				}
			}
		} finally {
			pager.close();
		}
	}


}
