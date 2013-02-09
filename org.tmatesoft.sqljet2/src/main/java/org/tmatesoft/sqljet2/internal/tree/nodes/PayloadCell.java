package org.tmatesoft.sqljet2.internal.tree.nodes;

import org.tmatesoft.sqljet2.internal.system.MemoryBlock;

public interface PayloadCell {

	VarInt getBytesCount();

	MemoryBlock getNotOverflowedPart();

	int getOverflowPageNumber();

}
