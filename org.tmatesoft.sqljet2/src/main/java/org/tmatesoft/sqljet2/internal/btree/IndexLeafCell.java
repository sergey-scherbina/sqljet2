package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.VarInt;

/**
 * <h1>Index B-Tree Leaf Cell:</h1>
 * <ul>
 * <li>A varint which is the total number of bytes of key payload, including any
 * overflow</li>
 * <li>The initial portion of the payload that does not spill to overflow pages.
 * </li>
 * <li>A 4-byte big-endian integer page number for the first page of the
 * overflow page list - omitted if all payload fits on the b-tree page.</li>
 * </ul>
 */
public class IndexLeafCell {
	
	private IndexLeafCell() {
	}

	public static long getPayloadBytesCount(final Memory m, final int a) {
		return VarInt.getValue(m, a);
	}

	public static int getPayloadOffset(final Memory m, final int a) {
		return VarInt.getBytesCount(VarInt.getValue(m, a));
	}

	/**
	 * <p>
	 * The amount of payload that spills onto overflow pages also depends on the
	 * page type. For the following computations, let U be the usable size of a
	 * database page, the total page size less the reserved space at the end of
	 * each page. And let P be the payload size.
	 * </p>
	 * 
	 * <h1>Index B-Tree Leaf Or Interior Cell:</h1>
	 * 
	 * <p>
	 * Let X be ((U-12)*64/255)-23). If the payload size P is less than or equal
	 * to X then the entire payload is stored on the b-tree page. Let M be
	 * ((U-12)*32/255)-23. If P is greater than X then the number of byte stored
	 * on the b-tree page is the smaller of M+((P-M)%(U-4)) and X. Note that
	 * number of bytes stored on the index page is never less than M.
	 * </p>
	 */
	public static long getPayloadInternalPartBytesCount(final long payloadSize,
			final int usablePageSize) {
		final int x = ((usablePageSize - 12) * 64 / 255) - 23;
		if (payloadSize <= x)
			return payloadSize;
		final int m = ((usablePageSize - 12) * 32 / 255) - 23;
		final long y = m + ((payloadSize - m) % (usablePageSize - 4));
		if (x < y)
			return x;
		return y;
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
