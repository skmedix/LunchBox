package org.bukkit.event.entity;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class CreeperPowerEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final CreeperPowerEvent.PowerCause cause;
    private LightningStrike bolt;

    public CreeperPowerEvent(Creeper creeper, LightningStrike bolt, CreeperPowerEvent.PowerCause cause) {
        this(creeper, cause);
        this.bolt = bolt;
    }

    public CreeperPowerEvent(Creeper creeper, CreeperPowerEvent.PowerCause cause) {
        super(creeper);
        this.cause = cause;
    }

    public boolean isCancelled() {
        return this.canceled;
    }

    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }

    public Creeper getEntity() {
        return (Creeper) this.entity;
    }

    public LightningStrike getLightning() {
        return this.bolt;
    }

    public CreeperPowerEvent.PowerCause getCause() {
        return this.cause;
    }

    public HandlerList getHandlers() {
        return CreeperPowerEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return CreeperPowerEvent.handlers;
    }

    public static enum PowerCause {

        LIGHTNING, SET_ON, SET_OFF;
    }
}
