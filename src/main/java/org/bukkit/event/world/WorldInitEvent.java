package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;

public class WorldInitEvent extends WorldEvent {

    private static final HandlerList handlers = new HandlerList();

    public WorldInitEvent(World world) {
        super(world);
    }

    public HandlerList getHandlers() {
        return WorldInitEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return WorldInitEvent.handlers;
    }
}
