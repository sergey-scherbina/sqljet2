package org.tmatesoft.sqljet2.internal.btree;

import static org.tmatesoft.sqljet2.internal.btree.BTreePageHeader.getCellsOffset;

import org.tmatesoft.sqljet2.internal.pager.Page;
import org.tmatesoft.sqljet2.internal.pager.Pager;
import org.tmatesoft.sqljet2.internal.system.Memory;
import org.tmatesoft.sqljet2.internal.system.Pointer;
import org.tmatesoft.sqljet2.internal.system.Trouble;
import org.tmatesoft.sqljet2.internal.system.Trouble.Code;

public class BTreeModel {

	public static abstract class Node {

		public static Node root(final Page page) {
			if (BTreePageHeader.isTrunkPage(page)) {
				return new Trunk(page);
			} else {
				return new Leaf(page);
			}
		}

		protected final Page page;

		public Node(final Page page) {
			this.page = page;
		}

		public abstract boolean isTrunk();

		public boolean isRoot() {
			return !isChild();
		}

		public boolean isChild() {
			return false;
		}

		public boolean isLeaf() {
			return !isTrunk();
		}

		public Trunk asTrunk() throws Trouble {
			if (isTrunk() && this instanceof Trunk)
				return (Trunk) this;
			throw new Trouble(Code.ERROR);
		}

		public Leaf asLeaf() throws Trouble {
			if (isLeaf() && this instanceof Leaf)
				return (Leaf) this;
			throw new Trouble(Code.ERROR);
		}

		public ChildNode asChild() throws Trouble {
			if (isChild() && this instanceof ChildNode) {
				return (ChildNode) this;
			}
			throw new Trouble(Code.ERROR);
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

		private ChildNode childNode(final int number) throws Trouble {
			final Page page = childPage(number);
			if (BTreePageHeader.isTrunkPage(page)) {
				return new TrunkChild(page, this, number);
			} else {
				return new LeafChild(page, this, number);
			}
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

	public static abstract class ChildNode extends Node {

		protected final Trunk parent;
		protected final int number;

		public ChildNode(final Page page, final Trunk parent, final int number) {
			super(page);
			this.parent = parent;
			this.number = number;
		}

		@Override
		public boolean isChild() {
			return true;
		}

	}

	public static class TrunkChild extends ChildNode {

		public TrunkChild(Page page, Trunk parent, int number) {
			super(page, parent, number);
		}

		@Override
		public boolean isTrunk() {
			return true;
		}

	}

	public static class LeafChild extends ChildNode {

		public LeafChild(Page page, Trunk parent, int number) {
			super(page, parent, number);
		}

		@Override
		public boolean isTrunk() {
			return false;
		}

	}

}
