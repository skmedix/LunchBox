package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;

public class CraftFish extends AbstractProjectile implements Fish {

    private double biteChance = -1.0D;

    public CraftFish(CraftServer server, EntityFishingHook entity) {
        super(server, entity);
    }

    public ProjectileSource getShooter() {
        return this.getHandle().owner != null ? this.getHandle().owner.getBukkitEntity() : null;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftHumanEntity) {
            this.getHandle().owner = (EntityHuman) ((CraftHumanEntity) shooter).entity;
        }

    }

    public EntityFishingHook getHandle() {
        return (EntityFishingHook) this.entity;
    }

    public String toString() {
        return "CraftFish";
    }

    public EntityType getType() {
        return EntityType.FISHING_HOOK;
    }

    public double getBiteChance() {
        EntityFishingHook hook = this.getHandle();

        return this.biteChance == -1.0D ? (hook.world.isRainingAt(new BlockPosition(MathHelper.floor(hook.locX), MathHelper.floor(hook.locY) + 1, MathHelper.floor(hook.locZ))) ? 0.0033333333333333335D : 0.002D) : this.biteChance;
    }

    public void setBiteChance(double chance) {
        Validate.isTrue(chance >= 0.0D && chance <= 1.0D, "The bite chance must be between 0 and 1.");
        this.biteChance = chance;
    }

    /** @deprecated */
    @Deprecated
    public LivingEntity getShooter() {
        return (LivingEntity) this.getShooter();
    }

    /** @deprecated */
    @Deprecated
    public void setShooter(LivingEntity shooter) {
        this.setShooter((ProjectileSource) shooter);
    }
}
