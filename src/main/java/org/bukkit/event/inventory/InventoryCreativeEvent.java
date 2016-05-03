package org.bukkit.event.inventory;

import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryCreativeEvent extends InventoryClickEvent {

    private ItemStack item;

    public InventoryCreativeEvent(InventoryView what, InventoryType.SlotType type, int slot, ItemStack newItem) {
        super(what, type, slot, ClickType.CREATIVE, InventoryAction.PLACE_ALL);
        this.item = newItem;
    }

    public ItemStack getCursor() {
        return this.item;
    }

    public void setCursor(ItemStack item) {
        this.item = item;
    }
}
