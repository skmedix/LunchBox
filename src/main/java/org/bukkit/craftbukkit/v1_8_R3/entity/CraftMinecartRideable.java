package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMinecartAbstract;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.RideableMinecart;

public class CraftMinecartRideable extends CraftMinecart implements RideableMinecart {

    public CraftMinecartRideable(CraftServer server, EntityMinecartAbstract entity) {
        super(server, entity);
    }

    public String toString() {
        return "CraftMinecartRideable";
    }

    public EntityType getType() {
        return EntityType.MINECART;
    }
}
