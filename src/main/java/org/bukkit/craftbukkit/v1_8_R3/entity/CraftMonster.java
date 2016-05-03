package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityMonster;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Monster;

public class CraftMonster extends CraftCreature implements Monster {

    public CraftMonster(CraftServer server, EntityMonster entity) {
        super(server, (EntityCreature) entity);
    }

    public EntityMonster getHandle() {
        return (EntityMonster) this.entity;
    }

    public String toString() {
        return "CraftMonster";
    }
}
