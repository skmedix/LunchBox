package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityMonster;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreeperPowerEvent;

public class CraftCreeper extends CraftMonster implements Creeper {

    public CraftCreeper(CraftServer server, EntityCreeper entity) {
        super(server, (EntityMonster) entity);
    }

    public boolean isPowered() {
        return this.getHandle().isPowered();
    }

    public void setPowered(boolean powered) {
        CraftServer server = this.server;
        Creeper entity = (Creeper) this.getHandle().getBukkitEntity();
        CreeperPowerEvent event;

        if (powered) {
            event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_ON);
            server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.getHandle().setPowered(true);
            }
        } else {
            event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_OFF);
            server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.getHandle().setPowered(false);
            }
        }

    }

    public EntityCreeper getHandle() {
        return (EntityCreeper) this.entity;
    }

    public String toString() {
        return "CraftCreeper";
    }

    public EntityType getType() {
        return EntityType.CREEPER;
    }
}
