package org.tmatesoft.sqljet2.internal.tree.nodes;

import org.tmatesoft.sqljet2.internal.tree.Node;

public interface Table extends Node {

	interface Trunk extends Table {

	}

	interface Leaf extends Table {

	}

}
