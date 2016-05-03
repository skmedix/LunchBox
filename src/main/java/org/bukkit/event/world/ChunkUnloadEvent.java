package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ChunkUnloadEvent extends ChunkEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;

    public ChunkUnloadEvent(Chunk chunk) {
        super(chunk);
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return ChunkUnloadEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return ChunkUnloadEvent.handlers;
    }
}
