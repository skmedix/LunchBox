package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

public class BlockRedstoneEvent extends BlockEvent {

    private static final HandlerList handlers = new HandlerList();
    private final int oldCurrent;
    private int newCurrent;

    public BlockRedstoneEvent(Block block, int oldCurrent, int newCurrent) {
        super(block);
        this.oldCurrent = oldCurrent;
        this.newCurrent = newCurrent;
    }

    public int getOldCurrent() {
        return this.oldCurrent;
    }

    public int getNewCurrent() {
        return this.newCurrent;
    }

    public void setNewCurrent(int newCurrent) {
        this.newCurrent = newCurrent;
    }

    public HandlerList getHandlers() {
        return BlockRedstoneEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockRedstoneEvent.handlers;
    }
}
