package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.NumberConversions;

public class EntityRegainHealthEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private double amount;
    private final EntityRegainHealthEvent.RegainReason regainReason;

    /** @deprecated */
    @Deprecated
    public EntityRegainHealthEvent(Entity entity, int amount, EntityRegainHealthEvent.RegainReason regainReason) {
        this(entity, (double) amount, regainReason);
    }

    public EntityRegainHealthEvent(Entity entity, double amount, EntityRegainHealthEvent.RegainReason regainReason) {
        super(entity);
        this.amount = amount;
        this.regainReason = regainReason;
    }

    public double getAmount() {
        return this.amount;
    }

    /** @deprecated */
    @Deprecated
    public int getAmount() {
        return NumberConversions.ceil(this.getAmount());
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    /** @deprecated */
    @Deprecated
    public void setAmount(int amount) {
        this.setAmount((double) amount);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public EntityRegainHealthEvent.RegainReason getRegainReason() {
        return this.regainReason;
    }

    public HandlerList getHandlers() {
        return EntityRegainHealthEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityRegainHealthEvent.handlers;
    }

    public static enum RegainReason {

        REGEN, SATIATED, EATING, ENDER_CRYSTAL, MAGIC, MAGIC_REGEN, WITHER_SPAWN, WITHER, CUSTOM;
    }
}
