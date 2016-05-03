package org.bukkit.inventory;

public interface HorseInventory extends Inventory {

    ItemStack getSaddle();

    ItemStack getArmor();

    void setSaddle(ItemStack itemstack);

    void setArmor(ItemStack itemstack);
}
