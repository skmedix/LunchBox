package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityWitch;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Witch;

public class CraftWitch extends CraftMonster implements Witch {

    public CraftWitch(CraftServer server, EntityWitch entity) {
        super(server, (EntityMonster) entity);
    }

    public EntityWitch getHandle() {
        return (EntityWitch) this.entity;
    }

    public String toString() {
        return "CraftWitch";
    }

    public EntityType getType() {
        return EntityType.WITCH;
    }
}
