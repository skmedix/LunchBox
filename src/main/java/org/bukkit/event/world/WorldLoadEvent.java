package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;

public class WorldLoadEvent extends WorldEvent {

    private static final HandlerList handlers = new HandlerList();

    public WorldLoadEvent(World world) {
        super(world);
    }

    public HandlerList getHandlers() {
        return WorldLoadEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return WorldLoadEvent.handlers;
    }
}
