package org.tmatesoft.sqljet2.test;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import org.tmatesoft.sqljet2.internal.btree.BTree;
import org.tmatesoft.sqljet2.internal.btree.BTreeRecord;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.pager.impl.FilePager;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.Trouble;

import static org.tmatesoft.sqljet2.test.TestUtils.*;

public class TestIterators {

    private final static String TEST_DB = "src/test/resources/db/rep-cache.db";
    private final static String REP_CACHE = "rep_cache";

    private Pager pager() throws Trouble {
        final Pager pager = new FilePager();
        pager.open(TEST_DB, OpenPermission.READONLY);
        return pager;
    }

    private BTree btree() throws Trouble {
        return new BTree(pager());
    }

    private BTree.Cursor schema(final BTree btree) throws Trouble {
        return btree.cursor(1);
    }

    private BTree.Cursor table(final String table, final BTree btree)
            throws Trouble {
        for (final BTree.Entry e : schema(btree)) {
            final BTreeRecord r = e.getRecord();
            final String type = r.getString(0);
            final String name = r.getString(1);
            final int page = (int) r.getInteger(3);
            if ("table".equalsIgnoreCase(type) && table.equalsIgnoreCase(name)) {
                return btree.cursor(page);
            }
        }
        return null;
    }

    private BTree.Cursor rep_cache(final BTree btree) throws Trouble {
        return table(REP_CACHE, btree);
    }

    private void printAll(final Streams stream, final BTree.Cursor cursor)
            throws Trouble, IOException {
        for (final BTree.Entry e : cursor) {
            final BTreeRecord r = e.getRecord();
            final StringBuilder s = new StringBuilder();
            s.append(r.getString(0)).append(' ');
            s.append(r.getInteger(1)).append(' ');
            s.append(r.getInteger(2)).append(' ');
            s.append(r.getInteger(3)).append(' ');
            s.append(r.getInteger(4)).append(' ');
            s.append('\n');
            stream.print(s.toString());
        }
    }

    private void printHash(final Streams stream, final BTree.Cursor cursor)
            throws Trouble, IOException {
        for (final BTree.Entry e : cursor) {
            final BTreeRecord r = e.getRecord();
            stream.print(r.getString(0)).space().println();
        }
    }

    @Test
    public void testSelectHash() throws Exception {
        final BTree btree = btree();
        try {
            warm(new Testable() {
                public void test() throws Exception {
                    printHash(Streams.NULL, rep_cache(btree));
                }
            });
            final Testable t = new Testable() {
                public void test() throws Exception {
                    printHash(Streams.OUT, rep_cache(btree));
                }
            };
            t.test();
            t.test();
            t.test();
            benchmark("\nselect hash from rep_cache;\n\ntestSelectHash", t);
        } finally {
            btree.close();
        }
    }

    @Test
    public void testSelect() throws Exception {
        final BTree btree = btree();
        try {
            warm(new Testable() {
                public void test() throws Exception {
                    printAll(Streams.NULL, rep_cache(btree));
                }
            });
            final Testable t = new Testable() {
                public void test() throws Exception {
                    printAll(Streams.OUT, rep_cache(btree));
                }
            };
            t.test();
            benchmark("\nselect * from rep_cache;\n\ntestSelect", t);
        } finally {
            btree.close();
        }
    }

    private long max(final int field, final BTree.Cursor cursor) throws Trouble {
        long max = 0;
        for (final BTree.Entry e : cursor) {
            final long v = e.getRecord().getInteger(field);
            if (v > max)
                max = v;
        }
        return max;
    }

    @Test
    public void testMaxOffset() throws Exception {
        final BTree btree = btree();
        try {
            final Testable t = new Testable() {
                public void test() throws Exception {
                    max(2, rep_cache(btree));
                }
            };
            warm(t);
            benchmark("\n\nselect max(offset) from rep_cache;\ntestMaxOffset", t);
        } finally {
            btree.close();
        }
    }

    @Test
    public void testFastCount() throws Exception {
        final BTree btree = btree();
        try {
            final Testable t = new Testable() {
                public void test() throws Exception {
                    rep_cache(btree).count();
                }
            };
            warm(t);
            benchmark("\n\nselect count(*) from rep_cache;\ntestFastCount", t);
        } finally {
            btree.close();
        }
    }

    private long countIterate(final String s, final BTree.Cursor cursor)
            throws Trouble {
        long count = 0;
        for (final BTree.Entry e : cursor) {
            if (!s.equals(e.getRecord().getString(0))) {
                ++count;
            }
        }
        return count;
    }

    @Test
    public void testCountIterate() throws Exception {
        final BTree btree = btree();
        try {
            final Testable t = new Testable() {
                public void test() throws Exception {
                    countIterate("", rep_cache(btree));
                }
            };
            warm(t);
            benchmark("\n\nselect count(*) from rep_cache where hash!='';\ntestCountIterate", t);
        } finally {
            btree.close();
        }
    }

    private long countIterateBytes(final byte[] b, final BTree.Cursor cursor)
            throws Trouble {
        long count = 0;
        for (final BTree.Entry e : cursor) {
            final BTreeRecord r = e.getRecord();
            if (!Arrays.equals(b, r.getStringBytes(0))) {
                ++count;
            }
        }
        return count;
    }

    @Test
    public void testCountIterateBytes() throws Exception {
        final BTree btree = btree();
        try {
            final Testable t = new Testable() {
                public void test() throws Exception {
                    countIterateBytes(new byte[]{}, rep_cache(btree));
                }
            };
            warm(t);
            benchmark("\n\nselect count(*) from rep_cache where hash!='';\ntestCountIterateBytes", t);
        } finally {
            btree.close();
        }
    }

}