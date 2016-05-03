package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntitySnowball;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;

public class CraftSnowball extends CraftProjectile implements Snowball {

    public CraftSnowball(CraftServer server, EntitySnowball entity) {
        super(server, entity);
    }

    public EntitySnowball getHandle() {
        return (EntitySnowball) this.entity;
    }

    public String toString() {
        return "CraftSnowball";
    }

    public EntityType getType() {
        return EntityType.SNOWBALL;
    }
}
