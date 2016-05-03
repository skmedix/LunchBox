package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerItemConsumeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;
    private ItemStack item;

    public PlayerItemConsumeEvent(Player player, ItemStack item) {
        super(player);
        this.item = item;
    }

    public ItemStack getItem() {
        return this.item.clone();
    }

    public void setItem(ItemStack item) {
        if (item == null) {
            this.item = new ItemStack(Material.AIR);
        } else {
            this.item = item;
        }

    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public HandlerList getHandlers() {
        return PlayerItemConsumeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerItemConsumeEvent.handlers;
    }
}
