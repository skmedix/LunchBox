package org.bukkit.event.player;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerDropItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Item drop;
    private boolean cancel = false;

    public PlayerDropItemEvent(Player player, Item drop) {
        super(player);
        this.drop = drop;
    }

    public Item getItemDrop() {
        return this.drop;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return PlayerDropItemEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerDropItemEvent.handlers;
    }
}
