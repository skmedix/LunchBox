package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;

public class PrepareItemCraftEvent extends InventoryEvent {

    private static final HandlerList handlers = new HandlerList();
    private boolean repair;
    private CraftingInventory matrix;

    public PrepareItemCraftEvent(CraftingInventory what, InventoryView view, boolean isRepair) {
        super(view);
        this.matrix = what;
        this.repair = isRepair;
    }

    public Recipe getRecipe() {
        return this.matrix.getRecipe();
    }

    public CraftingInventory getInventory() {
        return this.matrix;
    }

    public boolean isRepair() {
        return this.repair;
    }

    public HandlerList getHandlers() {
        return PrepareItemCraftEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PrepareItemCraftEvent.handlers;
    }
}
