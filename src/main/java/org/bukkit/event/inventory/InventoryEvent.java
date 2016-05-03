package org.bukkit.event.inventory;

import java.util.List;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class InventoryEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    protected InventoryView transaction;

    public InventoryEvent(InventoryView transaction) {
        this.transaction = transaction;
    }

    public Inventory getInventory() {
        return this.transaction.getTopInventory();
    }

    public List getViewers() {
        return this.transaction.getTopInventory().getViewers();
    }

    public InventoryView getView() {
        return this.transaction;
    }

    public HandlerList getHandlers() {
        return InventoryEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return InventoryEvent.handlers;
    }
}
