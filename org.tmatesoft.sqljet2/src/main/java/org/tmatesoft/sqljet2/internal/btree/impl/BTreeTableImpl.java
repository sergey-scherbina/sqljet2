package org.tmatesoft.sqljet2.internal.btree.impl;

import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.VarInt;

public class BTreeTableImpl extends BTreeImpl implements BTreeTable {

	private Pointer cell;

	private long allDataSize;
	private long rowId;
	private int notOverflowDataSize;
	private int notOverflowDataOffset;
	private boolean hasOverflow;
	private int overflowPageNumber;

	public BTreeTableImpl(final Pager pager, final int rootPageNumber)
			throws Trouble {
		super(pager, rootPageNumber);
		assert (getRootPage().isTablePage());
	}

	@Override
	public void begin() throws Trouble {
		cell = null;
		super.begin();
	}

	@Override
	public boolean next() throws Trouble {
		cell = null;
		return super.next();
	}

	@Override
	public Pointer getCell() {
		if (cell != null)
			return cell;
		cell = super.getCell();
		parseCell();
		return cell;
	}

	public long getRowId() {
		return rowId;
	}

	public long getAllDataSize() {
		return allDataSize;
	}

	public long getNotOverflowDataSize() {
		return notOverflowDataSize;
	}

	public int getNotOverflowDataOffset() {
		return notOverflowDataOffset;
	}

	public Pointer getNotOverflowData() {
		return getCell().getPointer(notOverflowDataOffset);
	}

	public boolean isHasOverflow() {
		return hasOverflow;
	}

	public int getOverflowPageNumber() {
		return overflowPageNumber;
	}

	private void parseCell() {
		allDataSize = VarInt.varInt.getValue(getCell(), 0);
		byte rowIdOffset = VarInt.varInt.getBytesCount(allDataSize);
		rowId = VarInt.varInt.getValue(getCell(), rowIdOffset);
		notOverflowDataOffset = rowIdOffset
				+ VarInt.varInt.getBytesCount(rowId);
		notOverflowDataSize = computeNotOverflowDataSize();
		hasOverflow = allDataSize != notOverflowDataSize;
		overflowPageNumber = hasOverflow ? getCell().getInt(
				notOverflowDataOffset + notOverflowDataSize) : 0;
	}

	private int computeNotOverflowDataSize() {
		return (int) allDataSize;
	}

}
