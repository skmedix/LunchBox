package org.bukkit.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerBedEnterEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final Block bed;

    public PlayerBedEnterEvent(Player who, Block bed) {
        super(who);
        this.bed = bed;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Block getBed() {
        return this.bed;
    }

    public HandlerList getHandlers() {
        return PlayerBedEnterEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerBedEnterEvent.handlers;
    }
}
