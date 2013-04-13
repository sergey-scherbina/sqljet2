package org.tmatesoft.sqljet2.internal.btree2;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.StructDef;
import org.tmatesoft.sqljet2.internal.system.StructDef.*;

/**
 * <pre>
 * Offset	Size	Description
 *
 * 0	16	 The header string: "SQLite format 3\000"
 * 16	2	 The database page size in bytes. Must be a power of two between 512 and 32768 inclusive, or the value 1 representing a page size of 65536.
 * 18	1	 File format write version. 1 for legacy; 2 for WAL.
 * 19	1	 File format read version. 1 for legacy; 2 for WAL.
 * 20	1	 Bytes of unused "reserved" space at the end of each page. Usually 0.
 * 21	1	 Maximum embedded payload fraction. Must be 64.
 * 22	1	 Minimum embedded payload fraction. Must be 32.
 * 23	1	 Leaf payload fraction. Must be 32.
 * 24	4	 File change counter.
 * 28	4	 Size of the database file in pages. The "in-header database size".
 * 32	4	 Page number of the first freelist trunk page.
 * 36	4	 Total number of freelist pages.
 * 40	4	 The schema cookie.
 * 44	4	 The schema format number. Supported schema formats are 1, 2, 3, and 4.
 * 48	4	 Default page cache size.
 * 52	4	 The page number of the largest root b-tree page when in auto-vacuum or incremental-vacuum modes, or zero otherwise.
 * 56	4	 The database text encoding. A value of 1 means UTF-8. A value of 2 means UTF-16le. A value of 3 means UTF-16be.
 * 60	4	 The "user version" as read and set by the user_version pragma.
 * 64	4	 True (non-zero) for incremental-vacuum mode. False (zero) otherwise.
 * 68	24	 Reserved for expansion. Must be zero.
 * 92	4	 The version-valid-for number.
 * 96	4	SQLITE_VERSION_NUMBER
 * </pre>
 */
public class FileHeader {

	public interface Def {

		String MAGIC = "SQLite format 3\000";
		int MAGIC_SIZE = 16;

		StructDef def = new StructDef();

		/** 0 16 The header string: "SQLite format 3\000" */
		Bytes magic = def.bytes(MAGIC_SIZE);

		/**
		 * 16 2 The database page size in bytes. Must be a power of two between
		 * 512 and 32768 inclusive, or the value 1 representing a page size of
		 * 65536.
		 */
		SignedShort pageSize = def.signedShort();

		/** 18 1 File format write version. 1 for legacy; 2 for WAL. */
		SignedByte writeVersion = def.signedByte();

		/** 19 1 File format read version. 1 for legacy; 2 for WAL. */
		SignedByte readVersion = def.signedByte();

		/**
		 * 20 1 Bytes of unused "reserved" space at the end of each page.
		 * Usually 0.
		 */
		UnsignedByte reserved = def.unsignedByte();

		/** 21 1 Maximum embedded payload fraction. Must be 64. */
		SignedByte payloadEmbedMax = def.signedByte();

		/** 22 1 Minimum embedded payload fraction. Must be 32. */
		SignedByte payloadEmbedMin = def.signedByte();

		/** 23 1 Leaf payload fraction. Must be 32. */
		SignedByte payloadLeaf = def.signedByte();

		/** 24 4 File change counter. */
		UnsignedInt changeCounter = def.unsignedInt();

		/**
		 * 28 4 Size of the database file in pages. The
		 * "in-header database size".
		 */
		SignedInt pagesCount = def.signedInt();

		/** 32 4 Page number of the first freelist trunk page. */
		SignedInt freelistFirstPage = def.signedInt();

		/** 36 4 Total number of freelist pages. */
		UnsignedInt freelistPagesCount = def.unsignedInt();

		/** 40 4 The schema cookie. */
		UnsignedInt schemaCookie = def.unsignedInt();

		/**
		 * 44 4 The schema format number. Supported schema formats are 1, 2, 3,
		 * and 4.
		 */
		SignedInt schemaFormat = def.signedInt();

		/** 48 4 Default page cache size. */
		SignedInt pageCacheSize = def.signedInt();

		/**
		 * 52 4 The page number of the largest root b-tree page when in
		 * auto-vacuum or incremental-vacuum modes, or zero otherwise.
		 */
		SignedInt vacuumLargestPage = def.signedInt();

		/**
		 * 56 4 The database text encoding. A value of 1 means UTF-8. A value of
		 * 2 means UTF-16le. A value of 3 means UTF-16be.
		 */
		SignedInt encoding = def.signedInt();

		/** 60 4 The "user version" as read and set by the user_version pragma. */
		UnsignedInt userVersion = def.unsignedInt();

		/**
		 * 64 4 True (non-zero) for incremental-vacuum mode. False (zero)
		 * otherwise.
		 */
		SignedInt incrementalVacuum = def.signedInt();

		/** 68 24 Reserved for expansion. Must be zero. */
		Bytes unused = def.bytes(24);

		/** 92 4 The version-valid-for number. */
		UnsignedInt versionValidFor = def.unsignedInt();

		/** 96 4 SQLITE_VERSION_NUMBER */
		UnsignedInt sqliteVersionNumber = def.unsignedInt();

	}

	private final Memory m;

	public FileHeader(final Memory m) {
		this.m = m;
	}

	private final static byte[] magicBytes = getMagicBytes();

	private static byte[] getMagicBytes() {
		return Def.MAGIC.getBytes(Charset.forName("US-ASCII"));
	}

	/** * 0 16 The header string: "SQLite format 3\000" */
	public static boolean isMagicValid(final Memory m) {
		return Arrays.equals(magicBytes, Def.magic.get(m));
	}

	/** * 0 16 The header string: "SQLite format 3\000" */
	public static void initMagic(final Memory m) {
		Def.magic.set(m, magicBytes);
	}

	/** * 0 16 The header string: "SQLite format 3\000" */
	public boolean isMagicValid() {
		return isMagicValid(m);
	}

	/** * 0 16 The header string: "SQLite format 3\000" */
	public void initMagic() {
		initMagic(m);
	}

	/**
	 * 16 2 The database page size in bytes. Must be a power of two between 512
	 * and 32768 inclusive, or the value 1 representing a page size of 65536.
	 */
	public static int getPageSize(final Memory m) {
		return Def.pageSize.get(m);
	}

	/**
	 * 16 2 The database page size in bytes. Must be a power of two between 512
	 * and 32768 inclusive, or the value 1 representing a page size of 65536.
	 */
	public int getPageSize() {
		return getPageSize(m);
	}

	/** 18 1 File format write version. 1 for legacy; 2 for WAL. */
	/** 19 1 File format read version. 1 for legacy; 2 for WAL. */
	/**
	 * 20 1 Bytes of unused "reserved" space at the end of each page. Usually 0.
	 */
	/** 21 1 Maximum embedded payload fraction. Must be 64. */
	/** 22 1 Minimum embedded payload fraction. Must be 32. */
	/** 23 1 Leaf payload fraction. Must be 32. */
	/** 24 4 File change counter. */
	/** 28 4 Size of the database file in pages. The "in-header database size". */
	/** 32 4 Page number of the first freelist trunk page. */
	/** 36 4 Total number of freelist pages. */
	/** 40 4 The schema cookie. */
	/**
	 * 44 4 The schema format number. Supported schema formats are 1, 2, 3, and
	 * 4.
	 */
	/** 48 4 Default page cache size. */
	/**
	 * 52 4 The page number of the largest root b-tree page when in auto-vacuum
	 * or incremental-vacuum modes, or zero otherwise.
	 */
	/**
	 * 56 4 The database text encoding. A value of 1 means UTF-8. A value of 2
	 * means UTF-16le. A value of 3 means UTF-16be.
	 */
	/** 60 4 The "user version" as read and set by the user_version pragma. */
	/**
	 * 64 4 True (non-zero) for incremental-vacuum mode. False (zero) otherwise.
	 */
	/** 68 24 Reserved for expansion. Must be zero. */
	/** 92 4 The version-valid-for number. */
	/** 96 4 SQLITE_VERSION_NUMBER */

}
