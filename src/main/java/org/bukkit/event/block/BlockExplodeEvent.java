package org.bukkit.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockExplodeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final List blocks;
    private float yield;

    public BlockExplodeEvent(Block what, List blocks, float yield) {
        super(what);
        this.blocks = blocks;
        this.yield = yield;
        this.cancel = false;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public List blockList() {
        return this.blocks;
    }

    public float getYield() {
        return this.yield;
    }

    public void setYield(float yield) {
        this.yield = yield;
    }

    public HandlerList getHandlers() {
        return BlockExplodeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockExplodeEvent.handlers;
    }
}
