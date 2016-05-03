package org.bukkit.block;

import org.bukkit.SkullType;

public interface Skull extends BlockState {

    boolean hasOwner();

    String getOwner();

    boolean setOwner(String s);

    BlockFace getRotation();

    void setRotation(BlockFace blockface);

    SkullType getSkullType();

    void setSkullType(SkullType skulltype);
}
