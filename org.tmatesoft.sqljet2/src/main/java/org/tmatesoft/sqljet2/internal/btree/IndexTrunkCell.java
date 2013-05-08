package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.VarInt;

/**
 * <h1>Index B-Tree Interior Cell:</h1>
 * <ul>
 * <li>A 4-byte big-endianpage number which is the left child pointer.</li>
 * <li>A varint which is the total number of bytes of key payload, including any
 * overflow</li>
 * <li>The initial portion of the payload that does not spill to overflow pages.
 * </li>
 * <li>A 4-byte big-endian integer page number for the first page of the
 * overflow page list - omitted if all payload fits on the b-tree page.</li>
 * </ul>
 */
public class IndexTrunkCell {

	private IndexTrunkCell() {
	}

	public static int getLeftChild(final Memory m, final int a) {
		return m.getInt(a);
	}

	public static long getPayloadBytesCount(final Memory m, final int a) {
		return VarInt.getValue(m, a + Memory.SIZE_INT);
	}

	public static int getPayloadOffset(final Memory m, final int a) {
		return VarInt.getBytesCount(VarInt.getValue(m, a + Memory.SIZE_INT));
	}

	public static boolean isPayloadOverflown(final Memory m, final int a,
			final int usablePageSize) {
		final long payload = getPayloadBytesCount(m, a);
		return payload > IndexLeafCell.getPayloadInternalPartBytesCount(
				payload, usablePageSize);
	}

	public static int getOverflowPageNumber(final Memory m, final int a,
			final int usablePageSize) {
		final long payload = getPayloadBytesCount(m, a);
		final long internal = IndexLeafCell.getPayloadInternalPartBytesCount(
				payload, usablePageSize);
		if (payload > internal) {
			return m.getInt(a + getPayloadOffset(m, a));
		}
		return 0;
	}

}
