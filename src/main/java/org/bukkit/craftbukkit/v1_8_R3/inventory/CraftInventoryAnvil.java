package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.inventory.IInventory;
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
        net.minecraft.item.ItemStack item;

        if (slot < this.getIngredientsInventory().getSizeInventory()) {
            item = this.getIngredientsInventory().getStackInSlot(slot);
            return item == null ? null : CraftItemStack.asCraftMirror(item);
        } else {
            item = this.getResultInventory().getStackInSlot(slot - this.getIngredientsInventory().getSizeInventory());
            return item == null ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    public void setItem(int index, ItemStack item) {
        if (index < this.getIngredientsInventory().getSizeInventory()) {
            this.getIngredientsInventory().setInventorySlotContents(index, item == null ? null : CraftItemStack.asNMSCopy(item));
        } else {
            this.getResultInventory().setInventorySlotContents(index - this.getIngredientsInventory().getSizeInventory(), item == null ? null : CraftItemStack.asNMSCopy(item));
        }

    }

    public int getSize() {
        return this.getResultInventory().getSizeInventory() + this.getIngredientsInventory().getSizeInventory();
    }
}
