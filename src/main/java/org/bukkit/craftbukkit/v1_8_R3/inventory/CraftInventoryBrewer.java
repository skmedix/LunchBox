package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.IInventory;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBrewer extends CraftInventory implements BrewerInventory {

    public CraftInventoryBrewer(IInventory inventory) {
        super(inventory);
    }

    public ItemStack getIngredient() {
        return this.getItem(3);
    }

    public void setIngredient(ItemStack ingredient) {
        this.setItem(3, ingredient);
    }

    public BrewingStand getHolder() {
        return (BrewingStand) this.inventory.getOwner();
    }
}
