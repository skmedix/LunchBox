package org.bukkit.event.player;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerEditBookEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final BookMeta previousBookMeta;
    private final int slot;
    private BookMeta newBookMeta;
    private boolean isSigning;
    private boolean cancel;

    public PlayerEditBookEvent(Player who, int slot, BookMeta previousBookMeta, BookMeta newBookMeta, boolean isSigning) {
        super(who);
        Validate.isTrue(slot >= 0 && slot <= 8, "Slot must be in range 0-8 inclusive");
        Validate.notNull(previousBookMeta, "Previous book meta must not be null");
        Validate.notNull(newBookMeta, "New book meta must not be null");
        Bukkit.getItemFactory().equals(previousBookMeta, newBookMeta);
        this.previousBookMeta = previousBookMeta;
        this.newBookMeta = newBookMeta;
        this.slot = slot;
        this.isSigning = isSigning;
        this.cancel = false;
    }

    public BookMeta getPreviousBookMeta() {
        return this.previousBookMeta.clone();
    }

    public BookMeta getNewBookMeta() {
        return this.newBookMeta.clone();
    }

    public int getSlot() {
        return this.slot;
    }

    public void setNewBookMeta(BookMeta newBookMeta) throws IllegalArgumentException {
        Validate.notNull(newBookMeta, "New book meta must not be null");
        Bukkit.getItemFactory().equals(newBookMeta, (ItemMeta) null);
        this.newBookMeta = newBookMeta.clone();
    }

    public boolean isSigning() {
        return this.isSigning;
    }

    public void setSigning(boolean signing) {
        this.isSigning = signing;
    }

    public HandlerList getHandlers() {
        return PlayerEditBookEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerEditBookEvent.handlers;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
