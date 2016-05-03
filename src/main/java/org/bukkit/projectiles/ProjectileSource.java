package org.bukkit.projectiles;

import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public interface ProjectileSource {

    Projectile launchProjectile(Class oclass);

    Projectile launchProjectile(Class oclass, Vector vector);
}
