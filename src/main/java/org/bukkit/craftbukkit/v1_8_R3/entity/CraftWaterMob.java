package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityWaterAnimal;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.WaterMob;

public class CraftWaterMob extends CraftLivingEntity implements WaterMob {

    public CraftWaterMob(CraftServer server, EntityWaterAnimal entity) {
        super(server, (EntityLiving) entity);
    }

    public EntityWaterAnimal getHandle() {
        return (EntityWaterAnimal) this.entity;
    }

    public String toString() {
        return "CraftWaterMob";
    }
}
