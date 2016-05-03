package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityCombustEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private int duration;
    private boolean cancel;

    public EntityCombustEvent(Entity combustee, int duration) {
        super(combustee);
        this.duration = duration;
        this.cancel = false;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public HandlerList getHandlers() {
        return EntityCombustEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityCombustEvent.handlers;
    }
}
