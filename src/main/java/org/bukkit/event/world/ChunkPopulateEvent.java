package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;

public class ChunkPopulateEvent extends ChunkEvent {

    private static final HandlerList handlers = new HandlerList();

    public ChunkPopulateEvent(Chunk chunk) {
        super(chunk);
    }

    public HandlerList getHandlers() {
        return ChunkPopulateEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return ChunkPopulateEvent.handlers;
    }
}
