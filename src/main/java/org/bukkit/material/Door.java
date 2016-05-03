package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Door extends MaterialData implements Directional, Openable {

    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    /** @deprecated */
    @Deprecated
    public Door() {
        super(Material.WOODEN_DOOR);
    }

    /** @deprecated */
    @Deprecated
    public Door(int type) {
        super(type);
    }

    public Door(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Door(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Door(Material type, byte data) {
        super(type, data);
    }

    public boolean isOpen() {
        return (this.getData() & 4) == 4;
    }

    public void setOpen(boolean isOpen) {
        this.setData((byte) (isOpen ? this.getData() | 4 : this.getData() & -5));
    }

    public boolean isTopHalf() {
        return (this.getData() & 8) == 8;
    }

    public void setTopHalf(boolean isTopHalf) {
        this.setData((byte) (isTopHalf ? this.getData() | 8 : this.getData() & -9));
    }

    /** @deprecated */
    @Deprecated
    public BlockFace getHingeCorner() {
        return BlockFace.SELF;
    }

    public String toString() {
        return (this.isTopHalf() ? "TOP" : "BOTTOM") + " half of " + super.toString();
    }

    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (this.getData() & 18);

        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
        case 1:
            data = (byte) (data | 1);
            break;

        case 2:
            data = (byte) (data | 2);
            break;

        case 3:
            data = (byte) (data | 3);
            break;

        case 4:
            data = (byte) (data | 0);
        }

        this.setData(data);
    }

    public BlockFace getFacing() {
        byte data = (byte) (this.getData() & 3);

        switch (data) {
        case 0:
            return BlockFace.WEST;

        case 1:
            return BlockFace.NORTH;

        case 2:
            return BlockFace.EAST;

        case 3:
            return BlockFace.SOUTH;

        default:
            return null;
        }
    }

    public boolean getHinge() {
        return (this.getData() & 1) == 1;
    }

    public void setHinge(boolean hinge) {
        this.setData((byte) (hinge ? this.getData() | 1 : this.getData() & -2));
    }

    public Door clone() {
        return (Door) super.clone();
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = Door.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (Door.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
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

            Door.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }
}
