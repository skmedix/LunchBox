package org.bukkit.craftbukkit.v1_8_R3.enchantments;

import net.minecraft.enchantment.EnumEnchantmentType;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class CraftEnchantment extends Enchantment {

    private final net.minecraft.enchantment.Enchantment target;
    private static int[] $SWITCH_TABLE$net$minecraft$server$EnchantmentSlotType;

    public CraftEnchantment(net.minecraft.enchantment.Enchantment target) {
        super(target.effectId);
        this.target = target;
    }

    public int getMaxLevel() {
        return this.target.getMaxLevel();
    }

    public int getStartLevel() {
        return this.target.getMinLevel();
    }

    public EnchantmentTarget getItemTarget() {
        switch ($SWITCH_TABLE$net$minecraft$server$EnchantmentSlotType()[this.target.type.ordinal()]) {
        case 1:
            return EnchantmentTarget.ALL;

        case 2:
            return EnchantmentTarget.ARMOR;

        case 3:
            return EnchantmentTarget.ARMOR_FEET;

        case 4:
            return EnchantmentTarget.ARMOR_LEGS;

        case 5:
            return EnchantmentTarget.ARMOR_TORSO;

        case 6:
            return EnchantmentTarget.ARMOR_HEAD;

        case 7:
            return EnchantmentTarget.WEAPON;

        case 8:
            return EnchantmentTarget.TOOL;

        case 9:
            return EnchantmentTarget.FISHING_ROD;

        case 10:
        default:
            return null;

        case 11:
            return EnchantmentTarget.BOW;
        }
    }

    public boolean canEnchantItem(ItemStack item) {
        return this.target.canApply(CraftItemStack.asNMSCopy(item));
    }

    public String getName() {
        switch (this.target.effectId) {
        case 0:
            return "PROTECTION_ENVIRONMENTAL";

        case 1:
            return "PROTECTION_FIRE";

        case 2:
            return "PROTECTION_FALL";

        case 3:
            return "PROTECTION_EXPLOSIONS";

        case 4:
            return "PROTECTION_PROJECTILE";

        case 5:
            return "OXYGEN";

        case 6:
            return "WATER_WORKER";

        case 7:
            return "THORNS";

        case 8:
            return "DEPTH_STRIDER";

        case 16:
            return "DAMAGE_ALL";

        case 17:
            return "DAMAGE_UNDEAD";

        case 18:
            return "DAMAGE_ARTHROPODS";

        case 19:
            return "KNOCKBACK";

        case 20:
            return "FIRE_ASPECT";

        case 21:
            return "LOOT_BONUS_MOBS";

        case 32:
            return "DIG_SPEED";

        case 33:
            return "SILK_TOUCH";

        case 34:
            return "DURABILITY";

        case 35:
            return "LOOT_BONUS_BLOCKS";

        case 48:
            return "ARROW_DAMAGE";

        case 49:
            return "ARROW_KNOCKBACK";

        case 50:
            return "ARROW_FIRE";

        case 51:
            return "ARROW_INFINITE";

        case 61:
            return "LUCK";

        case 62:
            return "LURE";

        default:
            return "UNKNOWN_ENCHANT_" + this.target.effectId;
        }
    }

    public static net.minecraft.enchantment.Enchantment getRaw(Enchantment enchantment) {
        if (enchantment instanceof EnchantmentWrapper) {
            enchantment = ((EnchantmentWrapper) enchantment).getEnchantment();
        }

        return enchantment instanceof CraftEnchantment ? ((CraftEnchantment) enchantment).target : null;
    }

    public boolean conflictsWith(Enchantment other) {
        if (other instanceof EnchantmentWrapper) {
            other = ((EnchantmentWrapper) other).getEnchantment();
        }

        if (!(other instanceof CraftEnchantment)) {
            return false;
        } else {
            CraftEnchantment ench = (CraftEnchantment) other;

            return !this.target.canApplyTogether(ench.target);
        }
    }

    static int[] $SWITCH_TABLE$net$minecraft$server$EnchantmentSlotType() {
        int[] aint = CraftEnchantment.$SWITCH_TABLE$net$minecraft$server$EnchantmentSlotType;

        if (CraftEnchantment.$SWITCH_TABLE$net$minecraft$server$EnchantmentSlotType != null) {
            return aint;
        } else {
            int[] aint1 = new int[EnumEnchantmentType.values().length];

            try {
                aint1[EnumEnchantmentType.ALL.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[EnumEnchantmentType.ARMOR.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[EnumEnchantmentType.ARMOR_FEET.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[EnumEnchantmentType.ARMOR_HEAD.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[EnumEnchantmentType.ARMOR_LEGS.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[EnumEnchantmentType.ARMOR_TORSO.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[EnumEnchantmentType.BOW.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[EnumEnchantmentType.BREAKABLE.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[EnumEnchantmentType.DIGGER.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[EnumEnchantmentType.FISHING_ROD.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[EnumEnchantmentType.WEAPON.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            CraftEnchantment.$SWITCH_TABLE$net$minecraft$server$EnchantmentSlotType = aint1;
            return aint1;
        }
    }
}
