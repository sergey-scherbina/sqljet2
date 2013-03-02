package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;

public interface BTree {

	Pager getPager();
	
	int getRootPageNumber();
	
	void begin() throws Trouble;

	void end() throws Trouble;
	
	boolean next() throws Trouble;
	
	boolean prev() throws Trouble;
	
	Pointer getCell();
	
	boolean isEmpty();

}
