package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityLightning;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;

public class CraftLightningStrike extends CraftEntity implements LightningStrike {

    private final LightningStrike.Spigot spigot = new LightningStrike.Spigot() {
        public boolean isSilent() {
            return CraftLightningStrike.this.getHandle().isSilent;
        }
    };

    public CraftLightningStrike(CraftServer server, EntityLightning entity) {
        super(server, entity);
    }

    public boolean isEffect() {
        return ((EntityLightning) super.getHandle()).isEffect;
    }

    public EntityLightning getHandle() {
        return (EntityLightning) this.entity;
    }

    public String toString() {
        return "CraftLightningStrike";
    }

    public EntityType getType() {
        return EntityType.LIGHTNING;
    }

    public LightningStrike.Spigot spigot() {
        return this.spigot;
    }
}
