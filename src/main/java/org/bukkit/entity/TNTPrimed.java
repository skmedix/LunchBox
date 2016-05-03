package org.bukkit.entity;

public interface TNTPrimed extends Explosive {

    void setFuseTicks(int i);

    int getFuseTicks();

    Entity getSource();
}
