package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityEnderSignal;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;

public class CraftEnderSignal extends CraftEntity implements EnderSignal {

    public CraftEnderSignal(CraftServer server, EntityEnderSignal entity) {
        super(server, entity);
    }

    public EntityEnderSignal getHandle() {
        return (EntityEnderSignal) this.entity;
    }

    public String toString() {
        return "CraftEnderSignal";
    }

    public EntityType getType() {
        return EntityType.ENDER_SIGNAL;
    }
}
