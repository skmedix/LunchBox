package org.bukkit.event.entity;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityChangeBlockEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Block block;
    private boolean cancel;
    private final Material to;
    private final byte data;

    /** @deprecated */
    @Deprecated
    public EntityChangeBlockEvent(LivingEntity what, Block block, Material to) {
        this(what, block, to, (byte) 0);
    }

    /** @deprecated */
    @Deprecated
    public EntityChangeBlockEvent(Entity what, Block block, Material to, byte data) {
        super(what);
        this.block = block;
        this.cancel = false;
        this.to = to;
        this.data = data;
    }

    public Block getBlock() {
        return this.block;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Material getTo() {
        return this.to;
    }

    /** @deprecated */
    @Deprecated
    public byte getData() {
        return this.data;
    }

    public HandlerList getHandlers() {
        return EntityChangeBlockEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityChangeBlockEvent.handlers;
    }
}
