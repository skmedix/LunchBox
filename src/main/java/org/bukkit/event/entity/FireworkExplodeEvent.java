package org.bukkit.event.entity;

import org.bukkit.entity.Firework;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class FireworkExplodeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;

    public FireworkExplodeEvent(Firework what) {
        super(what);
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Firework getEntity() {
        return (Firework) super.getEntity();
    }

    public HandlerList getHandlers() {
        return FireworkExplodeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return FireworkExplodeEvent.handlers;
    }
}
