package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Gate extends MaterialData implements Directional, Openable {

    private static final byte OPEN_BIT = 4;
    private static final byte DIR_BIT = 3;
    private static final byte GATE_SOUTH = 0;
    private static final byte GATE_WEST = 1;
    private static final byte GATE_NORTH = 2;
    private static final byte GATE_EAST = 3;
    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    public Gate() {
        super(Material.FENCE_GATE);
    }

    public Gate(int type, byte data) {
        super(type, data);
    }

    public Gate(byte data) {
        super(Material.FENCE_GATE, data);
    }

    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (this.getData() & -4);

        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
        case 1:
            data = (byte) (data | 3);
            break;

        case 2:
        default:
            data = (byte) (data | 0);
            break;

        case 3:
            data = (byte) (data | 1);
            break;

        case 4:
            data = (byte) (data | 2);
        }

        this.setData(data);
    }

    public BlockFace getFacing() {
        switch (this.getData() & 3) {
        case 0:
            return BlockFace.EAST;

        case 1:
            return BlockFace.SOUTH;

        case 2:
            return BlockFace.WEST;

        case 3:
            return BlockFace.NORTH;

        default:
            return BlockFace.EAST;
        }
    }

    public boolean isOpen() {
        return (this.getData() & 4) > 0;
    }

    public void setOpen(boolean isOpen) {
        byte data = this.getData();

        if (isOpen) {
            data = (byte) (data | 4);
        } else {
            data &= -5;
        }

        this.setData(data);
    }

    public String toString() {
        return (this.isOpen() ? "OPEN " : "CLOSED ") + " facing and opening " + this.getFacing();
    }

    public Gate clone() {
        return (Gate) super.clone();
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = Gate.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (Gate.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
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

            Gate.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }
}
