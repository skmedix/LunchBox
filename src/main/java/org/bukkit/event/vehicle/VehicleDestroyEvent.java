package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class VehicleDestroyEvent extends VehicleEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Entity attacker;
    private boolean cancelled;

    public VehicleDestroyEvent(Vehicle vehicle, Entity attacker) {
        super(vehicle);
        this.attacker = attacker;
    }

    public Entity getAttacker() {
        return this.attacker;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return VehicleDestroyEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return VehicleDestroyEvent.handlers;
    }
}
