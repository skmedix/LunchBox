package org.bukkit.entity;

import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public interface Minecart extends Vehicle {

    /** @deprecated */
    @Deprecated
    void setDamage(int i);

    void setDamage(double d0);

    /** @deprecated */
    @Deprecated
    int getDamage();

    double getDamage();

    double getMaxSpeed();

    void setMaxSpeed(double d0);

    boolean isSlowWhenEmpty();

    void setSlowWhenEmpty(boolean flag);

    Vector getFlyingVelocityMod();

    void setFlyingVelocityMod(Vector vector);

    Vector getDerailedVelocityMod();

    void setDerailedVelocityMod(Vector vector);

    void setDisplayBlock(MaterialData materialdata);

    MaterialData getDisplayBlock();

    void setDisplayBlockOffset(int i);

    int getDisplayBlockOffset();
}
