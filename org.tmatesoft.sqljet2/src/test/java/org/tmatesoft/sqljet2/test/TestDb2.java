package org.tmatesoft.sqljet2.test;

import org.junit.Test;
import org.tmatesoft.sqljet2.internal.btree.BTreeRecord;
import org.tmatesoft.sqljet2.internal.btree2.BTreeIterator;
import org.tmatesoft.sqljet2.internal.btree2.TableLeafCell;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.pager.impl.FilePager;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class TestDb2 {
	
	private final static String TEST_DB = "src/test/resources/db/testdb.sqlite";

	@Test
	public void testForward() throws Trouble {
		final Pager pager = new FilePager();
		pager.open(TEST_DB, OpenPermission.READONLY);
		try {
			final BTreeIterator t = new BTreeIterator.LeafIterator(pager.readPage(1));
			while(t.hasNext()){
				final Pointer p = t.next();
				int payloadOffset = TableLeafCell.getPayloadOffset(p.getMemory(), p.getAddress());
				final BTreeRecord r = new BTreeRecord(p.getMemory().getPointer(payloadOffset));
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
