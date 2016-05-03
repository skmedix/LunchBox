package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Chest extends DirectionalContainer {

    public Chest() {
        super(Material.CHEST);
    }

    public Chest(BlockFace direction) {
        this();
        this.setFacingDirection(direction);
    }

    /** @deprecated */
    @Deprecated
    public Chest(int type) {
        super(type);
    }

    public Chest(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Chest(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Chest(Material type, byte data) {
        super(type, data);
    }

    public Chest clone() {
        return (Chest) super.clone();
    }
}
