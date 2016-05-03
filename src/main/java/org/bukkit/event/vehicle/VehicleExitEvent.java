package org.bukkit.event.vehicle;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class VehicleExitEvent extends VehicleEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final LivingEntity exited;

    public VehicleExitEvent(Vehicle vehicle, LivingEntity exited) {
        super(vehicle);
        this.exited = exited;
    }

    public LivingEntity getExited() {
        return this.exited;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return VehicleExitEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return VehicleExitEvent.handlers;
    }
}
