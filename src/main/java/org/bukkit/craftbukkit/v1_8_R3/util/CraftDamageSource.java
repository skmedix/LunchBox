package org.bukkit.craftbukkit.v1_8_R3.util;

import net.minecraft.server.v1_8_R3.DamageSource;

public final class CraftDamageSource extends DamageSource {

    public static DamageSource copyOf(DamageSource original) {
        CraftDamageSource newSource = new CraftDamageSource(original.translationIndex);

        if (original.ignoresArmor()) {
            newSource.setIgnoreArmor();
        }

        if (original.isMagic()) {
            newSource.setMagic();
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
