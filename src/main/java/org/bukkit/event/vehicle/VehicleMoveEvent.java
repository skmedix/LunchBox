package org.bukkit.event.vehicle;

import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;

public class VehicleMoveEvent extends VehicleEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Location from;
    private final Location to;

    public VehicleMoveEvent(Vehicle vehicle, Location from, Location to) {
        super(vehicle);
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return this.from;
    }

    public Location getTo() {
        return this.to;
    }

    public HandlerList getHandlers() {
        return VehicleMoveEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return VehicleMoveEvent.handlers;
    }
}
