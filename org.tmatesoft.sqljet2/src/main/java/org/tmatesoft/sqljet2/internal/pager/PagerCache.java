package org.tmatesoft.sqljet2.internal.pager;

public interface PagerCache {
	
	Page getPage(int pageNumber);
	
	void putPage(Page page);

}
