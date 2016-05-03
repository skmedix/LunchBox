package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class ExtendedRails extends Rails {

    /** @deprecated */
    @Deprecated
    public ExtendedRails(int type) {
        super(type);
    }

    public ExtendedRails(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public ExtendedRails(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public ExtendedRails(Material type, byte data) {
        super(type, data);
    }

    public boolean isCurve() {
        return false;
    }

    /** @deprecated */
    @Deprecated
    protected byte getConvertedData() {
        return (byte) (this.getData() & 7);
    }

    public void setDirection(BlockFace face, boolean isOnSlope) {
        boolean extraBitSet = (this.getData() & 8) == 8;

        if (face != BlockFace.WEST && face != BlockFace.EAST && face != BlockFace.NORTH && face != BlockFace.SOUTH) {
            throw new IllegalArgumentException("Detector rails and powered rails cannot be set on a curve!");
        } else {
            super.setDirection(face, isOnSlope);
            this.setData((byte) (extraBitSet ? this.getData() | 8 : this.getData() & -9));
        }
    }

    public ExtendedRails clone() {
        return (ExtendedRails) super.clone();
    }
}
