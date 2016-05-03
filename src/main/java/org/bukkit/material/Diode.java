package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Diode extends MaterialData implements Directional {

    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    public Diode() {
        super(Material.DIODE_BLOCK_ON);
    }

    /** @deprecated */
    @Deprecated
    public Diode(int type) {
        super(type);
    }

    public Diode(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Diode(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Diode(Material type, byte data) {
        super(type, data);
    }

    public void setDelay(int delay) {
        if (delay > 4) {
            delay = 4;
        }

        if (delay < 1) {
            delay = 1;
        }

        byte newData = (byte) (this.getData() & 3);

        this.setData((byte) (newData | delay - 1 << 2));
    }

    public int getDelay() {
        return (this.getData() >> 2) + 1;
    }

    public void setFacingDirection(BlockFace face) {
        int delay = this.getDelay();
        byte data;

        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
        case 1:
        default:
            data = 0;
            break;

        case 2:
            data = 1;
            break;

        case 3:
            data = 2;
            break;

        case 4:
            data = 3;
        }

        this.setData(data);
        this.setDelay(delay);
    }

    public BlockFace getFacing() {
        byte data = (byte) (this.getData() & 3);

        switch (data) {
        case 0:
        default:
            return BlockFace.NORTH;

        case 1:
            return BlockFace.EAST;

        case 2:
            return BlockFace.SOUTH;

        case 3:
            return BlockFace.WEST;
        }
    }

    public String toString() {
        return super.toString() + " facing " + this.getFacing() + " with " + this.getDelay() + " ticks delay";
    }

    public Diode clone() {
        return (Diode) super.clone();
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = Diode.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (Diode.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
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

            Diode.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }
}
