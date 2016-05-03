package org.bukkit.inventory;

public interface BeaconInventory extends Inventory {

    void setItem(ItemStack itemstack);

    ItemStack getItem();
}
