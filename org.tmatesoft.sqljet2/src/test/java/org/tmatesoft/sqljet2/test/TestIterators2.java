package org.tmatesoft.sqljet2.test;

import org.junit.Test;
import org.tmatesoft.sqljet2.internal.btree2.BTree;
import org.tmatesoft.sqljet2.internal.btree.BTreeRecord;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.pager.impl.FilePager;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class TestIterators2 {

	private final static String TEST_DB = "src/test/resources/db/testdb.sqlite";
	private final static String TEST_DB2 = "src/test/resources/db/rep-cache.db";

	@Test
	public void testSchema() throws Trouble {
		final Pager pager = new FilePager();
		pager.open(TEST_DB, OpenPermission.READONLY);
		try {
			final BTree btree = new BTree(pager);
			for (final BTree.Entry r : btree.page(1)) {
				System.out.println(r.getRowId());
				for (int i = 0; i < r.fieldsCount(); i++) {
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
		pager.open(TEST_DB2, OpenPermission.READONLY);
		try {
			final BTree btree = new BTree(pager);
			for (final BTree.Entry s : btree.page(1)) {
				final String type = (String) s.getValue(0);
				final String name = (String) s.getValue(1);
				final Number page = (Number) s.getValue(3);
				final String def = (String) s.getValue(4);
				if ("table".equalsIgnoreCase(type)) {
					System.out.println();
					System.out.println(type);
					System.out.println(name);
					System.out.println(page);
					System.out.println(def);
					System.out.println();
					int n = 0;
					for (final BTree.Entry r : btree.page(page.intValue())) {
						System.out.println(++n);
						System.out.println(r.getRowId());
						for (int i = 0; i < r.fieldsCount(); i++) {
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

	private static final String MAGIC = "03f2ef7de3f349642b8c7bc6a40e533f7186e37c";

	@Test
	public void testData2() throws Trouble {
		final Pager pager = new FilePager();
		pager.open(TEST_DB2, OpenPermission.READONLY);
		try {
			final BTree btree = new BTree(pager);
			final BTree.Entry s = btree.page(1).iterator().next();
			final String type = (String) s.getValue(0);
			final Number page = (Number) s.getValue(3);
			if ("table".equalsIgnoreCase(type)) {
				int n = 0;
				for (final BTree.Entry r : btree.page(page.intValue())) {
					n++;
					if (MAGIC.equals(r.getValue(0)))
						break;
				}
				System.out.println(n);
			}
		} finally {
			pager.close();
		}
	}

}
