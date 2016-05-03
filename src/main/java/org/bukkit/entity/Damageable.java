package org.bukkit.entity;

public interface Damageable extends Entity {

    void damage(double d0);

    /** @deprecated */
    @Deprecated
    void damage(int i);

    void damage(double d0, Entity entity);

    /** @deprecated */
    @Deprecated
    void damage(int i, Entity entity);

    double getHealth();

    /** @deprecated */
    @Deprecated
    int getHealth();

    void setHealth(double d0);

    /** @deprecated */
    @Deprecated
    void setHealth(int i);

    double getMaxHealth();

    /** @deprecated */
    @Deprecated
    int getMaxHealth();

    void setMaxHealth(double d0);

    /** @deprecated */
    @Deprecated
    void setMaxHealth(int i);

    void resetMaxHealth();
}
