package org.bukkit.entity;

import org.bukkit.projectiles.ProjectileSource;

public interface Projectile extends Entity {

    /** @deprecated */
    @Deprecated
    LivingEntity getShooter();

    ProjectileSource getShooter();

    /** @deprecated */
    @Deprecated
    void setShooter(LivingEntity livingentity);

    void setShooter(ProjectileSource projectilesource);

    boolean doesBounce();

    void setBounce(boolean flag);
}
