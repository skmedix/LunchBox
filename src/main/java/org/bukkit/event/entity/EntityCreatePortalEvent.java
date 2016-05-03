package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.PortalType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityCreatePortalEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final List blocks;
    private boolean cancelled = false;
    private PortalType type;

    public EntityCreatePortalEvent(LivingEntity what, List blocks, PortalType type) {
        super(what);
        this.type = PortalType.CUSTOM;
        this.blocks = blocks;
        this.type = type;
    }

    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    public List getBlocks() {
        return this.blocks;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public PortalType getPortalType() {
        return this.type;
    }

    public HandlerList getHandlers() {
        return EntityCreatePortalEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityCreatePortalEvent.handlers;
    }
}
