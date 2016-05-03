package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

    public CraftTNTPrimed(CraftServer server, EntityTNTPrimed entity) {
        super(server, entity);
    }

    public float getYield() {
        return this.getHandle().yield;
    }

    public boolean isIncendiary() {
        return this.getHandle().isIncendiary;
    }

    public void setIsIncendiary(boolean isIncendiary) {
        this.getHandle().isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
        this.getHandle().yield = yield;
    }

    public int getFuseTicks() {
        return this.getHandle().fuseTicks;
    }

    public void setFuseTicks(int fuseTicks) {
        this.getHandle().fuseTicks = fuseTicks;
    }

    public EntityTNTPrimed getHandle() {
        return (EntityTNTPrimed) this.entity;
    }

    public String toString() {
        return "CraftTNTPrimed";
    }

    public EntityType getType() {
        return EntityType.PRIMED_TNT;
    }

    public Entity getSource() {
        EntityLiving source = this.getHandle().getSource();

        if (source != null) {
            CraftEntity bukkitEntity = source.getBukkitEntity();

            if (bukkitEntity.isValid()) {
                return bukkitEntity;
            }
        }

        return null;
    }
}
