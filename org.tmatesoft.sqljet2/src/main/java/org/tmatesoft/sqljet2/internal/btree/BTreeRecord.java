package org.tmatesoft.sqljet2.internal.btree;

import org.tmatesoft.sqljet2.internal.system.Trouble;

public interface BTreeRecord {

	int getColumnsCount();

	int getType(int column) throws Trouble;

	Object getValue(int column) throws Trouble;

}