package org.bukkit.entity;

import org.bukkit.DyeColor;

public interface Wolf extends Animals, Tameable {

    boolean isAngry();

    void setAngry(boolean flag);

    boolean isSitting();

    void setSitting(boolean flag);

    DyeColor getCollarColor();

    void setCollarColor(DyeColor dyecolor);
}
