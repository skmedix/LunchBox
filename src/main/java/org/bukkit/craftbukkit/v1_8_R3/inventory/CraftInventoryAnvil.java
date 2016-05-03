package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.IInventory;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryAnvil extends CraftInventory implements AnvilInventory {

    private final IInventory resultInventory;

    public CraftInventoryAnvil(IInventory inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public IInventory getResultInventory() {
        return this.resultInventory;
    }

    public IInventory getIngredientsInventory() {
        return this.inventory;
    }

    public ItemStack getItem(int slot) {
        net.minecraft.server.v1_8_R3.ItemStack item;

        if (slot < this.getIngredientsInventory().getSize()) {
            item = this.getIngredientsInventory().getItem(slot);
            return item == null ? null : CraftItemStack.asCraftMirror(item);
        } else {
            item = this.getResultInventory().getItem(slot - this.getIngredientsInventory().getSize());
            return item == null ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    public void setItem(int index, ItemStack item) {
        if (index < this.getIngredientsInventory().getSize()) {
            this.getIngredientsInventory().setItem(index, item == null ? null : CraftItemStack.asNMSCopy(item));
        } else {
            this.getResultInventory().setItem(index - this.getIngredientsInventory().getSize(), item == null ? null : CraftItemStack.asNMSCopy(item));
        }

    }

    public int getSize() {
        return this.getResultInventory().getSize() + this.getIngredientsInventory().getSize();
    }
}
