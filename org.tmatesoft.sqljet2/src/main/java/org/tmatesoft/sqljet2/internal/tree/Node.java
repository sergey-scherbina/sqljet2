package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.pager.Page;

public interface Node extends Page {

	NodeType getNodeType();

}
