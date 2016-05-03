package org.bukkit.inventory;

import org.bukkit.block.Furnace;

public interface FurnaceInventory extends Inventory {

    ItemStack getResult();

    ItemStack getFuel();

    ItemStack getSmelting();

    void setFuel(ItemStack itemstack);

    void setResult(ItemStack itemstack);

    void setSmelting(ItemStack itemstack);

    Furnace getHolder();
}
