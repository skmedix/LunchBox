package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.NumberConversions;

public class VehicleDamageEvent extends VehicleEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Entity attacker;
    private double damage;
    private boolean cancelled;

    /** @deprecated */
    @Deprecated
    public VehicleDamageEvent(Vehicle vehicle, Entity attacker, int damage) {
        this(vehicle, attacker, (double) damage);
    }

    public VehicleDamageEvent(Vehicle vehicle, Entity attacker, double damage) {
        super(vehicle);
        this.attacker = attacker;
        this.damage = damage;
    }

    public Entity getAttacker() {
        return this.attacker;
    }

    public double getDamage() {
        return this.damage;
    }

    /** @deprecated */
    @Deprecated
    public int getDamage() {
        return NumberConversions.ceil(this.getDamage());
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    /** @deprecated */
    @Deprecated
    public void setDamage(int damage) {
        this.setDamage((double) damage);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return VehicleDamageEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return VehicleDamageEvent.handlers;
    }
}
