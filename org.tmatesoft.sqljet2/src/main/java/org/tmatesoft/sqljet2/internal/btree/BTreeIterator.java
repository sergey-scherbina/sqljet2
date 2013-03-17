package org.tmatesoft.sqljet2.internal.btree;

import static org.tmatesoft.sqljet2.internal.btree.BTreePageHeader.getCellsOffset;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.Trouble.Code;

public class BTreeIterator {

	private final Node rootNode;

	private Leaf leaf;

	public BTreeIterator(final Page rootPage) throws Trouble {
		this.rootNode = rootNode(rootPage);
	}

	public boolean begin() throws Trouble {
		leaf = rootNode.first();
		return leaf != null;
	}

	public boolean next() throws Trouble {
		if (leaf != null) {
			leaf = leaf.next();
		}
		return leaf != null;
	}

	public Pointer getCurrent() throws Trouble {
		if (leaf != null) {
			return leaf.getCell();
		} else {
			return null;
		}
	}

	private static Node rootNode(final Page page) {
		if (BTreePageHeader.isTrunkPage(page)) {
			return new Trunk(page);
		} else {
			return new Leaf(page);
		}
	}

	private static abstract class Node {

		protected final Page page;
		protected final Trunk parent;
		protected final int number;

		public Node(final Page page, final Trunk parent, final int number) {
			this.page = page;
			this.parent = parent;
			this.number = number;
		}

		public Node(final Page page) {
			this(page, null, 0);
		}

		public abstract Leaf first() throws Trouble;

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

	private static class Trunk extends Node {

		public Trunk(final Page page, final Trunk parent, final int number) {
			super(page, parent, number);
		}

		public Trunk(final Page page) {
			super(page);
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
			final Page page = childPage(number);
			if (BTreePageHeader.isTrunkPage(page)) {
				return new Trunk(page, this, number);
			} else {
				return new Leaf(page, this, number);
			}
		}

		public Leaf first() throws Trouble {
			return next(0);
		}

		public Leaf next(int number) throws Trouble {
			return childNode(number).first();
		}

	}

	private static class Leaf extends Node {

		private int position;

		public Leaf(final Page page, final Trunk parent, final int number) {
			super(page, parent, number);
		}

		public Leaf(final Page page) {
			super(page);
		}

		public Leaf first() {
			position = 0;
			return this;
		}

		public Leaf next() throws Trouble {
			if (++position > getCellsCount()) {
				if (parent != null) {
					return parent.next(number);
				}
				return null;
			}
			return this;
		}

		public Pointer getCell() {
			return getCell(position);
		}

	}

}
