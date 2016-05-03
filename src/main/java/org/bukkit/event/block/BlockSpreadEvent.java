package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;

public class BlockSpreadEvent extends BlockFormEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Block source;

    public BlockSpreadEvent(Block block, Block source, BlockState newState) {
        super(block, newState);
        this.source = source;
    }

    public Block getSource() {
        return this.source;
    }

    public HandlerList getHandlers() {
        return BlockSpreadEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockSpreadEvent.handlers;
    }
}
