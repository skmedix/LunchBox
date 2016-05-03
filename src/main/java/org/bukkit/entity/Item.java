package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;

public interface Item extends Entity {

    ItemStack getItemStack();

    void setItemStack(ItemStack itemstack);

    int getPickupDelay();

    void setPickupDelay(int i);
}
