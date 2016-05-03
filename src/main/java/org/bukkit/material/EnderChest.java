package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class EnderChest extends DirectionalContainer {

    public EnderChest() {
        super(Material.ENDER_CHEST);
    }

    public EnderChest(BlockFace direction) {
        this();
        this.setFacingDirection(direction);
    }

    /** @deprecated */
    @Deprecated
    public EnderChest(int type) {
        super(type);
    }

    public EnderChest(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public EnderChest(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public EnderChest(Material type, byte data) {
        super(type, data);
    }

    public EnderChest clone() {
        return (EnderChest) super.clone();
    }
}
