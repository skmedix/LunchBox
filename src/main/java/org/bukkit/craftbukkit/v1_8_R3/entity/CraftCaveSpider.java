package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.EntitySpider;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.EntityType;

public class CraftCaveSpider extends CraftSpider implements CaveSpider {

    public CraftCaveSpider(CraftServer server, EntityCaveSpider entity) {
        super(server, (EntitySpider) entity);
    }

    public EntityCaveSpider getHandle() {
        return (EntityCaveSpider) this.entity;
    }

    public String toString() {
        return "CraftCaveSpider";
    }

    public EntityType getType() {
        return EntityType.CAVE_SPIDER;
    }
}
