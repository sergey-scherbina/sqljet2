package org.tmatesoft.sqljet2.internal.tree;

import org.tmatesoft.sqljet2.internal.Page;

public interface Index {

	interface TrunkPage extends Page<TrunkPage> {

	}

	interface LeafPage extends Page<LeafPage> {

	}

}
