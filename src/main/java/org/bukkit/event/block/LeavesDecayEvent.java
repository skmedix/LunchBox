package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class LeavesDecayEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;

    public LeavesDecayEvent(Block block) {
        super(block);
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return LeavesDecayEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return LeavesDecayEvent.handlers;
    }
}
