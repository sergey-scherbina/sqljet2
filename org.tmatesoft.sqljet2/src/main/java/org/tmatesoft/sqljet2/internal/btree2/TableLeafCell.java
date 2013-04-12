package org.tmatesoft.sqljet2.internal.btree2;

import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.VarInt;

/**
 * <pre>
 * A varint which is the total number of bytes of payload, including any overflow.
 * 
 * A varint which is the integer key, a.k.a. "rowid".
 * 
 * The initial portion of the payload that does not spill to overflow pages.
 * 
 * A 4-byte big-endian integer page number for the first page of the overflow page list - 
 * 	omitted if all payload fits on the b-tree page.
 * </pre>
 */
public class TableLeafCell {

	private TableLeafCell() {

	}

	public static long getPayloadBytesCount(final Memory m, final int a) {
		return VarInt.getValue(m, a);
	}

	private static int getRowIdOffset(final Memory m, final int a) {
		return VarInt.getBytesCount(getPayloadBytesCount(m, a));
	}

	public static long getRowId(final Memory m, final int a) {
		return VarInt.getValue(m, a + getRowIdOffset(m, a));
	}

	public static long getRowId(final Memory m, final int a,
			final long payloadBytesCount) {
		return VarInt.getValue(m, a + VarInt.getBytesCount(payloadBytesCount));
	}

	public static int getPayloadOffset(final Memory m, final int a) {
		final int rowIdOffset = a + getRowIdOffset(m, a);
		return rowIdOffset
				+ VarInt.getBytesCount(VarInt.getValue(m, rowIdOffset));
	}

	/**
	 * The amount of payload that spills onto overflow pages also depends on the
	 * page type. For the following computations, let U be the usable size of a
	 * database page, the total page size less the reserved space at the end of
	 * each page. And let P be the payload size.
	 * 
	 * Table B-Tree Leaf Cell:
	 * 
	 * If the payload size P is less than or equal to U-35 then the entire
	 * payload is stored on the b-tree leaf page. Let M be ((U-12)*32/255)-23.
	 * If P is greater than U-35 then the number of byte stored on the b-tree
	 * leaf page is the smaller of M+((P-M)%(U-4)) and U-35. Note that number of
	 * bytes stored on the leaf page is never less than M.
	 */
	public static long getPayloadInternalPartBytesCount(final long payloadSize,
			final int usablePageSize) {
		final int u_35 = usablePageSize - 35;
		if (payloadSize <= u_35)
			return payloadSize;
		final int m = ((usablePageSize - 12) * 32 / 255) - 23;
		final long x = m + ((payloadSize - m) % (usablePageSize - 4));
		if (x < u_35)
			return x;
		return u_35;
	}

	public static boolean isPayloadOverflown(final Memory m, final int a,
			final int usablePageSize) {
		final long payload = getPayloadBytesCount(m, a);
		return payload > getPayloadInternalPartBytesCount(payload,
				usablePageSize);
	}

	public static int getOverflowPageNumber(final Memory m, final int a,
			final int usablePageSize) {
		final long payload = getPayloadBytesCount(m, a);
		final long internal = getPayloadInternalPartBytesCount(payload,
				usablePageSize);
		if (payload > internal) {
			return m.getInt(a + getPayloadOffset(m, a));
		}
		return 0;
	}
}
