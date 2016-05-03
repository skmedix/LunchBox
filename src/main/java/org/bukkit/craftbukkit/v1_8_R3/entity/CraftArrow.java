package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityArrow;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;

public class CraftArrow extends AbstractProjectile implements Arrow {

    private final Arrow.Spigot spigot = new Arrow.Spigot() {
        public double getDamage() {
            return CraftArrow.this.getHandle().j();
        }

        public void setDamage(double damage) {
            CraftArrow.this.getHandle().b(damage);
        }
    };

    public CraftArrow(CraftServer server, EntityArrow entity) {
        super(server, entity);
    }

    public void setKnockbackStrength(int knockbackStrength) {
        Validate.isTrue(knockbackStrength >= 0, "Knockback cannot be negative");
        this.getHandle().setKnockbackStrength(knockbackStrength);
    }

    public int getKnockbackStrength() {
        return this.getHandle().knockbackStrength;
    }

    public boolean isCritical() {
        return this.getHandle().isCritical();
    }

    public void setCritical(boolean critical) {
        this.getHandle().setCritical(critical);
    }

    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof LivingEntity) {
            this.getHandle().shooter = ((CraftLivingEntity) shooter).getHandle();
        } else {
            this.getHandle().shooter = null;
        }

        this.getHandle().projectileSource = shooter;
    }

    public EntityArrow getHandle() {
        return (EntityArrow) this.entity;
    }

    public String toString() {
        return "CraftArrow";
    }

    public EntityType getType() {
        return EntityType.ARROW;
    }

    /** @deprecated */
    @Deprecated
    public LivingEntity getShooter() {
        return this.getHandle().shooter == null ? null : (LivingEntity) this.getHandle().shooter.getBukkitEntity();
    }

    /** @deprecated */
    @Deprecated
    public void setShooter(LivingEntity shooter) {
        this.getHandle().shooter = ((CraftLivingEntity) shooter).getHandle();
    }

    public Arrow.Spigot spigot() {
        return this.spigot;
    }
}
