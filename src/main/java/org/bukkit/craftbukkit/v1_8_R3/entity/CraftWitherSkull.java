package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkull;

public class CraftWitherSkull extends CraftFireball implements WitherSkull {

    public CraftWitherSkull(CraftServer server, EntityWitherSkull entity) {
        super(server, (EntityFireball) entity);
    }

    public void setCharged(boolean charged) {
        this.getHandle().setCharged(charged);
    }

    public boolean isCharged() {
        return this.getHandle().isCharged();
    }

    public EntityWitherSkull getHandle() {
        return (EntityWitherSkull) this.entity;
    }

    public String toString() {
        return "CraftWitherSkull";
    }

    public EntityType getType() {
        return EntityType.WITHER_SKULL;
    }
}
