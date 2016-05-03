package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityEndermite;
import net.minecraft.server.v1_8_R3.EntityMonster;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.EntityType;

public class CraftEndermite extends CraftMonster implements Endermite {

    public CraftEndermite(CraftServer server, EntityEndermite entity) {
        super(server, (EntityMonster) entity);
    }

    public String toString() {
        return "CraftEndermite";
    }

    public EntityType getType() {
        return EntityType.ENDERMITE;
    }
}
