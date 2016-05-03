package org.bukkit.entity;

public interface FishHook extends Projectile {

    double getBiteChance();

    void setBiteChance(double d0) throws IllegalArgumentException;
}
