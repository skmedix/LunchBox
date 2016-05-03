package org.bukkit.entity;

public interface Explosive extends Entity {

    void setYield(float f);

    float getYield();

    void setIsIncendiary(boolean flag);

    boolean isIncendiary();
}
