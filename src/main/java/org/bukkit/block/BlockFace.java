package org.bukkit.block;

public enum BlockFace {

    NORTH(0, 0, -1), EAST(1, 0, 0), SOUTH(0, 0, 1), WEST(-1, 0, 0), UP(0, 1, 0), DOWN(0, -1, 0), NORTH_EAST(BlockFace.NORTH, BlockFace.EAST), NORTH_WEST(BlockFace.NORTH, BlockFace.WEST), SOUTH_EAST(BlockFace.SOUTH, BlockFace.EAST), SOUTH_WEST(BlockFace.SOUTH, BlockFace.WEST), WEST_NORTH_WEST(BlockFace.WEST, BlockFace.NORTH_WEST), NORTH_NORTH_WEST(BlockFace.NORTH, BlockFace.NORTH_WEST), NORTH_NORTH_EAST(BlockFace.NORTH, BlockFace.NORTH_EAST), EAST_NORTH_EAST(BlockFace.EAST, BlockFace.NORTH_EAST), EAST_SOUTH_EAST(BlockFace.EAST, BlockFace.SOUTH_EAST), SOUTH_SOUTH_EAST(BlockFace.SOUTH, BlockFace.SOUTH_EAST), SOUTH_SOUTH_WEST(BlockFace.SOUTH, BlockFace.SOUTH_WEST), WEST_SOUTH_WEST(BlockFace.WEST, BlockFace.SOUTH_WEST), SELF(0, 0, 0);

    private final int modX;
    private final int modY;
    private final int modZ;
    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    private BlockFace(int modX, int modY, int modZ) {
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
    }

    private BlockFace(BlockFace face1, BlockFace face2) {
        this.modX = face1.getModX() + face2.getModX();
        this.modY = face1.getModY() + face2.getModY();
        this.modZ = face1.getModZ() + face2.getModZ();
    }

    public int getModX() {
        return this.modX;
    }

    public int getModY() {
        return this.modY;
    }

    public int getModZ() {
        return this.modZ;
    }

    public BlockFace getOppositeFace() {
        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[this.ordinal()]) {
        case 1:
            return BlockFace.SOUTH;

        case 2:
            return BlockFace.WEST;

        case 3:
            return BlockFace.NORTH;

        case 4:
            return BlockFace.EAST;

        case 5:
            return BlockFace.DOWN;

        case 6:
            return BlockFace.UP;

        case 7:
            return BlockFace.SOUTH_WEST;

        case 8:
            return BlockFace.SOUTH_EAST;

        case 9:
            return BlockFace.NORTH_WEST;

        case 10:
            return BlockFace.NORTH_EAST;

        case 11:
            return BlockFace.EAST_SOUTH_EAST;

        case 12:
            return BlockFace.SOUTH_SOUTH_EAST;

        case 13:
            return BlockFace.SOUTH_SOUTH_WEST;

        case 14:
            return BlockFace.WEST_SOUTH_WEST;

        case 15:
            return BlockFace.WEST_NORTH_WEST;

        case 16:
            return BlockFace.NORTH_NORTH_WEST;

        case 17:
            return BlockFace.NORTH_NORTH_EAST;

        case 18:
            return BlockFace.EAST_NORTH_EAST;

        case 19:
            return BlockFace.SELF;

        default:
            return BlockFace.SELF;
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = BlockFace.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (BlockFace.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
            return aint;
        } else {
            int[] aint1 = new int[values().length];

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

            BlockFace.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }
}
