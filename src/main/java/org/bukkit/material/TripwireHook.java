package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class TripwireHook extends SimpleAttachableMaterialData implements Redstone {

    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    public TripwireHook() {
        super(Material.TRIPWIRE_HOOK);
    }

    /** @deprecated */
    @Deprecated
    public TripwireHook(int type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public TripwireHook(int type, byte data) {
        super(type, data);
    }

    public TripwireHook(BlockFace dir) {
        this();
        this.setFacingDirection(dir);
    }

    public boolean isConnected() {
        return (this.getData() & 4) != 0;
    }

    public void setConnected(boolean connected) {
        int dat = this.getData() & 11;

        if (connected) {
            dat |= 4;
        }

        this.setData((byte) dat);
    }

    public boolean isActivated() {
        return (this.getData() & 8) != 0;
    }

    public void setActivated(boolean act) {
        int dat = this.getData() & 7;

        if (act) {
            dat |= 8;
        }

        this.setData((byte) dat);
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

    public BlockFace getAttachedFace() {
        switch (this.getData() & 3) {
        case 0:
            return BlockFace.NORTH;

        case 1:
            return BlockFace.EAST;

        case 2:
            return BlockFace.SOUTH;

        case 3:
            return BlockFace.WEST;

        default:
            return null;
        }
    }

    public boolean isPowered() {
        return this.isActivated();
    }

    public TripwireHook clone() {
        return (TripwireHook) super.clone();
    }

    public String toString() {
        return super.toString() + " facing " + this.getFacing() + (this.isActivated() ? " Activated" : "") + (this.isConnected() ? " Connected" : "");
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = TripwireHook.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (TripwireHook.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
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

            TripwireHook.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }
}
