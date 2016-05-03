package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityGiantZombie;
import net.minecraft.server.v1_8_R3.EntityMonster;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;

public class CraftGiant extends CraftMonster implements Giant {

    public CraftGiant(CraftServer server, EntityGiantZombie entity) {
        super(server, (EntityMonster) entity);
    }

    public EntityGiantZombie getHandle() {
        return (EntityGiantZombie) this.entity;
    }

    public String toString() {
        return "CraftGiant";
    }

    public EntityType getType() {
        return EntityType.GIANT;
    }
}
