package org.bukkit.craftbukkit.v1_8_R3.util;

import net.minecraft.util.DamageSource;

public final class CraftDamageSource extends DamageSource {

    public static DamageSource copyOf(DamageSource original) {
        CraftDamageSource newSource = new CraftDamageSource(original.damageType);

        if (original.isUnblockable()) {
            newSource.setDamageBypassesArmor();
        }

        if (original.isMagicDamage()) {
            newSource.setMagicDamage();
        }

        if (original.isExplosion()) {
            newSource.setExplosion();
        }

        return newSource;
    }

    private CraftDamageSource(String identifier) {
        super(identifier);
    }
}
