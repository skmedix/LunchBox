package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityThrownExpBottle;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle extends CraftProjectile implements ThrownExpBottle {

    public CraftThrownExpBottle(CraftServer server, EntityThrownExpBottle entity) {
        super(server, entity);
    }

    public EntityThrownExpBottle getHandle() {
        return (EntityThrownExpBottle) this.entity;
    }

    public String toString() {
        return "EntityThrownExpBottle";
    }

    public EntityType getType() {
        return EntityType.THROWN_EXP_BOTTLE;
    }
}
