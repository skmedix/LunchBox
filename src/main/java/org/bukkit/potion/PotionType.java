package org.bukkit.potion;

public enum PotionType {

    WATER(0, (PotionEffectType) null, 0), REGEN(1, PotionEffectType.REGENERATION, 2), SPEED(2, PotionEffectType.SPEED, 2), FIRE_RESISTANCE(3, PotionEffectType.FIRE_RESISTANCE, 1), POISON(4, PotionEffectType.POISON, 2), INSTANT_HEAL(5, PotionEffectType.HEAL, 2), NIGHT_VISION(6, PotionEffectType.NIGHT_VISION, 1), WEAKNESS(8, PotionEffectType.WEAKNESS, 1), STRENGTH(9, PotionEffectType.INCREASE_DAMAGE, 2), SLOWNESS(10, PotionEffectType.SLOW, 1), JUMP(11, PotionEffectType.JUMP, 2), INSTANT_DAMAGE(12, PotionEffectType.HARM, 2), WATER_BREATHING(13, PotionEffectType.WATER_BREATHING, 1), INVISIBILITY(14, PotionEffectType.INVISIBILITY, 1);

    private final int damageValue;
    private final int maxLevel;
    private final PotionEffectType effect;

    private PotionType(int damageValue, PotionEffectType effect, int maxLevel) {
        this.damageValue = damageValue;
        this.effect = effect;
        this.maxLevel = maxLevel;
    }

    public PotionEffectType getEffectType() {
        return this.effect;
    }

    /** @deprecated */
    @Deprecated
    public int getDamageValue() {
        return this.damageValue;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public boolean isInstant() {
        return this.effect == null ? true : this.effect.isInstant();
    }

    /** @deprecated */
    @Deprecated
    public static PotionType getByDamageValue(int damage) {
        PotionType[] apotiontype;
        int i = (apotiontype = values()).length;

        for (int j = 0; j < i; ++j) {
            PotionType type = apotiontype[j];

            if (type.damageValue == damage) {
                return type;
            }
        }

        return null;
    }

    public static PotionType getByEffect(PotionEffectType effectType) {
        if (effectType == null) {
            return PotionType.WATER;
        } else {
            PotionType[] apotiontype;
            int i = (apotiontype = values()).length;

            for (int j = 0; j < i; ++j) {
                PotionType type = apotiontype[j];

                if (effectType.equals(type.effect)) {
                    return type;
                }
            }

            return null;
        }
    }
}
