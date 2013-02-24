package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.system.Pointer;

public interface BTreeTable extends BTree {

	Pointer getCell();

	long getRowId();

	long getAllDataSize();

	long getNotOverflowDataSize();

	int getNotOverflowDataOffset();

	Pointer getNotOverflowData();

	boolean isHasOverflow();

	int getOverflowPageNumber();

}