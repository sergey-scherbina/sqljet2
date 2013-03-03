package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.VarInt;

public class BTreeTable extends BTree {

	private Pointer cell;

	private long allDataSize;
	private long rowId;
	private int notOverflowDataSize;
	private int notOverflowDataOffset;
	private boolean hasOverflow;
	private int overflowPageNumber;

	public BTreeTable(final Pager pager, final int rootPageNumber)
			throws Trouble {
		super(pager, rootPageNumber);
		assert (getRootPage().getHeader().isTablePage());
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
	public void end() throws Trouble {
		cell = null;
		super.end();
	}

	@Override
	public boolean prev() throws Trouble {
		cell = null;
		return super.prev();
	}

	@Override
	public Pointer getCell() {
		if (cell == null) {
			cell = super.getCell();
			parseCell();
		}
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
		if (cell == null)
			return;
		allDataSize = VarInt.getValue(cell, 0);
		byte rowIdOffset = VarInt.getBytesCount(allDataSize);
		rowId = VarInt.getValue(cell, rowIdOffset);
		notOverflowDataOffset = rowIdOffset + VarInt.getBytesCount(rowId);
		notOverflowDataSize = computeNotOverflowDataSize();
		hasOverflow = allDataSize != notOverflowDataSize;
		overflowPageNumber = hasOverflow ? cell.getInt(notOverflowDataOffset
				+ notOverflowDataSize) : 0;
	}

	private int computeNotOverflowDataSize() {
		return (int) allDataSize;
	}

}
