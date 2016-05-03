package org.bukkit.event.vehicle;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;

public class VehicleBlockCollisionEvent extends VehicleCollisionEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Block block;

    public VehicleBlockCollisionEvent(Vehicle vehicle, Block block) {
        super(vehicle);
        this.block = block;
    }

    public Block getBlock() {
        return this.block;
    }

    public HandlerList getHandlers() {
        return VehicleBlockCollisionEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return VehicleBlockCollisionEvent.handlers;
    }
}
