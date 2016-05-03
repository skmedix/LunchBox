package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityFlying;
import net.minecraft.server.v1_8_R3.EntityGhast;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;

public class CraftGhast extends CraftFlying implements Ghast {

    public CraftGhast(CraftServer server, EntityGhast entity) {
        super(server, (EntityFlying) entity);
    }

    public EntityGhast getHandle() {
        return (EntityGhast) this.entity;
    }

    public String toString() {
        return "CraftGhast";
    }

    public EntityType getType() {
        return EntityType.GHAST;
    }
}
