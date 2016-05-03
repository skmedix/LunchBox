package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityFlying;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Flying;

public class CraftFlying extends CraftLivingEntity implements Flying {

    public CraftFlying(CraftServer server, EntityFlying entity) {
        super(server, (EntityLiving) entity);
    }

    public EntityFlying getHandle() {
        return (EntityFlying) this.entity;
    }

    public String toString() {
        return "CraftFlying";
    }
}
