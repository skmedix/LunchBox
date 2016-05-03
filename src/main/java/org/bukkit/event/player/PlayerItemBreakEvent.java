package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerItemBreakEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final ItemStack brokenItem;

    public PlayerItemBreakEvent(Player player, ItemStack brokenItem) {
        super(player);
        this.brokenItem = brokenItem;
    }

    public ItemStack getBrokenItem() {
        return this.brokenItem;
    }

    public HandlerList getHandlers() {
        return PlayerItemBreakEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerItemBreakEvent.handlers;
    }
}
