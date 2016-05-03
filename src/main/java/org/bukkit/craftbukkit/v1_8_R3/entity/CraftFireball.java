package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class CraftFireball extends AbstractProjectile implements Fireball {

    public CraftFireball(CraftServer server, EntityFireball entity) {
        super(server, entity);
    }

    public float getYield() {
        return this.getHandle().bukkitYield;
    }

    public boolean isIncendiary() {
        return this.getHandle().isIncendiary;
    }

    public void setIsIncendiary(boolean isIncendiary) {
        this.getHandle().isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
        this.getHandle().bukkitYield = yield;
    }

    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            this.getHandle().shooter = ((CraftLivingEntity) shooter).getHandle();
        } else {
            this.getHandle().shooter = null;
        }

        this.getHandle().projectileSource = shooter;
    }

    public Vector getDirection() {
        return new Vector(this.getHandle().dirX, this.getHandle().dirY, this.getHandle().dirZ);
    }

    public void setDirection(Vector direction) {
        Validate.notNull(direction, "Direction can not be null");
        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();
        double magnitude = (double) MathHelper.sqrt(x * x + y * y + z * z);

        this.getHandle().dirX = x / magnitude;
        this.getHandle().dirY = y / magnitude;
        this.getHandle().dirZ = z / magnitude;
    }

    public EntityFireball getHandle() {
        return (EntityFireball) this.entity;
    }

    public String toString() {
        return "CraftFireball";
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

    /** @deprecated */
    @Deprecated
    public void setShooter(LivingEntity shooter) {
        this.setShooter((ProjectileSource) shooter);
    }

    /** @deprecated */
    @Deprecated
    public LivingEntity getShooter() {
        return this.getHandle().shooter != null ? (LivingEntity) this.getHandle().shooter.getBukkitEntity() : null;
    }
}
