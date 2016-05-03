package org.bukkit.entity;

public interface Tameable {

    boolean isTamed();

    void setTamed(boolean flag);

    AnimalTamer getOwner();

    void setOwner(AnimalTamer animaltamer);
}
