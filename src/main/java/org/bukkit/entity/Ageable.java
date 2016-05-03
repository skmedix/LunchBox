package org.bukkit.entity;

public interface Ageable extends Creature {

    int getAge();

    void setAge(int i);

    void setAgeLock(boolean flag);

    boolean getAgeLock();

    void setBaby();

    void setAdult();

    boolean isAdult();

    boolean canBreed();

    void setBreed(boolean flag);
}
