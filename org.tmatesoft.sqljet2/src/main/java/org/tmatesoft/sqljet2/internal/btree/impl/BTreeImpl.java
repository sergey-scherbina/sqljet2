package org.tmatesoft.sqljet2.internal.btree.impl;

import org.tmatesoft.sqljet2.internal.btree.BTree;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Pointer;

public class BTreeImpl implements BTree {
	
	private final Pager pager;
	
	private final int rootPageNumber;
	
	public BTreeImpl(final Pager pager, final int rootPageNumber) {
		this.pager = pager;
		this.rootPageNumber = rootPageNumber;
	}

	public Pager getPager() {
		return pager;
	}

	public int getRootPageNumber() {
		return rootPageNumber;
	}

	public void begin() {
		// TODO Auto-generated method stub

	}

	public void end() {
		// TODO Auto-generated method stub

	}

	public boolean next() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean prev() {
		// TODO Auto-generated method stub
		return false;
	}

	public Pointer getCell() {
		// TODO Auto-generated method stub
		return null;
	}

}
