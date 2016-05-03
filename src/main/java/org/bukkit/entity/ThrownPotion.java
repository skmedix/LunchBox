package org.bukkit.entity;

import java.util.Collection;
import org.bukkit.inventory.ItemStack;

public interface ThrownPotion extends Projectile {

    Collection getEffects();

    ItemStack getItem();

    void setItem(ItemStack itemstack);
}
