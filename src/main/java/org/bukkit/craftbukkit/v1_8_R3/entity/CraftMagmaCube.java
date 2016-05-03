package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMagmaCube;
import net.minecraft.server.v1_8_R3.EntitySlime;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;

public class CraftMagmaCube extends CraftSlime implements MagmaCube {

    public CraftMagmaCube(CraftServer server, EntityMagmaCube entity) {
        super(server, (EntitySlime) entity);
    }

    public EntityMagmaCube getHandle() {
        return (EntityMagmaCube) this.entity;
    }

    public String toString() {
        return "CraftMagmaCube";
    }

    public EntityType getType() {
        return EntityType.MAGMA_CUBE;
    }
}
