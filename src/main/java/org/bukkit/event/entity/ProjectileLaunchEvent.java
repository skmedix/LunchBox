package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ProjectileLaunchEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    public ProjectileLaunchEvent(Entity what) {
        super(what);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public Projectile getEntity() {
        return (Projectile) this.entity;
    }

    public HandlerList getHandlers() {
        return ProjectileLaunchEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return ProjectileLaunchEvent.handlers;
    }
}
