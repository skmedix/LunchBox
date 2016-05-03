package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntitySpider;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;

public class CraftSpider extends CraftMonster implements Spider {

    public CraftSpider(CraftServer server, EntitySpider entity) {
        super(server, (EntityMonster) entity);
    }

    public EntitySpider getHandle() {
        return (EntitySpider) this.entity;
    }

    public String toString() {
        return "CraftSpider";
    }

    public EntityType getType() {
        return EntityType.SPIDER;
    }
}
