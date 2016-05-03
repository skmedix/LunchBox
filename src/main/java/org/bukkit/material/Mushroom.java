package org.bukkit.material;

import java.util.EnumSet;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Mushroom extends MaterialData {

    private static final byte SHROOM_NONE = 0;
    private static final byte SHROOM_STEM = 10;
    private static final byte NORTH_LIMIT = 4;
    private static final byte SOUTH_LIMIT = 6;
    private static final byte EAST_WEST_LIMIT = 3;
    private static final byte EAST_REMAINDER = 0;
    private static final byte WEST_REMAINDER = 1;
    private static final byte NORTH_SOUTH_MOD = 3;
    private static final byte EAST_WEST_MOD = 1;
    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    public Mushroom(Material shroom) {
        super(shroom);
        Validate.isTrue(shroom == Material.HUGE_MUSHROOM_1 || shroom == Material.HUGE_MUSHROOM_2, "Not a mushroom!");
    }

    /** @deprecated */
    @Deprecated
    public Mushroom(Material shroom, byte data) {
        super(shroom, data);
        Validate.isTrue(shroom == Material.HUGE_MUSHROOM_1 || shroom == Material.HUGE_MUSHROOM_2, "Not a mushroom!");
    }

    /** @deprecated */
    @Deprecated
    public Mushroom(int type, byte data) {
        super(type, data);
        Validate.isTrue(type == Material.HUGE_MUSHROOM_1.getId() || type == Material.HUGE_MUSHROOM_2.getId(), "Not a mushroom!");
    }

    public boolean isStem() {
        return this.getData() == 10;
    }

    public void setStem() {
        this.setData((byte) 10);
    }

    public boolean isFacePainted(BlockFace face) {
        byte data = this.getData();

        if (data != 0 && data != 10) {
            switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
            case 1:
                if (data % 3 == 0) {
                    return true;
                }

                return false;

            case 2:
                if (data > 6) {
                    return true;
                }

                return false;

            case 3:
                if (data % 3 == 1) {
                    return true;
                }

                return false;

            case 4:
                if (data < 4) {
                    return true;
                }

                return false;

            case 5:
                return true;

            default:
                return false;
            }
        } else {
            return false;
        }
    }

    public void setFacePainted(BlockFace face, boolean painted) {
        if (painted != this.isFacePainted(face)) {
            byte data = this.getData();

            if (data == 10) {
                data = 5;
            }

            switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
            case 1:
                if (painted) {
                    ++data;
                } else {
                    --data;
                }
                break;

            case 2:
                if (painted) {
                    data = (byte) (data + 3);
                } else {
                    data = (byte) (data - 3);
                }
                break;

            case 3:
                if (painted) {
                    --data;
                } else {
                    ++data;
                }
                break;

            case 4:
                if (painted) {
                    data = (byte) (data - 3);
                } else {
                    data = (byte) (data + 3);
                }
                break;

            case 5:
                if (!painted) {
                    data = 0;
                }
                break;

            default:
                throw new IllegalArgumentException("Can\'t paint that face of a mushroom!");
            }

            this.setData(data);
        }
    }

    public Set getPaintedFaces() {
        EnumSet faces = EnumSet.noneOf(BlockFace.class);

        if (this.isFacePainted(BlockFace.WEST)) {
            faces.add(BlockFace.WEST);
        }

        if (this.isFacePainted(BlockFace.NORTH)) {
            faces.add(BlockFace.NORTH);
        }

        if (this.isFacePainted(BlockFace.SOUTH)) {
            faces.add(BlockFace.SOUTH);
        }

        if (this.isFacePainted(BlockFace.EAST)) {
            faces.add(BlockFace.EAST);
        }

        if (this.isFacePainted(BlockFace.UP)) {
            faces.add(BlockFace.UP);
        }

        return faces;
    }

    public String toString() {
        return Material.getMaterial(this.getItemTypeId()).toString() + (this.isStem() ? "{STEM}" : this.getPaintedFaces());
    }

    public Mushroom clone() {
        return (Mushroom) super.clone();
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = Mushroom.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (Mushroom.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
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

            Mushroom.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }
}
