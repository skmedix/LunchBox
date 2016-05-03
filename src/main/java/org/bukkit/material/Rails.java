package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Rails extends MaterialData {

    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    public Rails() {
        super(Material.RAILS);
    }

    /** @deprecated */
    @Deprecated
    public Rails(int type) {
        super(type);
    }

    public Rails(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Rails(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Rails(Material type, byte data) {
        super(type, data);
    }

    public boolean isOnSlope() {
        byte d = this.getConvertedData();

        return d == 2 || d == 3 || d == 4 || d == 5;
    }

    public boolean isCurve() {
        byte d = this.getConvertedData();

        return d == 6 || d == 7 || d == 8 || d == 9;
    }

    public BlockFace getDirection() {
        byte d = this.getConvertedData();

        switch (d) {
        case 0:
        default:
            return BlockFace.SOUTH;

        case 1:
            return BlockFace.EAST;

        case 2:
            return BlockFace.EAST;

        case 3:
            return BlockFace.WEST;

        case 4:
            return BlockFace.NORTH;

        case 5:
            return BlockFace.SOUTH;

        case 6:
            return BlockFace.NORTH_WEST;

        case 7:
            return BlockFace.NORTH_EAST;

        case 8:
            return BlockFace.SOUTH_EAST;

        case 9:
            return BlockFace.SOUTH_WEST;
        }
    }

    public String toString() {
        return super.toString() + " facing " + this.getDirection() + (this.isCurve() ? " on a curve" : (this.isOnSlope() ? " on a slope" : ""));
    }

    /** @deprecated */
    @Deprecated
    protected byte getConvertedData() {
        return this.getData();
    }

    public void setDirection(BlockFace face, boolean isOnSlope) {
        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
        case 1:
            this.setData((byte) (isOnSlope ? 4 : 0));
            break;

        case 2:
            this.setData((byte) (isOnSlope ? 2 : 1));
            break;

        case 3:
            this.setData((byte) (isOnSlope ? 5 : 0));
            break;

        case 4:
            this.setData((byte) (isOnSlope ? 3 : 1));

        case 5:
        case 6:
        default:
            break;

        case 7:
            this.setData((byte) 7);
            break;

        case 8:
            this.setData((byte) 6);
            break;

        case 9:
            this.setData((byte) 8);
            break;

        case 10:
            this.setData((byte) 9);
        }

    }

    public Rails clone() {
        return (Rails) super.clone();
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = Rails.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (Rails.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
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

            Rails.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }
}
