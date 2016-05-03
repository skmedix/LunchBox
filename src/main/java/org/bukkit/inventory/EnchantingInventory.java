package org.bukkit.inventory;

public interface EnchantingInventory extends Inventory {

    void setItem(ItemStack itemstack);

    ItemStack getItem();

    void setSecondary(ItemStack itemstack);

    ItemStack getSecondary();
}
