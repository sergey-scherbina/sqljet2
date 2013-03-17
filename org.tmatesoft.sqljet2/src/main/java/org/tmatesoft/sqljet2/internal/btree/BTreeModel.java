package org.tmatesoft.sqljet2.internal.btree;

import static org.tmatesoft.sqljet2.internal.btree.BTreePageHeader.getCellsOffset;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.Trouble.Code;

public class BTreeModel {

	private static Node node(final Page page) {
		if (BTreePageHeader.isTrunkPage(page)) {
			return new Trunk(page);
		} else {
			return new Leaf(page);
		}
	}

	private static abstract class Node {
		protected final Page page;

		public Node(final Page page) {
			this.page = page;
		}

		public abstract boolean isTrunk();

		public boolean isLeaf() {
			return !isTrunk();
		}

		public Trunk asTrunk() {
			if (isTrunk() && this instanceof Trunk)
				return (Trunk) this;
			return null;
		}

		public Leaf asLeaf() {
			if (isLeaf() && this instanceof Leaf)
				return (Leaf) this;
			return null;
		}

		protected Memory getData() {
			return page.getData();
		}

		public int getCellsCount() {
			return BTreePageHeader.getCellsCount(page);
		}

		protected int getCellOffset(int cellNumber) {
			final int offset = getCellsOffset(page) + cellNumber * 2;
			return getData().getUnsignedShort(offset);
		}

		protected Pointer getCell(int cellNumber) {
			return getData().getPointer(getCellOffset(cellNumber));
		}

	}

	public static class Trunk extends Node {

		public Trunk(final Page page) {
			super(page);
		}

		@Override
		public boolean isTrunk() {
			return true;
		}

		private Pager getPager() {
			return page.getPager();
		}

		private int childPageNumber(final int childNumber) throws Trouble {
			if (childNumber < 0) {
				throw new Trouble(Code.ERROR);
			} else if (childNumber <= getCellsCount()) {
				return getData().getInt(getCellOffset(childNumber));
			} else {
				return BTreePageHeader.getRightMostChildPageNumber(page);
			}
		}

		private Page childPage(final int number) throws Trouble {
			return getPager().readPage(childPageNumber(number));
		}

		private Node childNode(final int number) throws Trouble {
			return node(childPage(number));
		}

	}

	public static class Leaf extends Node {

		public Leaf(final Page page) {
			super(page);
		}

		@Override
		public boolean isTrunk() {
			return false;
		}

	}

}
