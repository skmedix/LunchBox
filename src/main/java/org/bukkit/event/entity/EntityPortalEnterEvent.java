package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class EntityPortalEnterEvent extends EntityEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Location location;

    public EntityPortalEnterEvent(Entity entity, Location location) {
        super(entity);
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public HandlerList getHandlers() {
        return EntityPortalEnterEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityPortalEnterEvent.handlers;
    }
}
