package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityEnderPearl;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;

public class CraftEnderPearl extends CraftProjectile implements EnderPearl {

    public CraftEnderPearl(CraftServer server, EntityEnderPearl entity) {
        super(server, entity);
    }

    public EntityEnderPearl getHandle() {
        return (EntityEnderPearl) this.entity;
    }

    public String toString() {
        return "CraftEnderPearl";
    }

    public EntityType getType() {
        return EntityType.ENDER_PEARL;
    }
}
