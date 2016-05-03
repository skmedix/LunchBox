package org.bukkit.event.enchantment;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class PrepareItemEnchantEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Block table;
    private final ItemStack item;
    private final int[] levelsOffered;
    private final int bonus;
    private boolean cancelled;
    private final Player enchanter;

    public PrepareItemEnchantEvent(Player enchanter, InventoryView view, Block table, ItemStack item, int[] levelsOffered, int bonus) {
        super(view);
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.levelsOffered = levelsOffered;
        this.bonus = bonus;
        this.cancelled = false;
    }

    public Player getEnchanter() {
        return this.enchanter;
    }

    public Block getEnchantBlock() {
        return this.table;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public int[] getExpLevelCostsOffered() {
        return this.levelsOffered;
    }

    public int getEnchantmentBonus() {
        return this.bonus;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return PrepareItemEnchantEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PrepareItemEnchantEvent.handlers;
    }
}
