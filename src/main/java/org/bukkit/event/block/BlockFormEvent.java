package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockFormEvent extends BlockGrowEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public BlockFormEvent(Block block, BlockState newState) {
        super(block, newState);
    }

    public HandlerList getHandlers() {
        return BlockFormEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockFormEvent.handlers;
    }
}
