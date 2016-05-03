package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Stairs extends MaterialData implements Directional {

    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    /** @deprecated */
    @Deprecated
    public Stairs(int type) {
        super(type);
    }

    public Stairs(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Stairs(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Stairs(Material type, byte data) {
        super(type, data);
    }

    public BlockFace getAscendingDirection() {
        int data = this.getData();

        switch (data & 3) {
        case 0:
        default:
            return BlockFace.EAST;

        case 1:
            return BlockFace.WEST;

        case 2:
            return BlockFace.SOUTH;

        case 3:
            return BlockFace.NORTH;
        }
    }

    public BlockFace getDescendingDirection() {
        return this.getAscendingDirection().getOppositeFace();
    }

    public void setFacingDirection(BlockFace face) {
        byte data;

        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
        case 1:
            data = 3;
            break;

        case 2:
        default:
            data = 0;
            break;

        case 3:
            data = 2;
            break;

        case 4:
            data = 1;
        }

        this.setData((byte) (this.getData() & 12 | data));
    }

    public BlockFace getFacing() {
        return this.getDescendingDirection();
    }

    public boolean isInverted() {
        return (this.getData() & 4) != 0;
    }

    public void setInverted(boolean inv) {
        int dat = this.getData() & 3;

        if (inv) {
            dat |= 4;
        }

        this.setData((byte) dat);
    }

    public String toString() {
        return super.toString() + " facing " + this.getFacing() + (this.isInverted() ? " inverted" : "");
    }

    public Stairs clone() {
        return (Stairs) super.clone();
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = Stairs.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (Stairs.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
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

            Stairs.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }
}
