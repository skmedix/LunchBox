package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.IRecipe;
import net.minecraft.server.v1_8_R3.InventoryCrafting;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.util.Java15Compat;

public class CraftInventoryCrafting extends CraftInventory implements CraftingInventory {

    private final IInventory resultInventory;

    public CraftInventoryCrafting(InventoryCrafting inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public IInventory getResultInventory() {
        return this.resultInventory;
    }

    public IInventory getMatrixInventory() {
        return this.inventory;
    }

    public int getSize() {
        return this.getResultInventory().getSize() + this.getMatrixInventory().getSize();
    }

    public void setContents(ItemStack[] items) {
        int resultLen = this.getResultInventory().getContents().length;
        int len = this.getMatrixInventory().getContents().length + resultLen;

        if (len > items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + len + " or less");
        } else {
            this.setContents(items[0], (ItemStack[]) Java15Compat.Arrays_copyOfRange(items, 1, items.length));
        }
    }

    public ItemStack[] getContents() {
        ItemStack[] items = new ItemStack[this.getSize()];
        net.minecraft.server.v1_8_R3.ItemStack[] mcResultItems = this.getResultInventory().getContents();
        boolean i = false;

        int i;

        for (i = 0; i < mcResultItems.length; ++i) {
            items[i] = CraftItemStack.asCraftMirror(mcResultItems[i]);
        }

        net.minecraft.server.v1_8_R3.ItemStack[] mcItems = this.getMatrixInventory().getContents();

        for (int j = 0; j < mcItems.length; ++j) {
            items[i + j] = CraftItemStack.asCraftMirror(mcItems[j]);
        }

        return items;
    }

    public void setContents(ItemStack result, ItemStack[] contents) {
        this.setResult(result);
        this.setMatrix(contents);
    }

    public CraftItemStack getItem(int index) {
        net.minecraft.server.v1_8_R3.ItemStack item;

        if (index < this.getResultInventory().getSize()) {
            item = this.getResultInventory().getItem(index);
            return item == null ? null : CraftItemStack.asCraftMirror(item);
        } else {
            item = this.getMatrixInventory().getItem(index - this.getResultInventory().getSize());
            return item == null ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    public void setItem(int index, ItemStack item) {
        if (index < this.getResultInventory().getSize()) {
            this.getResultInventory().setItem(index, item == null ? null : CraftItemStack.asNMSCopy(item));
        } else {
            this.getMatrixInventory().setItem(index - this.getResultInventory().getSize(), item == null ? null : CraftItemStack.asNMSCopy(item));
        }

    }

    public ItemStack[] getMatrix() {
        ItemStack[] items = new ItemStack[this.getSize()];
        net.minecraft.server.v1_8_R3.ItemStack[] matrix = this.getMatrixInventory().getContents();

        for (int i = 0; i < matrix.length; ++i) {
            items[i] = CraftItemStack.asCraftMirror(matrix[i]);
        }

        return items;
    }

    public ItemStack getResult() {
        net.minecraft.server.v1_8_R3.ItemStack item = this.getResultInventory().getItem(0);

        return item != null ? CraftItemStack.asCraftMirror(item) : null;
    }

    public void setMatrix(ItemStack[] contents) {
        if (this.getMatrixInventory().getContents().length > contents.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + this.getMatrixInventory().getContents().length + " or less");
        } else {
            net.minecraft.server.v1_8_R3.ItemStack[] mcItems = this.getMatrixInventory().getContents();

            for (int i = 0; i < mcItems.length; ++i) {
                if (i < contents.length) {
                    ItemStack item = contents[i];

                    if (item != null && item.getTypeId() > 0) {
                        mcItems[i] = CraftItemStack.asNMSCopy(item);
                    } else {
                        mcItems[i] = null;
                    }
                } else {
                    mcItems[i] = null;
                }
            }

        }
    }

    public void setResult(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack[] contents = this.getResultInventory().getContents();

        if (item != null && item.getTypeId() > 0) {
            contents[0] = CraftItemStack.asNMSCopy(item);
        } else {
            contents[0] = null;
        }

    }

    public Recipe getRecipe() {
        IRecipe recipe = ((InventoryCrafting) this.getInventory()).currentRecipe;

        return recipe == null ? null : recipe.toBukkitRecipe();
    }
}
