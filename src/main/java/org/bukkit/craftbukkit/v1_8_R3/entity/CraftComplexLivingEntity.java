package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.ComplexLivingEntity;

public abstract class CraftComplexLivingEntity extends CraftLivingEntity implements ComplexLivingEntity {

    public CraftComplexLivingEntity(CraftServer server, EntityLiving entity) {
        super(server, entity);
    }

    public EntityLiving getHandle() {
        return (EntityLiving) this.entity;
    }

    public String toString() {
        return "CraftComplexLivingEntity";
    }
}
