package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;

public class InventoryOpenEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    public InventoryOpenEvent(InventoryView transaction) {
        super(transaction);
    }

    public final HumanEntity getPlayer() {
        return this.transaction.getPlayer();
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return InventoryOpenEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return InventoryOpenEvent.handlers;
    }
}
