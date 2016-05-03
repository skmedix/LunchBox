package org.bukkit.entity;

import org.bukkit.block.BlockFace;
import org.bukkit.material.Attachable;

public interface Hanging extends Entity, Attachable {

    boolean setFacingDirection(BlockFace blockface, boolean flag);
}
