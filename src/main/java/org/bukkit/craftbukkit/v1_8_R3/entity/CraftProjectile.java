package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityProjectile;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public abstract class CraftProjectile extends AbstractProjectile implements Projectile {

    public CraftProjectile(CraftServer server, Entity entity) {
        super(server, entity);
    }

    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            this.getHandle().shooter = (EntityLiving) ((CraftLivingEntity) shooter).entity;
            if (shooter instanceof CraftHumanEntity) {
                this.getHandle().shooterName = ((CraftHumanEntity) shooter).getName();
            }
        } else {
            this.getHandle().shooter = null;
            this.getHandle().shooterName = null;
        }

        this.getHandle().projectileSource = shooter;
    }

    public EntityProjectile getHandle() {
        return (EntityProjectile) this.entity;
    }

    public String toString() {
        return "CraftProjectile";
    }

    /** @deprecated */
    @Deprecated
    public LivingEntity getShooter() {
        return this.getHandle().shooter == null ? null : (LivingEntity) this.getHandle().shooter.getBukkitEntity();
    }

    /** @deprecated */
    @Deprecated
    public void setShooter(LivingEntity shooter) {
        if (shooter != null) {
            this.getHandle().shooter = ((CraftLivingEntity) shooter).getHandle();
            if (shooter instanceof CraftHumanEntity) {
                this.getHandle().shooterName = ((CraftHumanEntity) shooter).getName();
            }

        }
    }
}
