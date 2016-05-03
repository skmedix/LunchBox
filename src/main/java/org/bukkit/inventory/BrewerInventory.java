package org.bukkit.inventory;

import org.bukkit.block.BrewingStand;

public interface BrewerInventory extends Inventory {

    ItemStack getIngredient();

    void setIngredient(ItemStack itemstack);

    BrewingStand getHolder();
}
