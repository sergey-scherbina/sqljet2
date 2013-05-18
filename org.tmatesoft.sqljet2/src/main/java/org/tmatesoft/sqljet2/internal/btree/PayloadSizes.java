package org.tmatesoft.sqljet2.internal.btree;

public class PayloadSizes {
	private final int usableSize;
	private final int minIndexLocal;
	private final int maxIndexLocal;
	private final int minLeafLocal;
	private final int maxLeafLocal;

	public PayloadSizes(final int usableSize) {
		this.usableSize = usableSize;
		minIndexLocal = minIndexLocal();
		maxIndexLocal = maxIndexLocal();
		minLeafLocal = minLeafLocal();
		maxLeafLocal = maxLeafLocal();
	}

	public int getLocal(final boolean isIndex, final long payloadSize) {
		return isIndex ? getIndexLocal(payloadSize)
				: getLeafLocal(payloadSize);
	}

	public int getIndexLocal(final long payloadSize) {
		return getLocal(payloadSize, maxIndexLocal, minIndexLocal);
	}

	public int getLeafLocal(final long payloadSize) {
		return getLocal(payloadSize, maxLeafLocal, minLeafLocal);
	}

	private int maxIndexLocal() {
		return (usableSize - 12) * 64 / 255 - 23;
	}

	private int minIndexLocal() {
		return (usableSize - 12) * 32 / 255 - 23;
	}

	private int maxLeafLocal() {
		return usableSize - 35;
	}

	private int minLeafLocal() {
		return (usableSize - 12) * 32 / 255 - 23;
	}

	private int getLocal(final long payloadSize, final int maxLocal,
			final int minLocal) {
		if (payloadSize <= maxLocal) {
			return (int) payloadSize;
		} else {
			long surplus = minLocal + (payloadSize - minLocal)
					% (usableSize - 4);
			if (surplus <= maxLocal) {
				return (int) surplus;
			} else {
				return minLocal;
			}
		}
	}
}