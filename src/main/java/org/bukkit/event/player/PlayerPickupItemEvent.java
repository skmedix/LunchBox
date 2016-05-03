package org.bukkit.event.player;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerPickupItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Item item;
    private boolean cancel = false;
    private final int remaining;

    public PlayerPickupItemEvent(Player player, Item item, int remaining) {
        super(player);
        this.item = item;
        this.remaining = remaining;
    }

    public Item getItem() {
        return this.item;
    }

    public int getRemaining() {
        return this.remaining;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return PlayerPickupItemEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerPickupItemEvent.handlers;
    }
}
