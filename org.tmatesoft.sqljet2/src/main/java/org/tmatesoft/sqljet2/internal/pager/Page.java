package org.tmatesoft.sqljet2.internal.pager;

import org.tmatesoft.sqljet2.internal.system.MemoryBlock;
import org.tmatesoft.sqljet2.internal.tree.TreePage;

public interface Page {

	Pager getPager();

	int getPageNumber();

	MemoryBlock getData();

	TreePage getTreePage();

	void setTreePage(TreePage treePage);

}
