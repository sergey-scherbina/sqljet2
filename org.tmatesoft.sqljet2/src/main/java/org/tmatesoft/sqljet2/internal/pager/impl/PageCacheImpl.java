package org.tmatesoft.sqljet2.internal.pager.impl;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.PagerCache;

public class PageCacheImpl implements PagerCache {

	private final ConcurrentHashMap<Integer, SoftReference<Page>> soft = new ConcurrentHashMap<Integer, SoftReference<Page>>();

	public Page getPage(final int pageNumber) {
		final Integer pn = pageNumber;
		final SoftReference<Page> ref = soft.get(pn);
		if (ref != null) {
			final Page page = ref.get();
			if (page == null) {
				soft.remove(pn);
			}
			return page;
		}
		return null;
	}

	public void putPage(final Page page) {
		if(page!=null) {
			soft.put(page.getPageNumber(), new SoftReference(page));
		}
	}
}
