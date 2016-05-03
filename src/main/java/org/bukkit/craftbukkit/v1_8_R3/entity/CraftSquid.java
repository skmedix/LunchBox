package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntitySquid;
import net.minecraft.server.v1_8_R3.EntityWaterAnimal;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;

public class CraftSquid extends CraftWaterMob implements Squid {

    public CraftSquid(CraftServer server, EntitySquid entity) {
        super(server, (EntityWaterAnimal) entity);
    }

    public EntitySquid getHandle() {
        return (EntitySquid) this.entity;
    }

    public String toString() {
        return "CraftSquid";
    }

    public EntityType getType() {
        return EntityType.SQUID;
    }
}
