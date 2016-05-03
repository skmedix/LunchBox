package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntitySmallFireball;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SmallFireball;

public class CraftSmallFireball extends CraftFireball implements SmallFireball {

    public CraftSmallFireball(CraftServer server, EntitySmallFireball entity) {
        super(server, (EntityFireball) entity);
    }

    public EntitySmallFireball getHandle() {
        return (EntitySmallFireball) this.entity;
    }

    public String toString() {
        return "CraftSmallFireball";
    }

    public EntityType getType() {
        return EntityType.SMALL_FIREBALL;
    }
}
