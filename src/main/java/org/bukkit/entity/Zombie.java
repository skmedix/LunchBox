package org.bukkit.entity;

public interface Zombie extends Monster {

    boolean isBaby();

    void setBaby(boolean flag);

    boolean isVillager();

    void setVillager(boolean flag);
}
