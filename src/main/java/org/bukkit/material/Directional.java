package org.bukkit.material;

import org.bukkit.block.BlockFace;

public interface Directional {

    void setFacingDirection(BlockFace blockface);

    BlockFace getFacing();
}
