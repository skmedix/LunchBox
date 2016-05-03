package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerItemDamageEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final ItemStack item;
    private int damage;
    private boolean cancelled = false;

    public PlayerItemDamageEvent(Player player, ItemStack what, int damage) {
        super(player);
        this.item = what;
        this.damage = damage;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return PlayerItemDamageEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerItemDamageEvent.handlers;
    }
}
