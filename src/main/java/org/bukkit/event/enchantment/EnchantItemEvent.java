package org.bukkit.event.enchantment;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class EnchantItemEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Block table;
    private final ItemStack item;
    private int level;
    private boolean cancelled;
    private final Map enchants;
    private final Player enchanter;
    private int button;

    public EnchantItemEvent(Player enchanter, InventoryView view, Block table, ItemStack item, int level, Map enchants, int i) {
        super(view);
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.level = level;
        this.enchants = new HashMap(enchants);
        this.cancelled = false;
        this.button = i;
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

    public int getExpLevelCost() {
        return this.level;
    }

    public void setExpLevelCost(int level) {
        this.level = level;
    }

    public Map getEnchantsToAdd() {
        return this.enchants;
    }

    public int whichButton() {
        return this.button;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return EnchantItemEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EnchantItemEvent.handlers;
    }
}
