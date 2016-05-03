package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityWither;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;

public class CraftWither extends CraftMonster implements Wither {

    public CraftWither(CraftServer server, EntityWither entity) {
        super(server, (EntityMonster) entity);
    }

    public EntityWither getHandle() {
        return (EntityWither) this.entity;
    }

    public String toString() {
        return "CraftWither";
    }

    public EntityType getType() {
        return EntityType.WITHER;
    }
}
