package org.spigotmc.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityMountEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Entity mount;

    public EntityMountEvent(Entity what, Entity mount) {
        super(what);
        this.mount = mount;
    }

    public Entity getMount() {
        return this.mount;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return EntityMountEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityMountEvent.handlers;
    }
}
