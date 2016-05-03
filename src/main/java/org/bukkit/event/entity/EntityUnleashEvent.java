package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class EntityUnleashEvent extends EntityEvent {

    private static final HandlerList handlers = new HandlerList();
    private final EntityUnleashEvent.UnleashReason reason;

    public EntityUnleashEvent(Entity entity, EntityUnleashEvent.UnleashReason reason) {
        super(entity);
        this.reason = reason;
    }

    public EntityUnleashEvent.UnleashReason getReason() {
        return this.reason;
    }

    public HandlerList getHandlers() {
        return EntityUnleashEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityUnleashEvent.handlers;
    }

    public static enum UnleashReason {

        HOLDER_GONE, PLAYER_UNLEASH, DISTANCE, UNKNOWN;
    }
}
