package org.bukkit.inventory;

import org.bukkit.entity.Entity;

public interface EntityEquipment {

    ItemStack getItemInHand();

    void setItemInHand(ItemStack itemstack);

    ItemStack getHelmet();

    void setHelmet(ItemStack itemstack);

    ItemStack getChestplate();

    void setChestplate(ItemStack itemstack);

    ItemStack getLeggings();

    void setLeggings(ItemStack itemstack);

    ItemStack getBoots();

    void setBoots(ItemStack itemstack);

    ItemStack[] getArmorContents();

    void setArmorContents(ItemStack[] aitemstack);

    void clear();

    float getItemInHandDropChance();

    void setItemInHandDropChance(float f);

    float getHelmetDropChance();

    void setHelmetDropChance(float f);

    float getChestplateDropChance();

    void setChestplateDropChance(float f);

    float getLeggingsDropChance();

    void setLeggingsDropChance(float f);

    float getBootsDropChance();

    void setBootsDropChance(float f);

    Entity getHolder();
}
