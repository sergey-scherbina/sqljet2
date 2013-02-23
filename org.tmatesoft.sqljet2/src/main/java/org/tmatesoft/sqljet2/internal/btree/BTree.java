package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Pointer;

public interface BTree {

	Pager getPager();
	
	int getRootPageNumber();
	
	void begin();

	void end();
	
	boolean next();
	
	boolean prev();
	
	Pointer getCell();

}
