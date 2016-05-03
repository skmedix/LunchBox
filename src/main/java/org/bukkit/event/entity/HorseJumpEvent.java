package org.bukkit.event.entity;

import org.bukkit.entity.Horse;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class HorseJumpEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private float power;

    public HorseJumpEvent(Horse horse, float power) {
        super(horse);
        this.power = power;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public Horse getEntity() {
        return (Horse) this.entity;
    }

    public float getPower() {
        return this.power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public HandlerList getHandlers() {
        return HorseJumpEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return HorseJumpEvent.handlers;
    }
}
