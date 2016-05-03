package org.bukkit.potion;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class Potion {

    private boolean extended;
    private boolean splash;
    private int level;
    private int name;
    private PotionType type;
    private static PotionBrewer brewer;
    private static final int EXTENDED_BIT = 64;
    private static final int POTION_BIT = 15;
    private static final int SPLASH_BIT = 16384;
    private static final int TIER_BIT = 32;
    private static final int TIER_SHIFT = 5;
    private static final int NAME_BIT = 63;

    public Potion(PotionType type) {
        this.extended = false;
        this.splash = false;
        this.level = 1;
        this.name = -1;
        this.type = type;
        if (type != null) {
            this.name = type.getDamageValue();
        }

        if (type == null || type == PotionType.WATER) {
            this.level = 0;
        }

    }

    /** @deprecated */
    @Deprecated
    public Potion(PotionType type, Potion.Tier tier) {
        this(type, tier == Potion.Tier.TWO ? 2 : 1);
        Validate.notNull(type, "Type cannot be null");
    }

    /** @deprecated */
    @Deprecated
    public Potion(PotionType type, Potion.Tier tier, boolean splash) {
        this(type, tier == Potion.Tier.TWO ? 2 : 1, splash);
    }

    /** @deprecated */
    @Deprecated
    public Potion(PotionType type, Potion.Tier tier, boolean splash, boolean extended) {
        this(type, tier, splash);
        this.extended = extended;
    }

    public Potion(PotionType type, int level) {
        this(type);
        Validate.notNull(type, "Type cannot be null");
        Validate.isTrue(type != PotionType.WATER, "Water bottles don\'t have a level!");
        Validate.isTrue(level > 0 && level < 3, "Level must be 1 or 2");
        this.level = level;
    }

    /** @deprecated */
    @Deprecated
    public Potion(PotionType type, int level, boolean splash) {
        this(type, level);
        this.splash = splash;
    }

    /** @deprecated */
    @Deprecated
    public Potion(PotionType type, int level, boolean splash, boolean extended) {
        this(type, level, splash);
        this.extended = extended;
    }

    public Potion(int name) {
        this(PotionType.getByDamageValue(name & 15));
        this.name = name & 63;
        if ((name & 15) == 0) {
            this.type = null;
        }

    }

    public Potion splash() {
        this.setSplash(true);
        return this;
    }

    public Potion extend() {
        this.setHasExtendedDuration(true);
        return this;
    }

    public void apply(ItemStack to) {
        Validate.notNull(to, "itemstack cannot be null");
        Validate.isTrue(to.getType() == Material.POTION, "given itemstack is not a potion");
        to.setDurability(this.toDamageValue());
    }

    public void apply(LivingEntity to) {
        Validate.notNull(to, "entity cannot be null");
        to.addPotionEffects(this.getEffects());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            Potion other = (Potion) obj;

            return this.extended == other.extended && this.splash == other.splash && this.level == other.level && this.type == other.type;
        } else {
            return false;
        }
    }

    public Collection getEffects() {
        return (Collection) (this.type == null ? ImmutableList.of() : getBrewer().getEffectsFromDamage(this.toDamageValue()));
    }

    public int getLevel() {
        return this.level;
    }

    /** @deprecated */
    @Deprecated
    public Potion.Tier getTier() {
        return this.level == 2 ? Potion.Tier.TWO : Potion.Tier.ONE;
    }

    public PotionType getType() {
        return this.type;
    }

    public boolean hasExtendedDuration() {
        return this.extended;
    }

    public int hashCode() {
        int result = 31 + this.level;

        result = 31 * result + (this.extended ? 1231 : 1237);
        result = 31 * result + (this.splash ? 1231 : 1237);
        result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
        return result;
    }

    public boolean isSplash() {
        return this.splash;
    }

    public void setHasExtendedDuration(boolean isExtended) {
        Validate.isTrue(this.type == null || !this.type.isInstant(), "Instant potions cannot be extended");
        this.extended = isExtended;
    }

    public void setSplash(boolean isSplash) {
        this.splash = isSplash;
    }

    /** @deprecated */
    @Deprecated
    public void setTier(Potion.Tier tier) {
        Validate.notNull(tier, "tier cannot be null");
        this.level = tier == Potion.Tier.TWO ? 2 : 1;
    }

    public void setType(PotionType type) {
        this.type = type;
    }

    public void setLevel(int level) {
        Validate.notNull(this.type, "No-effect potions don\'t have a level.");
        int max = this.type.getMaxLevel();

        Validate.isTrue(level > 0 && level <= max, "Level must be " + (max == 1 ? "" : "between 1 and ") + max + " for this potion");
        this.level = level;
    }

    /** @deprecated */
    @Deprecated
    public short toDamageValue() {
        if (this.type == PotionType.WATER) {
            return (short) 0;
        } else {
            short damage;

            if (this.type == null) {
                damage = (short) (this.name == 0 ? 8192 : this.name);
            } else {
                damage = (short) (this.level - 1);
                damage = (short) (damage << 5);
                damage |= (short) this.type.getDamageValue();
            }

            if (this.splash) {
                damage = (short) (damage | 16384);
            }

            if (this.extended) {
                damage = (short) (damage | 64);
            }

            return damage;
        }
    }

    public ItemStack toItemStack(int amount) {
        return new ItemStack(Material.POTION, amount, this.toDamageValue());
    }

    /** @deprecated */
    @Deprecated
    public static Potion fromDamage(int damage) {
        PotionType type = PotionType.getByDamageValue(damage & 15);
        Potion potion;

        if (type != null && type != PotionType.WATER) {
            int level = (damage & 32) >> 5;

            ++level;
            potion = new Potion(type, level);
        } else {
            potion = new Potion(damage & 63);
        }

        if ((damage & 16384) > 0) {
            potion = potion.splash();
        }

        if ((type == null || !type.isInstant()) && (damage & 64) > 0) {
            potion = potion.extend();
        }

        return potion;
    }

    public static Potion fromItemStack(ItemStack item) {
        Validate.notNull(item, "item cannot be null");
        if (item.getType() != Material.POTION) {
            throw new IllegalArgumentException("item is not a potion");
        } else {
            return fromDamage(item.getDurability());
        }
    }

    public static PotionBrewer getBrewer() {
        return Potion.brewer;
    }

    public static void setPotionBrewer(PotionBrewer other) {
        if (Potion.brewer != null) {
            throw new IllegalArgumentException("brewer can only be set internally");
        } else {
            Potion.brewer = other;
        }
    }

    /** @deprecated */
    @Deprecated
    public int getNameId() {
        return this.name;
    }

    /** @deprecated */
    @Deprecated
    public static enum Tier {

        ONE(0), TWO(32);

        private int damageBit;

        private Tier(int bit) {
            this.damageBit = bit;
        }

        public int getDamageBit() {
            return this.damageBit;
        }

        public static Potion.Tier getByDamageBit(int damageBit) {
            Potion.Tier[] apotion_tier;
            int i = (apotion_tier = values()).length;

            for (int j = 0; j < i; ++j) {
                Potion.Tier tier = apotion_tier[j];

                if (tier.damageBit == damageBit) {
                    return tier;
                }
            }

            return null;
        }
    }
}
