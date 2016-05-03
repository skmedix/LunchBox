package org.bukkit.entity;

import org.bukkit.Material;

public interface FallingBlock extends Entity {

    Material getMaterial();

    /** @deprecated */
    @Deprecated
    int getBlockId();

    /** @deprecated */
    @Deprecated
    byte getBlockData();

    boolean getDropItem();

    void setDropItem(boolean flag);

    boolean canHurtEntities();

    void setHurtEntities(boolean flag);
}
