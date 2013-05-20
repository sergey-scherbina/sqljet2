package org.tmatesoft.sqljet2.test;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.tmatesoft.sqljet2.internal.btree.BTree;
import org.tmatesoft.sqljet2.internal.btree.BTreeRecord;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.pager.impl.FilePager;
import org.tmatesoft.sqljet2.internal.system.FileSystem.OpenPermission;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class TestIterators {

	private int WARM = 1000;

	private final static String TEST_DB = "src/test/resources/db/rep-cache.db";
	private final static String REP_CACHE = "rep_cache";

	private static final BufferedWriter out = openOut();

	private static BufferedWriter openOut() {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				java.io.FileDescriptor.out), Charset.forName("ASCII")), 512);
	}

	@AfterClass
	public static void tearDownClass() {
		closeOut();
	}

	@After
	public void tearDown() {
		flushOut();
	}

	private static void closeOut() {
		try {
			out.close();
		} catch (IOException e) {
		}
	}

	private void flushOut() {
		try {
			out.flush();
		} catch (IOException e) {
		}
	}

	private static String toString(final String s) {
		return (s == null) ? "null" : s;
	}

	private static String toString(final Object x) {
		return (x == null) ? "null" : x.toString();
	}

	private static String toString(final long x) {
		return Long.toString(x);
	}

	private static void print(final String s) {
		try {
			out.write(toString(s));
		} catch (IOException e) {
		}
	}

	private static void print(final Object x) {
		try {
			out.write(toString(x));
		} catch (IOException e) {
		}
	}

	private static void print(final long x) {
		try {
			out.write(toString(x));
		} catch (IOException e) {
		}
	}

	private static void println() {
		try {
			out.write('\n');
		} catch (IOException e) {
		}
	}

	private static void println(final String s) {
		print(s);
		println();
	}

	private static void println(final Object x) {
		print(x);
		println();
	}

	private static void println(final long x) {
		print(x);
		println();
	}

	private static String ns(final long ns) {
		return String.format("%f s", ((double) ns) / 1E9);
	}

	private static long start() {
		return System.nanoTime();
	}

	private static String time(final long start) {
		return ns(System.nanoTime() - start);
	}

	private static void end(final long start) {
		println("Time: " + time(start));
	}

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

	@Test
	public void testSchema() throws Trouble {
		final BTree btree = btree();
		try {
			for (final BTree.Entry e : schema(btree)) {
				final BTreeRecord r = e.getRecord();
				for (int i = 0; i < r.getColumnsCount(); i++) {
					println(r.getValue(i));
				}
				println();
			}
		} finally {
			btree.close();
		}
	}

	private List<BTree.Entry> select(final BTree.Cursor cursor) {
		final List<BTree.Entry> list = new LinkedList<BTree.Entry>();
		for (final BTree.Entry e : cursor) {
			list.add(e);
		}
		return list;
	}

	@Test
	public void testSelect() throws Trouble {
		println("select * from rep_cache;");
		final BTree btree = btree();
		try {
			for (int i = 0; i < WARM; i++) {
				select(rep_cache(btree));
			}
			final long start = start();
			final List<BTree.Entry> select = select(rep_cache(btree));
			final String time = time(start);
			for (final BTree.Entry e : select) {
				final BTreeRecord r = e.getRecord();
				for (int i = 0; i < r.getColumnsCount(); ++i) {
					print(r.getValue(i));
					print(" ");
				}
				println();
			}
			println(time);
		} finally {
			btree.close();
		}
	}

	private List<String> selectHash(final BTree.Cursor cursor) throws Trouble {
		final List<String> list = new LinkedList<String>();
		for (final BTree.Entry e : cursor) {
			list.add(e.getRecord().getString(0));
		}
		return list;
	}

	@Test
	public void testSelectHash() throws Trouble {
		println("select hash from rep_cache;");
		final BTree btree = btree();
		try {
			for (int i = 0; i < WARM; i++) {
				selectHash(rep_cache(btree));
			}
			final long start = start();
			final List<String> hashes = selectHash(rep_cache(btree));
			final String time = time(start);
			for (final String hash : hashes) {
				println(hash);
			}
			println(time);
		} finally {
			btree.close();
		}
	}

	private long countIterate(final String s, final BTree.Cursor cursor)
			throws Trouble {
		long count = 0;
		for (final BTree.Entry e : cursor) {
			if (!s.equals(e.getRecord().getString(0)))
				++count;
		}
		return count;
	}

	private final static String ABCDEF = "abcdef";

	@Test
	public void testCountIterate() throws Trouble {
		println("select count(*) from rep_cache where hash!='abcdef';");
		final BTree btree = btree();
		try {
			for (int i = 0; i < WARM; i++) {
				countIterate(ABCDEF, rep_cache(btree));
			}
			final long start = start();
			final long count = countIterate(ABCDEF, rep_cache(btree));
			end(start);
			println(count);
		} finally {
			btree.close();
		}
	}

	@Test
	public void testFastCount() throws Trouble {
		println("select count(*) from rep_cache;");
		final BTree btree = btree();
		try {
			for (int i = 0; i < WARM; i++) {
				rep_cache(btree).count();
			}
			final long start = start();
			final long count = rep_cache(btree).count();
			end(start);
			println(count);
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
	public void testMaxOffset() throws Trouble {
		println("select max(offset) from rep_cache;");
		final BTree btree = btree();
		try {
			for (int i = 0; i < WARM; i++) {
				max(2, rep_cache(btree));
			}
			final long start = start();
			final long max = max(2, rep_cache(btree));
			end(start);
			println(max);
		} finally {
			btree.close();
		}
	}

}
