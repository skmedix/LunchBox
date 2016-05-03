package org.bukkit.enchantments;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.defaults.EnchantCommand;
import org.bukkit.inventory.ItemStack;

public abstract class Enchantment {

    public static final Enchantment PROTECTION_ENVIRONMENTAL = new EnchantmentWrapper(0);
    public static final Enchantment PROTECTION_FIRE = new EnchantmentWrapper(1);
    public static final Enchantment PROTECTION_FALL = new EnchantmentWrapper(2);
    public static final Enchantment PROTECTION_EXPLOSIONS = new EnchantmentWrapper(3);
    public static final Enchantment PROTECTION_PROJECTILE = new EnchantmentWrapper(4);
    public static final Enchantment OXYGEN = new EnchantmentWrapper(5);
    public static final Enchantment WATER_WORKER = new EnchantmentWrapper(6);
    public static final Enchantment THORNS = new EnchantmentWrapper(7);
    public static final Enchantment DEPTH_STRIDER = new EnchantmentWrapper(8);
    public static final Enchantment DAMAGE_ALL = new EnchantmentWrapper(16);
    public static final Enchantment DAMAGE_UNDEAD = new EnchantmentWrapper(17);
    public static final Enchantment DAMAGE_ARTHROPODS = new EnchantmentWrapper(18);
    public static final Enchantment KNOCKBACK = new EnchantmentWrapper(19);
    public static final Enchantment FIRE_ASPECT = new EnchantmentWrapper(20);
    public static final Enchantment LOOT_BONUS_MOBS = new EnchantmentWrapper(21);
    public static final Enchantment DIG_SPEED = new EnchantmentWrapper(32);
    public static final Enchantment SILK_TOUCH = new EnchantmentWrapper(33);
    public static final Enchantment DURABILITY = new EnchantmentWrapper(34);
    public static final Enchantment LOOT_BONUS_BLOCKS = new EnchantmentWrapper(35);
    public static final Enchantment ARROW_DAMAGE = new EnchantmentWrapper(48);
    public static final Enchantment ARROW_KNOCKBACK = new EnchantmentWrapper(49);
    public static final Enchantment ARROW_FIRE = new EnchantmentWrapper(50);
    public static final Enchantment ARROW_INFINITE = new EnchantmentWrapper(51);
    public static final Enchantment LUCK = new EnchantmentWrapper(61);
    public static final Enchantment LURE = new EnchantmentWrapper(62);
    private static final Map byId = new HashMap();
    private static final Map byName = new HashMap();
    private static boolean acceptingNew = true;
    private final int id;

    public Enchantment(int id) {
        this.id = id;
    }

    /** @deprecated */
    @Deprecated
    public int getId() {
        return this.id;
    }

    public abstract String getName();

    public abstract int getMaxLevel();

    public abstract int getStartLevel();

    public abstract EnchantmentTarget getItemTarget();

    public abstract boolean conflictsWith(Enchantment enchantment);

    public abstract boolean canEnchantItem(ItemStack itemstack);

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Enchantment)) {
            return false;
        } else {
            Enchantment other = (Enchantment) obj;

            return this.id == other.id;
        }
    }

    public int hashCode() {
        return this.id;
    }

    public String toString() {
        return "Enchantment[" + this.id + ", " + this.getName() + "]";
    }

    public static void registerEnchantment(Enchantment enchantment) {
        if (!Enchantment.byId.containsKey(Integer.valueOf(enchantment.id)) && !Enchantment.byName.containsKey(enchantment.getName())) {
            if (!isAcceptingRegistrations()) {
                throw new IllegalStateException("No longer accepting new enchantments (can only be done by the server implementation)");
            } else {
                Enchantment.byId.put(Integer.valueOf(enchantment.id), enchantment);
                Enchantment.byName.put(enchantment.getName(), enchantment);
            }
        } else {
            throw new IllegalArgumentException("Cannot set already-set enchantment");
        }
    }

    public static boolean isAcceptingRegistrations() {
        return Enchantment.acceptingNew;
    }

    public static void stopAcceptingRegistrations() {
        Enchantment.acceptingNew = false;
        EnchantCommand.buildEnchantments();
    }

    /** @deprecated */
    @Deprecated
    public static Enchantment getById(int id) {
        return (Enchantment) Enchantment.byId.get(Integer.valueOf(id));
    }

    public static Enchantment getByName(String name) {
        return (Enchantment) Enchantment.byName.get(name);
    }

    public static Enchantment[] values() {
        return (Enchantment[]) Enchantment.byId.values().toArray(new Enchantment[Enchantment.byId.size()]);
    }
}
