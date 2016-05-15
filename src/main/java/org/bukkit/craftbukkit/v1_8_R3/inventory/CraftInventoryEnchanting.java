package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.IInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryEnchanting extends CraftInventory implements EnchantingInventory {

    public CraftInventoryEnchanting(ContainerEnchantment inventory) {
        super((IInventory) inventory);
    }

    public void setItem(ItemStack item) {
        this.setItem(0, item);
    }

    public ItemStack getItem() {
        return this.getItem(0);
    }

    public IInventory getInventory() {
        return this.inventory;
    }

    public void setSecondary(ItemStack item) {
        this.setItem(1, item);
    }

    public ItemStack getSecondary() {
        return this.getItem(1);
    }
}
