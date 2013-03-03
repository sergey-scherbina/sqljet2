package org.tmatesoft.sqljet2.internal.pager.impl;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.PagerCache;

public class HeapPagerCache implements PagerCache {

	private final Map<Integer, SoftReference<Page>> heap = new ConcurrentHashMap<Integer, SoftReference<Page>>();

	public Page getPage(final int pageNumber) {
		final Integer pn = pageNumber;
		final SoftReference<Page> ref = heap.get(pn);
		if (ref != null) {
			final Page page = ref.get();
			if (page == null) {
				heap.remove(pn);
			}
			return page;
		}
		return null;
	}

	public void putPage(final Page page) {
		if (page != null) {
			heap.put(page.getPageNumber(), new SoftReference<Page>(page));
		}
	}
}
