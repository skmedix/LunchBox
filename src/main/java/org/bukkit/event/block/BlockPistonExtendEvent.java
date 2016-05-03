package org.bukkit.event.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;

public class BlockPistonExtendEvent extends BlockPistonEvent {

    private static final HandlerList handlers = new HandlerList();
    private final int length;
    private List blocks;

    /** @deprecated */
    @Deprecated
    public BlockPistonExtendEvent(Block block, int length, BlockFace direction) {
        super(block, direction);
        this.length = length;
    }

    public BlockPistonExtendEvent(Block block, List blocks, BlockFace direction) {
        super(block, direction);
        this.length = blocks.size();
        this.blocks = blocks;
    }

    /** @deprecated */
    @Deprecated
    public int getLength() {
        return this.length;
    }

    public List getBlocks() {
        if (this.blocks == null) {
            ArrayList tmp = new ArrayList();

            for (int i = 0; i < this.getLength(); ++i) {
                tmp.add(this.block.getRelative(this.getDirection(), i + 1));
            }

            this.blocks = Collections.unmodifiableList(tmp);
        }

        return this.blocks;
    }

    public HandlerList getHandlers() {
        return BlockPistonExtendEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockPistonExtendEvent.handlers;
    }
}
