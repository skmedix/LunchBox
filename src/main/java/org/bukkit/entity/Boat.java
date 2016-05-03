package org.bukkit.entity;

public interface Boat extends Vehicle {

    double getMaxSpeed();

    void setMaxSpeed(double d0);

    double getOccupiedDeceleration();

    void setOccupiedDeceleration(double d0);

    double getUnoccupiedDeceleration();

    void setUnoccupiedDeceleration(double d0);

    boolean getWorkOnLand();

    void setWorkOnLand(boolean flag);
}
