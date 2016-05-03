package org.bukkit.block;

import org.bukkit.projectiles.BlockProjectileSource;

public interface Dispenser extends BlockState, ContainerBlock {

    BlockProjectileSource getBlockProjectileSource();

    boolean dispense();
}
