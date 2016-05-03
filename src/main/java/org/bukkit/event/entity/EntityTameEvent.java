package org.bukkit.event.entity;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityTameEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final AnimalTamer owner;

    public EntityTameEvent(LivingEntity entity, AnimalTamer owner) {
        super(entity);
        this.owner = owner;
    }

    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public AnimalTamer getOwner() {
        return this.owner;
    }

    public HandlerList getHandlers() {
        return EntityTameEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityTameEvent.handlers;
    }
}
