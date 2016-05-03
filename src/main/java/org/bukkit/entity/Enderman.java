package org.bukkit.entity;

import org.bukkit.material.MaterialData;

public interface Enderman extends Monster {

    MaterialData getCarriedMaterial();

    void setCarriedMaterial(MaterialData materialdata);
}
