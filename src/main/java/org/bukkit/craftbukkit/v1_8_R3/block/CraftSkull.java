package org.bukkit.craftbukkit.v1_8_R3.block;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.TileEntitySkull;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class CraftSkull extends CraftBlockState implements Skull {

    private static final int MAX_OWNER_LENGTH = 16;
    private final TileEntitySkull skull;
    private GameProfile profile;
    private SkullType skullType;
    private byte rotation;
    private static int[] $SWITCH_TABLE$org$bukkit$SkullType;
    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;

    public CraftSkull(Block block) {
        super(block);
        CraftWorld world = (CraftWorld) block.getWorld();

        this.skull = (TileEntitySkull) world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
        this.profile = this.skull.getGameProfile();
        this.skullType = getSkullType(this.skull.getSkullType());
        this.rotation = (byte) this.skull.getRotation();
    }

    public CraftSkull(Material material, TileEntitySkull te) {
        super(material);
        this.skull = te;
        this.profile = this.skull.getGameProfile();
        this.skullType = getSkullType(this.skull.getSkullType());
        this.rotation = (byte) this.skull.getRotation();
    }

    static SkullType getSkullType(int id) {
        switch (id) {
        case 0:
        default:
            return SkullType.SKELETON;

        case 1:
            return SkullType.WITHER;

        case 2:
            return SkullType.ZOMBIE;

        case 3:
            return SkullType.PLAYER;

        case 4:
            return SkullType.CREEPER;
        }
    }

    static int getSkullType(SkullType type) {
        switch ($SWITCH_TABLE$org$bukkit$SkullType()[type.ordinal()]) {
        case 1:
        default:
            return 0;

        case 2:
            return 1;

        case 3:
            return 2;

        case 4:
            return 3;

        case 5:
            return 4;
        }
    }

    static byte getBlockFace(BlockFace rotation) {
        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[rotation.ordinal()]) {
        case 1:
            return (byte) 0;

        case 2:
            return (byte) 4;

        case 3:
            return (byte) 8;

        case 4:
            return (byte) 12;

        case 5:
        case 6:
        default:
            throw new IllegalArgumentException("Invalid BlockFace rotation: " + rotation);

        case 7:
            return (byte) 2;

        case 8:
            return (byte) 14;

        case 9:
            return (byte) 6;

        case 10:
            return (byte) 10;

        case 11:
            return (byte) 13;

        case 12:
            return (byte) 15;

        case 13:
            return (byte) 1;

        case 14:
            return (byte) 3;

        case 15:
            return (byte) 5;

        case 16:
            return (byte) 7;

        case 17:
            return (byte) 9;

        case 18:
            return (byte) 11;
        }
    }

    static BlockFace getBlockFace(byte rotation) {
        switch (rotation) {
        case 0:
            return BlockFace.NORTH;

        case 1:
            return BlockFace.NORTH_NORTH_EAST;

        case 2:
            return BlockFace.NORTH_EAST;

        case 3:
            return BlockFace.EAST_NORTH_EAST;

        case 4:
            return BlockFace.EAST;

        case 5:
            return BlockFace.EAST_SOUTH_EAST;

        case 6:
            return BlockFace.SOUTH_EAST;

        case 7:
            return BlockFace.SOUTH_SOUTH_EAST;

        case 8:
            return BlockFace.SOUTH;

        case 9:
            return BlockFace.SOUTH_SOUTH_WEST;

        case 10:
            return BlockFace.SOUTH_WEST;

        case 11:
            return BlockFace.WEST_SOUTH_WEST;

        case 12:
            return BlockFace.WEST;

        case 13:
            return BlockFace.WEST_NORTH_WEST;

        case 14:
            return BlockFace.NORTH_WEST;

        case 15:
            return BlockFace.NORTH_NORTH_WEST;

        default:
            throw new AssertionError(rotation);
        }
    }

    public boolean hasOwner() {
        return this.profile != null;
    }

    public String getOwner() {
        return this.hasOwner() ? this.profile.getName() : null;
    }

    public boolean setOwner(String name) {
        if (name != null && name.length() <= 16) {
            GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(name);

            if (profile == null) {
                return false;
            } else {
                if (this.skullType != SkullType.PLAYER) {
                    this.skullType = SkullType.PLAYER;
                }

                this.profile = profile;
                return true;
            }
        } else {
            return false;
        }
    }

    public BlockFace getRotation() {
        return getBlockFace(this.rotation);
    }

    public void setRotation(BlockFace rotation) {
        this.rotation = getBlockFace(rotation);
    }

    public SkullType getSkullType() {
        return this.skullType;
    }

    public void setSkullType(SkullType skullType) {
        this.skullType = skullType;
        if (skullType != SkullType.PLAYER) {
            this.profile = null;
        }

    }

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            if (this.skullType == SkullType.PLAYER) {
                this.skull.setGameProfile(this.profile);
            } else {
                this.skull.setSkullType(getSkullType(this.skullType));
            }

            this.skull.setRotation(this.rotation);
            this.skull.update();
        }

        return result;
    }

    public TileEntitySkull getTileEntity() {
        return this.skull;
    }

    static int[] $SWITCH_TABLE$org$bukkit$SkullType() {
        int[] aint = CraftSkull.$SWITCH_TABLE$org$bukkit$SkullType;

        if (CraftSkull.$SWITCH_TABLE$org$bukkit$SkullType != null) {
            return aint;
        } else {
            int[] aint1 = new int[SkullType.values().length];

            try {
                aint1[SkullType.CREEPER.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[SkullType.PLAYER.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[SkullType.SKELETON.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[SkullType.WITHER.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[SkullType.ZOMBIE.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            CraftSkull.$SWITCH_TABLE$org$bukkit$SkullType = aint1;
            return aint1;
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = CraftSkull.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (CraftSkull.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
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

            CraftSkull.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }
}
