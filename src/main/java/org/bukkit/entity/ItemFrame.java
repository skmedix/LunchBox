package org.bukkit.entity;

import org.bukkit.Rotation;
import org.bukkit.inventory.ItemStack;

public interface ItemFrame extends Hanging {

    ItemStack getItem();

    void setItem(ItemStack itemstack);

    Rotation getRotation();

    void setRotation(Rotation rotation) throws IllegalArgumentException;
}
