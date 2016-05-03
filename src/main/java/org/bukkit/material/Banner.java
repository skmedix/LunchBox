package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Banner extends MaterialData implements Attachable {

    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    public Banner() {
        super(Material.BANNER);
    }

    public Banner(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Banner(int type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public Banner(Material type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public Banner(int type, byte data) {
        super(type, data);
    }

    public boolean isWallBanner() {
        return this.getItemType() == Material.WALL_BANNER;
    }

    public BlockFace getAttachedFace() {
        if (this.isWallBanner()) {
            byte data = this.getData();

            switch (data) {
            case 2:
                return BlockFace.SOUTH;

            case 3:
                return BlockFace.NORTH;

            case 4:
                return BlockFace.EAST;

            case 5:
                return BlockFace.WEST;

            default:
                return null;
            }
        } else {
            return BlockFace.DOWN;
        }
    }

    public BlockFace getFacing() {
        byte data = this.getData();

        if (!this.isWallBanner()) {
            switch (data) {
            case 0:
                return BlockFace.SOUTH;

            case 1:
                return BlockFace.SOUTH_SOUTH_WEST;

            case 2:
                return BlockFace.SOUTH_WEST;

            case 3:
                return BlockFace.WEST_SOUTH_WEST;

            case 4:
                return BlockFace.WEST;

            case 5:
                return BlockFace.WEST_NORTH_WEST;

            case 6:
                return BlockFace.NORTH_WEST;

            case 7:
                return BlockFace.NORTH_NORTH_WEST;

            case 8:
                return BlockFace.NORTH;

            case 9:
                return BlockFace.NORTH_NORTH_EAST;

            case 10:
                return BlockFace.NORTH_EAST;

            case 11:
                return BlockFace.EAST_NORTH_EAST;

            case 12:
                return BlockFace.EAST;

            case 13:
                return BlockFace.EAST_SOUTH_EAST;

            case 14:
                return BlockFace.SOUTH_EAST;

            case 15:
                return BlockFace.SOUTH_SOUTH_EAST;

            default:
                return null;
            }
        } else {
            return this.getAttachedFace().getOppositeFace();
        }
    }

    public void setFacingDirection(BlockFace face) {
        byte data;

        if (this.isWallBanner()) {
            switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
            case 1:
                data = 2;
                break;

            case 2:
            default:
                data = 5;
                break;

            case 3:
                data = 3;
                break;

            case 4:
                data = 4;
            }
        } else {
            switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
            case 1:
                data = 8;
                break;

            case 2:
                data = 12;
                break;

            case 3:
                data = 0;
                break;

            case 4:
                data = 4;
                break;

            case 5:
            case 6:
            case 9:
            default:
                data = 14;
                break;

            case 7:
                data = 10;
                break;

            case 8:
                data = 6;
                break;

            case 10:
                data = 2;
                break;

            case 11:
                data = 5;
                break;

            case 12:
                data = 7;
                break;

            case 13:
                data = 9;
                break;

            case 14:
                data = 11;
                break;

            case 15:
                data = 13;
                break;

            case 16:
                data = 15;
                break;

            case 17:
                data = 1;
                break;

            case 18:
                data = 3;
            }
        }

        this.setData(data);
    }

    public String toString() {
        return super.toString() + " facing " + this.getFacing();
    }

    public Banner clone() {
        return (Banner) super.clone();
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = Banner.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (Banner.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
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

            Banner.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }
}
