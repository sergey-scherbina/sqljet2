package org.tmatesoft.sqljet2.internal.tree;

public interface Table extends Node {

	interface Trunk extends Table {

	}

	interface Leaf extends Table {

	}

}
