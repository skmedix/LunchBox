package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityZombie;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

public class CraftZombie extends CraftMonster implements Zombie {

    public CraftZombie(CraftServer server, EntityZombie entity) {
        super(server, (EntityMonster) entity);
    }

    public EntityZombie getHandle() {
        return (EntityZombie) this.entity;
    }

    public String toString() {
        return "CraftZombie";
    }

    public EntityType getType() {
        return EntityType.ZOMBIE;
    }

    public boolean isBaby() {
        return this.getHandle().isBaby();
    }

    public void setBaby(boolean flag) {
        this.getHandle().setBaby(flag);
    }

    public boolean isVillager() {
        return this.getHandle().isVillager();
    }

    public void setVillager(boolean flag) {
        this.getHandle().setVillager(flag);
    }
}
