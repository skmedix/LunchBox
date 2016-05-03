package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityAmbient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.EntityType;

public class CraftAmbient extends CraftLivingEntity implements Ambient {

    public CraftAmbient(CraftServer server, EntityAmbient entity) {
        super(server, (EntityLiving) entity);
    }

    public EntityAmbient getHandle() {
        return (EntityAmbient) this.entity;
    }

    public String toString() {
        return "CraftAmbient";
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
