package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class CocoaPlant extends MaterialData implements Directional, Attachable {

    private static int[] $SWITCH_TABLE$org$bukkit$material$CocoaPlant$CocoaPlantSize;
    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    public CocoaPlant() {
        super(Material.COCOA);
    }

    /** @deprecated */
    @Deprecated
    public CocoaPlant(int type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public CocoaPlant(int type, byte data) {
        super(type, data);
    }

    public CocoaPlant(CocoaPlant.CocoaPlantSize sz) {
        this();
        this.setSize(sz);
    }

    public CocoaPlant(CocoaPlant.CocoaPlantSize sz, BlockFace dir) {
        this();
        this.setSize(sz);
        this.setFacingDirection(dir);
    }

    public CocoaPlant.CocoaPlantSize getSize() {
        switch (this.getData() & 12) {
        case 0:
            return CocoaPlant.CocoaPlantSize.SMALL;

        case 1:
        case 2:
        case 3:
        default:
            return CocoaPlant.CocoaPlantSize.LARGE;

        case 4:
            return CocoaPlant.CocoaPlantSize.MEDIUM;
        }
    }

    public void setSize(CocoaPlant.CocoaPlantSize sz) {
        int dat = this.getData() & 3;

        switch ($SWITCH_TABLE$org$bukkit$material$CocoaPlant$CocoaPlantSize()[sz.ordinal()]) {
        case 1:
        default:
            break;

        case 2:
            dat |= 4;
            break;

        case 3:
            dat |= 8;
        }

        this.setData((byte) dat);
    }

    public BlockFace getAttachedFace() {
        return this.getFacing().getOppositeFace();
    }

    public void setFacingDirection(BlockFace face) {
        int dat = this.getData() & 12;

        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
        case 1:
            dat |= 2;
            break;

        case 2:
            dat |= 3;

        case 3:
        default:
            break;

        case 4:
            dat |= 1;
        }

        this.setData((byte) dat);
    }

    public BlockFace getFacing() {
        switch (this.getData() & 3) {
        case 0:
            return BlockFace.SOUTH;

        case 1:
            return BlockFace.WEST;

        case 2:
            return BlockFace.NORTH;

        case 3:
            return BlockFace.EAST;

        default:
            return null;
        }
    }

    public CocoaPlant clone() {
        return (CocoaPlant) super.clone();
    }

    public String toString() {
        return super.toString() + " facing " + this.getFacing() + " " + this.getSize();
    }

    static int[] $SWITCH_TABLE$org$bukkit$material$CocoaPlant$CocoaPlantSize() {
        int[] aint = CocoaPlant.$SWITCH_TABLE$org$bukkit$material$CocoaPlant$CocoaPlantSize;

        if (CocoaPlant.$SWITCH_TABLE$org$bukkit$material$CocoaPlant$CocoaPlantSize != null) {
            return aint;
        } else {
            int[] aint1 = new int[CocoaPlant.CocoaPlantSize.values().length];

            try {
                aint1[CocoaPlant.CocoaPlantSize.LARGE.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[CocoaPlant.CocoaPlantSize.MEDIUM.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[CocoaPlant.CocoaPlantSize.SMALL.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            CocoaPlant.$SWITCH_TABLE$org$bukkit$material$CocoaPlant$CocoaPlantSize = aint1;
            return aint1;
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = CocoaPlant.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (CocoaPlant.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
            return aint;
        } else {
            int[] aint1 = new int[BlockFace.values().length];

            try {
                aint1[BlockFace.DOWN.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[BlockFace.EAST.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[BlockFace.EAST_NORTH_EAST.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[BlockFace.EAST_SOUTH_EAST.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[BlockFace.NORTH.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[BlockFace.NORTH_EAST.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[BlockFace.NORTH_NORTH_EAST.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[BlockFace.NORTH_NORTH_WEST.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[BlockFace.NORTH_WEST.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[BlockFace.SELF.ordinal()] = 19;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[BlockFace.SOUTH.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[BlockFace.SOUTH_EAST.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[BlockFace.SOUTH_SOUTH_EAST.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[BlockFace.SOUTH_SOUTH_WEST.ordinal()] = 17;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[BlockFace.SOUTH_WEST.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                aint1[BlockFace.UP.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            try {
                aint1[BlockFace.WEST.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror16) {
                ;
            }

            try {
                aint1[BlockFace.WEST_NORTH_WEST.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror17) {
                ;
            }

            try {
                aint1[BlockFace.WEST_SOUTH_WEST.ordinal()] = 18;
            } catch (NoSuchFieldError nosuchfielderror18) {
                ;
            }

            CocoaPlant.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }

    public static enum CocoaPlantSize {

        SMALL, MEDIUM, LARGE;
    }
}
