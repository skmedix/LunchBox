package org.bukkit.inventory.meta;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public interface ItemMeta extends Cloneable, ConfigurationSerializable {

    boolean hasDisplayName();

    String getDisplayName();

    void setDisplayName(String s);

    boolean hasLore();

    List getLore();

    void setLore(List list);

    boolean hasEnchants();

    boolean hasEnchant(Enchantment enchantment);

    int getEnchantLevel(Enchantment enchantment);

    Map getEnchants();

    boolean addEnchant(Enchantment enchantment, int i, boolean flag);

    boolean removeEnchant(Enchantment enchantment);

    boolean hasConflictingEnchant(Enchantment enchantment);

    void addItemFlags(ItemFlag... aitemflag);

    void removeItemFlags(ItemFlag... aitemflag);

    Set getItemFlags();

    boolean hasItemFlag(ItemFlag itemflag);

    ItemMeta clone();

    ItemMeta.Spigot spigot();

    public static class Spigot {

        public void setUnbreakable(boolean unbreakable) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isUnbreakable() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
