package org.bukkit.entity;

import org.bukkit.util.Vector;

public interface Vehicle extends Entity {

    Vector getVelocity();

    void setVelocity(Vector vector);
}
