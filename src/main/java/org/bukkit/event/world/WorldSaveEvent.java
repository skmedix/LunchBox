package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;

public class WorldSaveEvent extends WorldEvent {

    private static final HandlerList handlers = new HandlerList();

    public WorldSaveEvent(World world) {
        super(world);
    }

    public HandlerList getHandlers() {
        return WorldSaveEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return WorldSaveEvent.handlers;
    }
}
