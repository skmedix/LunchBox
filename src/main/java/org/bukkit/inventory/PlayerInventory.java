package org.bukkit.inventory;

import org.bukkit.entity.HumanEntity;

public interface PlayerInventory extends Inventory {

    ItemStack[] getArmorContents();

    ItemStack getHelmet();

    ItemStack getChestplate();

    ItemStack getLeggings();

    ItemStack getBoots();

    void setItem(int i, ItemStack itemstack);

    void setArmorContents(ItemStack[] aitemstack);

    void setHelmet(ItemStack itemstack);

    void setChestplate(ItemStack itemstack);

    void setLeggings(ItemStack itemstack);

    void setBoots(ItemStack itemstack);

    ItemStack getItemInHand();

    void setItemInHand(ItemStack itemstack);

    int getHeldItemSlot();

    void setHeldItemSlot(int i);

    /** @deprecated */
    @Deprecated
    int clear(int i, int j);

    HumanEntity getHolder();
}
