package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;

public class ChunkLoadEvent extends ChunkEvent {

    private static final HandlerList handlers = new HandlerList();
    private final boolean newChunk;

    public ChunkLoadEvent(Chunk chunk, boolean newChunk) {
        super(chunk);
        this.newChunk = newChunk;
    }

    public boolean isNewChunk() {
        return this.newChunk;
    }

    public HandlerList getHandlers() {
        return ChunkLoadEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return ChunkLoadEvent.handlers;
    }
}
