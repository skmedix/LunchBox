package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityGolem;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Golem;

public class CraftGolem extends CraftCreature implements Golem {

    public CraftGolem(CraftServer server, EntityGolem entity) {
        super(server, (EntityCreature) entity);
    }

    public EntityGolem getHandle() {
        return (EntityGolem) this.entity;
    }

    public String toString() {
        return "CraftGolem";
    }
}
