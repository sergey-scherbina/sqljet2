package org.tmatesoft.sqljet2.internal.btree.impl;

import java.util.Stack;

import org.tmatesoft.sqljet2.internal.btree.BTree;
import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class BTreeImpl implements BTree {
	
	private final Pager pager;
	
	private final int rootPageNumber;
	
	private final Stack<BTreePage> stack = new Stack<BTreePage>();
	
	private final BTreePage rootPage;
	
	public BTreeImpl(final Pager pager, final int rootPageNumber) throws Trouble {
		this.pager = pager;
		this.rootPageNumber = rootPageNumber;
		this.rootPage = new BTreePage(pager.readPage(rootPageNumber));
		stack.push(rootPage);
	}

	public Pager getPager() {
		return pager;
	}

	public int getRootPageNumber() {
		return rootPageNumber;
	}

	public void begin() throws Trouble {
		stack.clear();
		stack.push(rootPage);
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
