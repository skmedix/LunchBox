package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class VehicleEnterEvent extends VehicleEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Entity entered;

    public VehicleEnterEvent(Vehicle vehicle, Entity entered) {
        super(vehicle);
        this.entered = entered;
    }

    public Entity getEntered() {
        return this.entered;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return VehicleEnterEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return VehicleEnterEvent.handlers;
    }
}
