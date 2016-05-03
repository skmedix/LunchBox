package org.bukkit.inventory.meta;

import java.util.Map;
import org.bukkit.enchantments.Enchantment;

public interface EnchantmentStorageMeta extends ItemMeta {

    boolean hasStoredEnchants();

    boolean hasStoredEnchant(Enchantment enchantment);

    int getStoredEnchantLevel(Enchantment enchantment);

    Map getStoredEnchants();

    boolean addStoredEnchant(Enchantment enchantment, int i, boolean flag);

    boolean removeStoredEnchant(Enchantment enchantment) throws IllegalArgumentException;

    boolean hasConflictingStoredEnchant(Enchantment enchantment);

    EnchantmentStorageMeta clone();
}
