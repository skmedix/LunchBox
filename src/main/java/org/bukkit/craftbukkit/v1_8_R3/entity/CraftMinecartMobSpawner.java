package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMinecartAbstract;
import net.minecraft.server.v1_8_R3.EntityMinecartMobSpawner;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.SpawnerMinecart;

final class CraftMinecartMobSpawner extends CraftMinecart implements SpawnerMinecart {

    CraftMinecartMobSpawner(CraftServer server, EntityMinecartMobSpawner entity) {
        super(server, (EntityMinecartAbstract) entity);
    }

    public String toString() {
        return "CraftMinecartMobSpawner";
    }

    public EntityType getType() {
        return EntityType.MINECART_MOB_SPAWNER;
    }
}
