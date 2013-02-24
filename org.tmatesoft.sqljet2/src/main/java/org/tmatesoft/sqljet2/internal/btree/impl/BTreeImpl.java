package org.tmatesoft.sqljet2.internal.btree.impl;

import java.util.Stack;

import org.tmatesoft.sqljet2.internal.btree.BTree;
import org.tmatesoft.sqljet2.internal.btree.BTreePage;
import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public class BTreeImpl implements BTree {
	
	private final Pager pager;
	
	private final int rootPageNumber;
	
	private final BTreePage rootPage;

	private BTreePage leafPage = null;
	
	private int leafCellNumber = 0;
	
	public BTreeImpl(final Pager pager, final int rootPageNumber) throws Trouble {
		this.pager = pager;
		this.rootPageNumber = rootPageNumber;
		this.rootPage = new BTreePage(pager.readPage(rootPageNumber));
	}

	public Pager getPager() {
		return pager;
	}

	public int getRootPageNumber() {
		return rootPageNumber;
	}
	
	public BTreePage getRootPage() {
		return rootPage;
	}

	public void begin() throws Trouble {
		leafCellNumber = 0;
		leafPage = rootPage.getFirstLeafPage();
	}
	
	public void end() {
		// TODO Auto-generated method stub

	}

	public boolean next() throws Trouble {
		leafCellNumber++;
		if(leafCellNumber>leafPage.getCellsCount()) {
			leafCellNumber = 0;
			leafPage = leafPage.getNextLeafPage();
		}
		return leafPage!=null;
	}

	public boolean prev() {
		// TODO Auto-generated method stub
		return false;
	}

	public Pointer getCell() {
		return leafPage.getCell(leafCellNumber);
	}

}
