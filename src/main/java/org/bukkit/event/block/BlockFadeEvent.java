package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockFadeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final BlockState newState;

    public BlockFadeEvent(Block block, BlockState newState) {
        super(block);
        this.newState = newState;
        this.cancelled = false;
    }

    public BlockState getNewState() {
        return this.newState;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return BlockFadeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockFadeEvent.handlers;
    }
}
