package org.bukkit.material;

import java.util.Arrays;
import java.util.EnumSet;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Vine extends MaterialData {

    private static final int VINE_NORTH = 4;
    private static final int VINE_EAST = 8;
    private static final int VINE_WEST = 2;
    private static final int VINE_SOUTH = 1;
    EnumSet possibleFaces;
    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    public Vine() {
        super(Material.VINE);
        this.possibleFaces = EnumSet.of(BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST);
    }

    /** @deprecated */
    @Deprecated
    public Vine(int type, byte data) {
        super(type, data);
        this.possibleFaces = EnumSet.of(BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST);
    }

    /** @deprecated */
    @Deprecated
    public Vine(byte data) {
        super(Material.VINE, data);
        this.possibleFaces = EnumSet.of(BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST);
    }

    public Vine(BlockFace... faces) {
        this(EnumSet.copyOf(Arrays.asList(faces)));
    }

    public Vine(EnumSet faces) {
        this((byte) 0);
        faces.retainAll(this.possibleFaces);
        byte data = 0;

        if (faces.contains(BlockFace.WEST)) {
            data = (byte) (data | 2);
        }

        if (faces.contains(BlockFace.NORTH)) {
            data = (byte) (data | 4);
        }

        if (faces.contains(BlockFace.SOUTH)) {
            data = (byte) (data | 1);
        }

        if (faces.contains(BlockFace.EAST)) {
            data = (byte) (data | 8);
        }

        this.setData(data);
    }

    public boolean isOnFace(BlockFace face) {
        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
        case 1:
            if ((this.getData() & 4) == 4) {
                return true;
            }

            return false;

        case 2:
            if ((this.getData() & 8) == 8) {
                return true;
            }

            return false;

        case 3:
            if ((this.getData() & 1) == 1) {
                return true;
            }

            return false;

        case 4:
            if ((this.getData() & 2) == 2) {
                return true;
            }

            return false;

        case 5:
            return true;

        case 6:
        default:
            return false;

        case 7:
            if (this.isOnFace(BlockFace.EAST) && this.isOnFace(BlockFace.NORTH)) {
                return true;
            }

            return false;

        case 8:
            if (this.isOnFace(BlockFace.WEST) && this.isOnFace(BlockFace.NORTH)) {
                return true;
            }

            return false;

        case 9:
            if (this.isOnFace(BlockFace.EAST) && this.isOnFace(BlockFace.SOUTH)) {
                return true;
            }

            return false;

        case 10:
            return this.isOnFace(BlockFace.WEST) && this.isOnFace(BlockFace.SOUTH);
        }
    }

    public void putOnFace(BlockFace face) {
        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
        case 1:
            this.setData((byte) (this.getData() | 4));
            break;

        case 2:
            this.setData((byte) (this.getData() | 8));
            break;

        case 3:
            this.setData((byte) (this.getData() | 1));
            break;

        case 4:
            this.setData((byte) (this.getData() | 2));

        case 5:
            break;

        case 6:
        default:
            throw new IllegalArgumentException("Vines can\'t go on face " + face.toString());

        case 7:
            this.putOnFace(BlockFace.EAST);
            this.putOnFace(BlockFace.NORTH);
            break;

        case 8:
            this.putOnFace(BlockFace.WEST);
            this.putOnFace(BlockFace.NORTH);
            break;

        case 9:
            this.putOnFace(BlockFace.EAST);
            this.putOnFace(BlockFace.SOUTH);
            break;

        case 10:
            this.putOnFace(BlockFace.WEST);
            this.putOnFace(BlockFace.SOUTH);
        }

    }

    public void removeFromFace(BlockFace face) {
        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
        case 1:
            this.setData((byte) (this.getData() & -5));
            break;

        case 2:
            this.setData((byte) (this.getData() & -9));
            break;

        case 3:
            this.setData((byte) (this.getData() & -2));
            break;

        case 4:
            this.setData((byte) (this.getData() & -3));

        case 5:
            break;

        case 6:
        default:
            throw new IllegalArgumentException("Vines can\'t go on face " + face.toString());

        case 7:
            this.removeFromFace(BlockFace.EAST);
            this.removeFromFace(BlockFace.NORTH);
            break;

        case 8:
            this.removeFromFace(BlockFace.WEST);
            this.removeFromFace(BlockFace.NORTH);
            break;

        case 9:
            this.removeFromFace(BlockFace.EAST);
            this.removeFromFace(BlockFace.SOUTH);
            break;

        case 10:
            this.removeFromFace(BlockFace.WEST);
            this.removeFromFace(BlockFace.SOUTH);
        }

    }

    public String toString() {
        return "VINE";
    }

    public Vine clone() {
        return (Vine) super.clone();
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = Vine.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (Vine.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
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

            Vine.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }
}
