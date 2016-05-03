package org.bukkit.entity;

public interface PigZombie extends Zombie {

    int getAnger();

    void setAnger(int i);

    void setAngry(boolean flag);

    boolean isAngry();
}
