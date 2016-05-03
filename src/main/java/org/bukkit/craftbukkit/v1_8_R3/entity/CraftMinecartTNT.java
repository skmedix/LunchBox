package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMinecartAbstract;
import net.minecraft.server.v1_8_R3.EntityMinecartTNT;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.ExplosiveMinecart;

final class CraftMinecartTNT extends CraftMinecart implements ExplosiveMinecart {

    CraftMinecartTNT(CraftServer server, EntityMinecartTNT entity) {
        super(server, (EntityMinecartAbstract) entity);
    }

    public String toString() {
        return "CraftMinecartTNT";
    }

    public EntityType getType() {
        return EntityType.MINECART_TNT;
    }
}
