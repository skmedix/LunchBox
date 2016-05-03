package org.bukkit.inventory;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public interface ItemFactory {

    ItemMeta getItemMeta(Material material);

    boolean isApplicable(ItemMeta itemmeta, ItemStack itemstack) throws IllegalArgumentException;

    boolean isApplicable(ItemMeta itemmeta, Material material) throws IllegalArgumentException;

    boolean equals(ItemMeta itemmeta, ItemMeta itemmeta1) throws IllegalArgumentException;

    ItemMeta asMetaFor(ItemMeta itemmeta, ItemStack itemstack) throws IllegalArgumentException;

    ItemMeta asMetaFor(ItemMeta itemmeta, Material material) throws IllegalArgumentException;

    Color getDefaultLeatherColor();
}
