package org.bukkit.entity;

import org.bukkit.material.Colorable;

public interface Sheep extends Animals, Colorable {

    boolean isSheared();

    void setSheared(boolean flag);
}
