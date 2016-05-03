package org.bukkit.inventory;

import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;

public abstract class InventoryView {

    public static final int OUTSIDE = -999;

    public abstract Inventory getTopInventory();

    public abstract Inventory getBottomInventory();

    public abstract HumanEntity getPlayer();

    public abstract InventoryType getType();

    public void setItem(int slot, ItemStack item) {
        if (slot != -999) {
            if (slot < this.getTopInventory().getSize()) {
                this.getTopInventory().setItem(this.convertSlot(slot), item);
            } else {
                this.getBottomInventory().setItem(this.convertSlot(slot), item);
            }
        } else {
            this.getPlayer().getWorld().dropItemNaturally(this.getPlayer().getLocation(), item);
        }

    }

    public ItemStack getItem(int slot) {
        return slot == -999 ? null : (slot < this.getTopInventory().getSize() ? this.getTopInventory().getItem(this.convertSlot(slot)) : this.getBottomInventory().getItem(this.convertSlot(slot)));
    }

    public final void setCursor(ItemStack item) {
        this.getPlayer().setItemOnCursor(item);
    }

    public final ItemStack getCursor() {
        return this.getPlayer().getItemOnCursor();
    }

    public final int convertSlot(int rawSlot) {
        int numInTop = this.getTopInventory().getSize();

        if (rawSlot < numInTop) {
            return rawSlot;
        } else {
            int slot = rawSlot - numInTop;

            if (this.getPlayer().getGameMode() == GameMode.CREATIVE && this.getType() == InventoryType.PLAYER) {
                return slot;
            } else {
                if (this.getType() == InventoryType.CRAFTING) {
                    if (slot < 4) {
                        return 39 - slot;
                    }

                    slot -= 4;
                }

                if (slot >= 27) {
                    slot -= 27;
                } else {
                    slot += 9;
                }

                return slot;
            }
        }
    }

    public final void close() {
        this.getPlayer().closeInventory();
    }

    public final int countSlots() {
        return this.getTopInventory().getSize() + this.getBottomInventory().getSize();
    }

    public final boolean setProperty(InventoryView.Property prop, int value) {
        return this.getPlayer().setWindowProperty(prop, value);
    }

    public final String getTitle() {
        return this.getTopInventory().getTitle();
    }

    public static enum Property {

        BREW_TIME(0, InventoryType.BREWING), COOK_TIME(0, InventoryType.FURNACE), BURN_TIME(1, InventoryType.FURNACE), TICKS_FOR_CURRENT_FUEL(2, InventoryType.FURNACE), ENCHANT_BUTTON1(0, InventoryType.ENCHANTING), ENCHANT_BUTTON2(1, InventoryType.ENCHANTING), ENCHANT_BUTTON3(2, InventoryType.ENCHANTING);

        int id;
        InventoryType style;

        private Property(int id, InventoryType appliesTo) {
            this.id = id;
            this.style = appliesTo;
        }

        public InventoryType getType() {
            return this.style;
        }

        /** @deprecated */
        @Deprecated
        public int getId() {
            return this.id;
        }
    }
}
