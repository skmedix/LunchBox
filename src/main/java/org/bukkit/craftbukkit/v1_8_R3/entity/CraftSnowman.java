package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityGolem;
import net.minecraft.server.v1_8_R3.EntitySnowman;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowman;

public class CraftSnowman extends CraftGolem implements Snowman {

    public CraftSnowman(CraftServer server, EntitySnowman entity) {
        super(server, (EntityGolem) entity);
    }

    public EntitySnowman getHandle() {
        return (EntitySnowman) this.entity;
    }

    public String toString() {
        return "CraftSnowman";
    }

    public EntityType getType() {
        return EntityType.SNOWMAN;
    }
}
