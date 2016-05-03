package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityGolem;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;

public class CraftIronGolem extends CraftGolem implements IronGolem {

    public CraftIronGolem(CraftServer server, EntityIronGolem entity) {
        super(server, (EntityGolem) entity);
    }

    public EntityIronGolem getHandle() {
        return (EntityIronGolem) this.entity;
    }

    public String toString() {
        return "CraftIronGolem";
    }

    public boolean isPlayerCreated() {
        return this.getHandle().isPlayerCreated();
    }

    public void setPlayerCreated(boolean playerCreated) {
        this.getHandle().setPlayerCreated(playerCreated);
    }

    public EntityType getType() {
        return EntityType.IRON_GOLEM;
    }
}
