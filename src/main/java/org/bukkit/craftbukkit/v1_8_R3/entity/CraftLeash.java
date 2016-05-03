package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityHanging;
import net.minecraft.server.v1_8_R3.EntityLeash;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LeashHitch;

public class CraftLeash extends CraftHanging implements LeashHitch {

    public CraftLeash(CraftServer server, EntityLeash entity) {
        super(server, (EntityHanging) entity);
    }

    public EntityLeash getHandle() {
        return (EntityLeash) this.entity;
    }

    public String toString() {
        return "CraftLeash";
    }

    public EntityType getType() {
        return EntityType.LEASH_HITCH;
    }
}
