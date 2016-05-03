package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class EntityPortalExitEvent extends EntityTeleportEvent {

    private static final HandlerList handlers = new HandlerList();
    private Vector before;
    private Vector after;

    public EntityPortalExitEvent(Entity entity, Location from, Location to, Vector before, Vector after) {
        super(entity, from, to);
        this.before = before;
        this.after = after;
    }

    public Vector getBefore() {
        return this.before.clone();
    }

    public Vector getAfter() {
        return this.after.clone();
    }

    public void setAfter(Vector after) {
        this.after = after.clone();
    }

    public HandlerList getHandlers() {
        return EntityPortalExitEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityPortalExitEvent.handlers;
    }
}
