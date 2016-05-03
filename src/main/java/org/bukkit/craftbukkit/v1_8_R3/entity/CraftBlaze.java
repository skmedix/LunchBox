package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityBlaze;
import net.minecraft.server.v1_8_R3.EntityMonster;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;

public class CraftBlaze extends CraftMonster implements Blaze {

    public CraftBlaze(CraftServer server, EntityBlaze entity) {
        super(server, (EntityMonster) entity);
    }

    public EntityBlaze getHandle() {
        return (EntityBlaze) this.entity;
    }

    public String toString() {
        return "CraftBlaze";
    }

    public EntityType getType() {
        return EntityType.BLAZE;
    }
}
