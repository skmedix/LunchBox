package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockPhysicsEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final int changed;
    private boolean cancel = false;

    /** @deprecated */
    @Deprecated
    public BlockPhysicsEvent(Block block, int changed) {
        super(block);
        this.changed = changed;
    }

    /** @deprecated */
    @Deprecated
    public int getChangedTypeId() {
        return this.changed;
    }

    public Material getChangedType() {
        return Material.getMaterial(this.changed);
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return BlockPhysicsEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockPhysicsEvent.handlers;
    }
}
