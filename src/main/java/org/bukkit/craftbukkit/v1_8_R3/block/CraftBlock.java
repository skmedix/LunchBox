package org.bukkit.craftbukkit.v1_8_R3.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.server.v1_8_R3.BiomeGenBase;
import net.minecraft.server.v1_8_R3.BlockCocoa;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockRedstoneWire;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.EnumSkyBlock;
import net.minecraft.server.v1_8_R3.GameProfileSerializer;
import net.minecraft.server.v1_8_R3.IBlockState;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;

public class CraftBlock implements Block {

    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;
    private static final Biome[] BIOME_MAPPING = new Biome[BiomeGenBase.getBiomeGenArray().length];
    private static final BiomeGenBase[] BiomeGenBase_MAPPING = new BiomeGenBase[Biome.values().length];
    private static int[] $SWITCH_TABLE$net$minecraft$server$EnumDirection;
    private static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace;
    private static int[] $SWITCH_TABLE$org$bukkit$Material;

    static {
        CraftBlock.BIOME_MAPPING[BiomeGenBase.ocean.biomeID] = Biome.OCEAN;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.plains.biomeID] = Biome.PLAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.desert.biomeID] = Biome.DESERT;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.extremeHills.biomeID] = Biome.EXTREME_HILLS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.forest.biomeID] = Biome.FOREST;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.taiga.biomeID] = Biome.TAIGA;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.swampland.biomeID] = Biome.SWAMPLAND;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.river.biomeID] = Biome.RIVER;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.hell.biomeID] = Biome.HELL;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.sky.biomeID] = Biome.SKY;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.frozenOcean.biomeID] = Biome.FROZEN_OCEAN;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.frozenRiver.biomeID] = Biome.FROZEN_RIVER;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.icePlains.biomeID] = Biome.ICE_PLAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.iceMountains.biomeID] = Biome.ICE_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.mushroomIsland.biomeID] = Biome.MUSHROOM_ISLAND;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.mushroomIslandShore.biomeID] = Biome.MUSHROOM_SHORE;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.beach.biomeID] = Biome.BEACH;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.desertHills.biomeID] = Biome.DESERT_HILLS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.forestHills.biomeID] = Biome.FOREST_HILLS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.taigaHills.biomeID] = Biome.TAIGA_HILLS;
        //CraftBlock.BIOME_MAPPING[BiomeGenBase.SMALL_MOUNTAINS.biomeID] = Biome.SMALL_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.jungle.biomeID] = Biome.JUNGLE;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.jungleHills.biomeID] = Biome.JUNGLE_HILLS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.jungleEdge.biomeID] = Biome.JUNGLE_EDGE;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.deepOcean.biomeID] = Biome.DEEP_OCEAN;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.stoneBeach.biomeID] = Biome.STONE_BEACH;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.coldBeach.biomeID] = Biome.COLD_BEACH;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.birchForest.biomeID] = Biome.BIRCH_FOREST;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.birchForestHills.biomeID] = Biome.BIRCH_FOREST_HILLS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.roofedForest.biomeID] = Biome.ROOFED_FOREST;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.coldTaiga.biomeID] = Biome.COLD_TAIGA;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.coldTaigaHills.biomeID] = Biome.COLD_TAIGA_HILLS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.megaTaiga.biomeID] = Biome.MEGA_TAIGA;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.megaTaigaHills.biomeID] = Biome.MEGA_TAIGA_HILLS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.extremeHillsPlus.biomeID] = Biome.EXTREME_HILLS_PLUS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.savanna.biomeID] = Biome.SAVANNA;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.savannaPlateau.biomeID] = Biome.SAVANNA_PLATEAU;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.mesa.biomeID] = Biome.MESA;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.mesaPlateau_F.biomeID] = Biome.MESA_PLATEAU_FOREST;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.mesaPlateau.biomeID] = Biome.MESA_PLATEAU;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.plains.biomeID + 128] = Biome.SUNFLOWER_PLAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.desertHills.biomeID + 128] = Biome.DESERT_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.forest.biomeID + 128] = Biome.FLOWER_FOREST;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.taiga.biomeID + 128] = Biome.TAIGA_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.swampland.biomeID + 128] = Biome.SWAMPLAND_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.icePlains.biomeID + 128] = Biome.ICE_PLAINS_SPIKES;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.jungle.biomeID + 128] = Biome.JUNGLE_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.jungleEdge.biomeID + 128] = Biome.JUNGLE_EDGE_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.coldTaiga.biomeID + 128] = Biome.COLD_TAIGA_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.savanna.biomeID + 128] = Biome.SAVANNA_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.savannaPlateau.biomeID + 128] = Biome.SAVANNA_PLATEAU_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.mesa.biomeID + 128] = Biome.MESA_BRYCE;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.mesaPlateau_F.biomeID + 128] = Biome.MESA_PLATEAU_FOREST_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.mesaPlateau.biomeID + 128] = Biome.MESA_PLATEAU_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.birchForest.biomeID + 128] = Biome.BIRCH_FOREST_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.birchForestHills.biomeID + 128] = Biome.BIRCH_FOREST_HILLS_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.roofedForest.biomeID + 128] = Biome.ROOFED_FOREST_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.megaTaiga.biomeID + 128] = Biome.MEGA_SPRUCE_TAIGA;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.extremeHills.biomeID + 128] = Biome.EXTREME_HILLS_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.extremeHillsPlus.biomeID + 128] = Biome.EXTREME_HILLS_PLUS_MOUNTAINS;
        CraftBlock.BIOME_MAPPING[BiomeGenBase.megaTaiga.biomeID + 128] = Biome.MEGA_SPRUCE_TAIGA_HILLS;

        for (int i = 0; i < CraftBlock.BIOME_MAPPING.length; ++i) {
            if (BiomeGenBase.getBiome(i) != null && CraftBlock.BIOME_MAPPING[i] == null) {
                throw new IllegalArgumentException("Missing Biome mapping for BiomeGenBase[" + i + ", " + BiomeGenBase.getBiome(i) + "]");
            }

            if (CraftBlock.BIOME_MAPPING[i] != null) {
                CraftBlock.BiomeGenBase_MAPPING[CraftBlock.BIOME_MAPPING[i].ordinal()] = BiomeGenBase.getBiome(i);
            }
        }

    }

    public CraftBlock(CraftChunk chunk, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunk = chunk;
    }

    private net.minecraft.block.Block getNMSBlock() {
        return CraftMagicNumbers.getBlock(this);
    }

    private static net.minecraft.block.Block getNMSBlock(int type) {
        return CraftMagicNumbers.getBlock(type);
    }

    public World getWorld() {
        return this.chunk.getWorld();
    }

    public Location getLocation() {
        return new Location(this.getWorld(), (double) this.x, (double) this.y, (double) this.z);
    }

    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(this.getWorld());
            loc.setX((double) this.x);
            loc.setY((double) this.y);
            loc.setZ((double) this.z);
            loc.setYaw(0.0F);
            loc.setPitch(0.0F);
        }

        return loc;
    }

    public BlockVector getVector() {
        return new BlockVector(this.x, this.y, this.z);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Chunk getChunk() {
        return this.chunk;
    }

    public void setData(byte data) {
        this.setData(data, 3);
    }

    public void setData(byte data, boolean applyPhysics) {
        if (applyPhysics) {
            this.setData(data, 3);
        } else {
            this.setData(data, 2);
        }

    }

    private void setData(byte data, int flag) {
        net.minecraft.world.World world = this.chunk.getHandle().getWorld();
        BlockPos position = new BlockPos(this.x, this.y, this.z);
        IBlockState blockData = world.getBlockState(position);

        world.setBlockState(position, (IBlockState) blockData.getBlock(), flag);
    }

    public byte getData() {
        return (byte) chunk.getHandle().getBlock(this.x & 0xF, this.y & 0xFF, this.z & 0xF).getMetaFromState((IBlockState) this.getState());
    }

    public void setType(Material type) {
        this.setType(type, true);
    }

    public void setType(Material type, boolean applyPhysics) {
        this.setTypebiomeID(type.(), applyPhysics);
    }

    public boolean setTypebiomeID(int type) {
        return this.setTypebiomeID(type, true);
    }

    public boolean setTypebiomeID(int type, boolean applyPhysics) {
        net.minecraft.block.Block block = getNMSBlock(type);

        return this.setTypebiomeIDAndData(type, (byte) block.getMetaFromState((IBlockState) block), applyPhysics);
    }

    public boolean setTypebiomeIDAndData(int type, byte data, boolean applyPhysics) {
        IBlockState blockData = getNMSBlock(type).getStateFromMeta(type);
        BlockPos position = new BlockPos(this.x, this.y, this.z);

        if (applyPhysics) {
            return this.chunk.getHandle().getWorld().setBlockState(position, blockData, 3);
        } else {
            boolean success = this.chunk.getHandle().getWorld().setBlockState(position, blockData, 2);

            if (success) {
                this.chunk.getHandle().getWorld().notifyLightSet(position);
            }

            return success;
        }
    }

    public Material getType() {
        return Material.getMaterial(this.getTypebiomeID());
    }

    /** @deprecated */
    @Deprecated
    public int getTypebiomeID() {
        return CraftMagicNumbers.getbiomeID(this.chunk.getHandle().getType(new BlockPosition(this.x, this.y, this.z)));
    }

    public byte getLightLevel() {
        return (byte) this.chunk.getHandle().getWorld().getLightLevel(new BlockPosition(this.x, this.y, this.z));
    }

    public byte getLightFromSky() {
        return (byte) this.chunk.getHandle().getBrightness(EnumSkyBlock.SKY, new BlockPosition(this.x, this.y, this.z));
    }

    public byte getLightFromBlocks() {
        return (byte) this.chunk.getHandle().getBrightness(EnumSkyBlock.BLOCK, new BlockPosition(this.x, this.y, this.z));
    }

    public Block getFace(BlockFace face) {
        return this.getRelative(face, 1);
    }

    public Block getFace(BlockFace face, int distance) {
        return this.getRelative(face, distance);
    }

    public Block getRelative(int modX, int modY, int modZ) {
        return this.getWorld().getBlockAt(this.getX() + modX, this.getY() + modY, this.getZ() + modZ);
    }

    public Block getRelative(BlockFace face) {
        return this.getRelative(face, 1);
    }

    public Block getRelative(BlockFace face, int distance) {
        return this.getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    public BlockFace getFace(Block block) {
        BlockFace[] values = BlockFace.values();
        BlockFace[] ablockface = values;
        int i = values.length;

        for (int j = 0; j < i; ++j) {
            BlockFace face = ablockface[j];

            if (this.getX() + face.getModX() == block.getX() && this.getY() + face.getModY() == block.getY() && this.getZ() + face.getModZ() == block.getZ()) {
                return face;
            }
        }

        return null;
    }

    public String toString() {
        return "CraftBlock{chunk=" + this.chunk + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",type=" + this.getType() + ",data=" + this.getData() + '}';
    }

    public static BlockFace notchToBlockFace(EnumDirection notch) {
        if (notch == null) {
            return BlockFace.SELF;
        } else {
            switch ($SWITCH_TABLE$net$minecraft$server$EnumDirection()[notch.ordinal()]) {
            case 1:
                return BlockFace.DOWN;

            case 2:
                return BlockFace.UP;

            case 3:
                return BlockFace.NORTH;

            case 4:
                return BlockFace.SOUTH;

            case 5:
                return BlockFace.WEST;

            case 6:
                return BlockFace.EAST;

            default:
                return BlockFace.SELF;
            }
        }
    }

    public static EnumFacing blockFaceToNotch(BlockFace face) {
        switch ($SWITCH_TABLE$org$bukkit$block$BlockFace()[face.ordinal()]) {
        case 1:
            return EnumFacing.NORTH;

        case 2:
            return EnumFacing.EAST;

        case 3:
            return EnumFacing.SOUTH;

        case 4:
            return EnumFacing.WEST;

        case 5:
            return EnumFacing.UP;

        case 6:
            return EnumFacing.DOWN;

        default:
            return null;
        }
    }

    public BlockState getState() {
        Material material = this.getType();

        switch ($SWITCH_TABLE$org$bukkit$Material()[material.ordinal()]) {
        case 24:
            return new CraftDispenser(this);

        case 26:
            return new CraftNoteBlock(this);

        case 53:
            return new CraftCreatureSpawner(this);

        case 55:
        case 147:
            return new CraftChest(this);

        case 62:
        case 63:
            return new CraftFurnace(this);

        case 64:
        case 69:
        case 266:
            return new CraftSign(this);

        case 85:
            return new CraftJukebox(this);

        case 118:
            return new CraftBrewingStand(this);

        case 138:
            return new CraftCommandBlock(this);

        case 139:
            return new CraftBeacon(this);

        case 145:
            return new CraftSkull(this);

        case 155:
            return new CraftHopper(this);

        case 159:
            return new CraftDropper(this);

        case 177:
        case 178:
        case 368:
            return new CraftBanner(this);

        default:
            return new CraftBlockState(this);
        }
    }

    public Biome getBiome() {
        return this.getWorld().getBiome(this.x, this.z);
    }

    public void setBiome(Biome bio) {
        this.getWorld().setBiome(this.x, this.z, bio);
    }

    public static Biome BiomeGenBaseToBiome(BiomeGenBase base) {
        return base == null ? null : CraftBlock.BIOME_MAPPING[base.biomebiomeID];
    }

    public static BiomeGenBase biomeToBiomeGenBase(Biome bio) {
        return bio == null ? null : CraftBlock.BiomeGenBase_MAPPING[bio.ordinal()];
    }

    public double getTemperature() {
        return this.getWorld().getTemperature(this.x, this.z);
    }

    public double getHumbiomeIDity() {
        return this.getWorld().getHumbiomeIDity(this.x, this.z);
    }

    public boolean isBlockPowered() {
        return this.chunk.getHandle().getWorld().getBlockPower(new BlockPosition(this.x, this.y, this.z)) > 0;
    }

    public boolean isBlockIndirectlyPowered() {
        return this.chunk.getHandle().getWorld().isBlockIndirectlyPowered(new BlockPosition(this.x, this.y, this.z));
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CraftBlock)) {
            return false;
        } else {
            CraftBlock other = (CraftBlock) o;

            return this.x == other.x && this.y == other.y && this.z == other.z && this.getWorld().equals(other.getWorld());
        }
    }

    public int hashCode() {
        return this.y << 24 ^ this.x ^ this.z ^ this.getWorld().hashCode();
    }

    public boolean isBlockFacePowered(BlockFace face) {
        return this.chunk.getHandle().getWorld().isBlockFacePowered(new BlockPosition(this.x, this.y, this.z), blockFaceToNotch(face));
    }

    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        int power = this.chunk.getHandle().getWorld().getBlockFacePower(new BlockPosition(this.x, this.y, this.z), blockFaceToNotch(face));
        Block relative = this.getRelative(face);

        return relative.getType() == Material.REDSTONE_WIRE ? Math.max(power, relative.getData()) > 0 : power > 0;
    }

    public int getBlockPower(BlockFace face) {
        int power = 0;
        BlockRedstoneWire wire = Blocks.REDSTONE_WIRE;
        net.minecraft.server.v1_8_R3.World world = this.chunk.getHandle().getWorld();

        if ((face == BlockFace.DOWN || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(this.x, this.y - 1, this.z), EnumDirection.DOWN)) {
            power = wire.getPower(world, new BlockPosition(this.x, this.y - 1, this.z), power);
        }

        if ((face == BlockFace.UP || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(this.x, this.y + 1, this.z), EnumDirection.UP)) {
            power = wire.getPower(world, new BlockPosition(this.x, this.y + 1, this.z), power);
        }

        if ((face == BlockFace.EAST || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(this.x + 1, this.y, this.z), EnumDirection.EAST)) {
            power = wire.getPower(world, new BlockPosition(this.x + 1, this.y, this.z), power);
        }

        if ((face == BlockFace.WEST || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(this.x - 1, this.y, this.z), EnumDirection.WEST)) {
            power = wire.getPower(world, new BlockPosition(this.x - 1, this.y, this.z), power);
        }

        if ((face == BlockFace.NORTH || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(this.x, this.y, this.z - 1), EnumDirection.NORTH)) {
            power = wire.getPower(world, new BlockPosition(this.x, this.y, this.z - 1), power);
        }

        if ((face == BlockFace.SOUTH || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(this.x, this.y, this.z + 1), EnumDirection.SOUTH)) {
            power = wire.getPower(world, new BlockPosition(this.x, this.y, this.z - 1), power);
        }

        int i;

        if (power > 0) {
            i = power;
        } else {
            label87: {
                if (face == BlockFace.SELF) {
                    if (!this.isBlockIndirectlyPowered()) {
                        break label87;
                    }
                } else if (!this.isBlockFaceIndirectlyPowered(face)) {
                    break label87;
                }

                i = 15;
                return i;
            }

            i = 0;
        }

        return i;
    }

    public int getBlockPower() {
        return this.getBlockPower(BlockFace.SELF);
    }

    public boolean isEmpty() {
        return this.getType() == Material.AIR;
    }

    public boolean isLiqubiomeID() {
        return this.getType() == Material.WATER || this.getType() == Material.STATIONARY_WATER || this.getType() == Material.LAVA || this.getType() == Material.STATIONARY_LAVA;
    }

    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getBybiomeID(this.getNMSBlock().getMaterial().getPushReaction());
    }

    private boolean itemCausesDrops(ItemStack item) {
        net.minecraft.server.v1_8_R3.Block block = this.getNMSBlock();
        Item itemType = item != null ? Item.getBybiomeID(item.getTypebiomeID()) : null;

        return block != null && (block.getMaterial().isAlwaysDestroyable() || itemType != null && itemType.canDestroySpecialBlock(block));
    }

    public boolean breakNaturally() {
        net.minecraft.server.v1_8_R3.Block block = this.getNMSBlock();
        byte data = this.getData();
        boolean result = false;

        if (block != null && block != Blocks.AIR) {
            block.dropNaturally(this.chunk.getHandle().getWorld(), new BlockPosition(this.x, this.y, this.z), block.fromLegacyData(data), 1.0F, 0);
            result = true;
        }

        this.setTypebiomeID(Material.AIR.getbiomeID());
        return result;
    }

    public boolean breakNaturally(ItemStack item) {
        return this.itemCausesDrops(item) ? this.breakNaturally() : this.setTypebiomeID(Material.AIR.getbiomeID());
    }

    public Collection getDrops() {
        ArrayList drops = new ArrayList();
        net.minecraft.server.v1_8_R3.Block block = this.getNMSBlock();

        if (block != Blocks.AIR) {
            byte data = this.getData();
            int count = block.getDropCount(0, this.chunk.getHandle().getWorld().random);

            for (int i = 0; i < count; ++i) {
                Item item = block.getDropType(block.fromLegacyData(data), this.chunk.getHandle().getWorld().random, 0);

                if (item != null) {
                    if (Blocks.SKULL == block) {
                        net.minecraft.server.v1_8_R3.ItemStack age = new net.minecraft.server.v1_8_R3.ItemStack(item, 1, block.getDropData(this.chunk.getHandle().getWorld(), new BlockPosition(this.x, this.y, this.z)));
                        TileEntitySkull dropAmount = (TileEntitySkull) this.chunk.getHandle().getWorld().getTileEntity(new BlockPosition(this.x, this.y, this.z));

                        if (dropAmount.getSkullType() == 3 && dropAmount.getGameProfile() != null) {
                            age.setTag(new NBTTagCompound());
                            NBTTagCompound j = new NBTTagCompound();

                            GameProfileSerializer.serialize(j, dropAmount.getGameProfile());
                            age.getTag().set("SkullOwner", j);
                        }

                        drops.add(CraftItemStack.asBukkitCopy(age));
                    } else if (Blocks.COCOA == block) {
                        int i = ((Integer) block.fromLegacyData(data).get(BlockCocoa.AGE)).intValue();
                        int j = i >= 2 ? 3 : 1;

                        for (int k = 0; k < j; ++k) {
                            drops.add(new ItemStack(Material.INK_SACK, 1, (short) 3));
                        }
                    } else {
                        drops.add(new ItemStack(CraftMagicNumbers.getMaterial(item), 1, (short) block.getDropData(block.fromLegacyData(data))));
                    }
                }
            }
        }

        return drops;
    }

    public Collection getDrops(ItemStack item) {
        return (Collection) (this.itemCausesDrops(item) ? this.getDrops() : Collections.emptyList());
    }

    public vobiomeID setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.chunk.getCraftWorld().getBlockMetadata().setMetadata((Block) this, metadataKey, newMetadataValue);
    }

    public List getMetadata(String metadataKey) {
        return this.chunk.getCraftWorld().getBlockMetadata().getMetadata((Block) this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return this.chunk.getCraftWorld().getBlockMetadata().hasMetadata((Block) this, metadataKey);
    }

    public vobiomeID removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.chunk.getCraftWorld().getBlockMetadata().removeMetadata((Block) this, metadataKey, owningPlugin);
    }

    static int[] $SWITCH_TABLE$net$minecraft$server$EnumDirection() {
        int[] aint = CraftBlock.$SWITCH_TABLE$net$minecraft$server$EnumDirection;

        if (CraftBlock.$SWITCH_TABLE$net$minecraft$server$EnumDirection != null) {
            return aint;
        } else {
            int[] aint1 = new int[EnumDirection.values().length];

            try {
                aint1[EnumDirection.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[EnumDirection.EAST.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[EnumDirection.NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[EnumDirection.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[EnumDirection.UP.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[EnumDirection.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            CraftBlock.$SWITCH_TABLE$net$minecraft$server$EnumDirection = aint1;
            return aint1;
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$block$BlockFace() {
        int[] aint = CraftBlock.$SWITCH_TABLE$org$bukkit$block$BlockFace;

        if (CraftBlock.$SWITCH_TABLE$org$bukkit$block$BlockFace != null) {
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

            CraftBlock.$SWITCH_TABLE$org$bukkit$block$BlockFace = aint1;
            return aint1;
        }
    }

    static int[] $SWITCH_TABLE$org$bukkit$Material() {
        int[] aint = CraftBlock.$SWITCH_TABLE$org$bukkit$Material;

        if (CraftBlock.$SWITCH_TABLE$org$bukkit$Material != null) {
            return aint;
        } else {
            int[] aint1 = new int[Material.values().length];

            try {
                aint1[Material.ACACIA_DOOR.ordinal()] = 197;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[Material.ACACIA_DOOR_ITEM.ordinal()] = 372;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[Material.ACACIA_FENCE.ordinal()] = 193;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[Material.ACACIA_FENCE_GATE.ordinal()] = 188;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[Material.ACACIA_STAIRS.ordinal()] = 164;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[Material.ACTIVATOR_RAIL.ordinal()] = 158;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[Material.AIR.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[Material.ANVIL.ordinal()] = 146;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                aint1[Material.APPLE.ordinal()] = 203;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                aint1[Material.ARMOR_STAND.ordinal()] = 359;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                aint1[Material.ARROW.ordinal()] = 205;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                aint1[Material.BAKED_POTATO.ordinal()] = 336;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                aint1[Material.BANNER.ordinal()] = 368;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                aint1[Material.BARRIER.ordinal()] = 167;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                aint1[Material.BEACON.ordinal()] = 139;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                aint1[Material.BED.ordinal()] = 298;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            try {
                aint1[Material.BEDROCK.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror16) {
                ;
            }

            try {
                aint1[Material.BED_BLOCK.ordinal()] = 27;
            } catch (NoSuchFieldError nosuchfielderror17) {
                ;
            }

            try {
                aint1[Material.BIRCH_DOOR.ordinal()] = 195;
            } catch (NoSuchFieldError nosuchfielderror18) {
                ;
            }

            try {
                aint1[Material.BIRCH_DOOR_ITEM.ordinal()] = 370;
            } catch (NoSuchFieldError nosuchfielderror19) {
                ;
            }

            try {
                aint1[Material.BIRCH_FENCE.ordinal()] = 190;
            } catch (NoSuchFieldError nosuchfielderror20) {
                ;
            }

            try {
                aint1[Material.BIRCH_FENCE_GATE.ordinal()] = 185;
            } catch (NoSuchFieldError nosuchfielderror21) {
                ;
            }

            try {
                aint1[Material.BIRCH_WOOD_STAIRS.ordinal()] = 136;
            } catch (NoSuchFieldError nosuchfielderror22) {
                ;
            }

            try {
                aint1[Material.BLAZE_POWDER.ordinal()] = 320;
            } catch (NoSuchFieldError nosuchfielderror23) {
                ;
            }

            try {
                aint1[Material.BLAZE_ROD.ordinal()] = 312;
            } catch (NoSuchFieldError nosuchfielderror24) {
                ;
            }

            try {
                aint1[Material.BOAT.ordinal()] = 276;
            } catch (NoSuchFieldError nosuchfielderror25) {
                ;
            }

            try {
                aint1[Material.BONE.ordinal()] = 295;
            } catch (NoSuchFieldError nosuchfielderror26) {
                ;
            }

            try {
                aint1[Material.BOOK.ordinal()] = 283;
            } catch (NoSuchFieldError nosuchfielderror27) {
                ;
            }

            try {
                aint1[Material.BOOKSHELF.ordinal()] = 48;
            } catch (NoSuchFieldError nosuchfielderror28) {
                ;
            }

            try {
                aint1[Material.BOOK_AND_QUILL.ordinal()] = 329;
            } catch (NoSuchFieldError nosuchfielderror29) {
                ;
            }

            try {
                aint1[Material.BOW.ordinal()] = 204;
            } catch (NoSuchFieldError nosuchfielderror30) {
                ;
            }

            try {
                aint1[Material.BOWL.ordinal()] = 224;
            } catch (NoSuchFieldError nosuchfielderror31) {
                ;
            }

            try {
                aint1[Material.BREAD.ordinal()] = 240;
            } catch (NoSuchFieldError nosuchfielderror32) {
                ;
            }

            try {
                aint1[Material.BREWING_STAND.ordinal()] = 118;
            } catch (NoSuchFieldError nosuchfielderror33) {
                ;
            }

            try {
                aint1[Material.BREWING_STAND_ITEM.ordinal()] = 322;
            } catch (NoSuchFieldError nosuchfielderror34) {
                ;
            }

            try {
                aint1[Material.BRICK.ordinal()] = 46;
            } catch (NoSuchFieldError nosuchfielderror35) {
                ;
            }

            try {
                aint1[Material.BRICK_STAIRS.ordinal()] = 109;
            } catch (NoSuchFieldError nosuchfielderror36) {
                ;
            }

            try {
                aint1[Material.BROWN_MUSHROOM.ordinal()] = 40;
            } catch (NoSuchFieldError nosuchfielderror37) {
                ;
            }

            try {
                aint1[Material.BUCKET.ordinal()] = 268;
            } catch (NoSuchFieldError nosuchfielderror38) {
                ;
            }

            try {
                aint1[Material.BURNING_FURNACE.ordinal()] = 63;
            } catch (NoSuchFieldError nosuchfielderror39) {
                ;
            }

            try {
                aint1[Material.CACTUS.ordinal()] = 82;
            } catch (NoSuchFieldError nosuchfielderror40) {
                ;
            }

            try {
                aint1[Material.CAKE.ordinal()] = 297;
            } catch (NoSuchFieldError nosuchfielderror41) {
                ;
            }

            try {
                aint1[Material.CAKE_BLOCK.ordinal()] = 93;
            } catch (NoSuchFieldError nosuchfielderror42) {
                ;
            }

            try {
                aint1[Material.CARPET.ordinal()] = 172;
            } catch (NoSuchFieldError nosuchfielderror43) {
                ;
            }

            try {
                aint1[Material.CARROT.ordinal()] = 142;
            } catch (NoSuchFieldError nosuchfielderror44) {
                ;
            }

            try {
                aint1[Material.CARROT_ITEM.ordinal()] = 334;
            } catch (NoSuchFieldError nosuchfielderror45) {
                ;
            }

            try {
                aint1[Material.CARROT_STICK.ordinal()] = 341;
            } catch (NoSuchFieldError nosuchfielderror46) {
                ;
            }

            try {
                aint1[Material.CAULDRON.ordinal()] = 119;
            } catch (NoSuchFieldError nosuchfielderror47) {
                ;
            }

            try {
                aint1[Material.CAULDRON_ITEM.ordinal()] = 323;
            } catch (NoSuchFieldError nosuchfielderror48) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_BOOTS.ordinal()] = 248;
            } catch (NoSuchFieldError nosuchfielderror49) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_CHESTPLATE.ordinal()] = 246;
            } catch (NoSuchFieldError nosuchfielderror50) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_HELMET.ordinal()] = 245;
            } catch (NoSuchFieldError nosuchfielderror51) {
                ;
            }

            try {
                aint1[Material.CHAINMAIL_LEGGINGS.ordinal()] = 247;
            } catch (NoSuchFieldError nosuchfielderror52) {
                ;
            }

            try {
                aint1[Material.CHEST.ordinal()] = 55;
            } catch (NoSuchFieldError nosuchfielderror53) {
                ;
            }

            try {
                aint1[Material.CLAY.ordinal()] = 83;
            } catch (NoSuchFieldError nosuchfielderror54) {
                ;
            }

            try {
                aint1[Material.CLAY_BALL.ordinal()] = 280;
            } catch (NoSuchFieldError nosuchfielderror55) {
                ;
            }

            try {
                aint1[Material.CLAY_BRICK.ordinal()] = 279;
            } catch (NoSuchFieldError nosuchfielderror56) {
                ;
            }

            try {
                aint1[Material.COAL.ordinal()] = 206;
            } catch (NoSuchFieldError nosuchfielderror57) {
                ;
            }

            try {
                aint1[Material.COAL_BLOCK.ordinal()] = 174;
            } catch (NoSuchFieldError nosuchfielderror58) {
                ;
            }

            try {
                aint1[Material.COAL_ORE.ordinal()] = 17;
            } catch (NoSuchFieldError nosuchfielderror59) {
                ;
            }

            try {
                aint1[Material.COBBLESTONE.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror60) {
                ;
            }

            try {
                aint1[Material.COBBLESTONE_STAIRS.ordinal()] = 68;
            } catch (NoSuchFieldError nosuchfielderror61) {
                ;
            }

            try {
                aint1[Material.COBBLE_WALL.ordinal()] = 140;
            } catch (NoSuchFieldError nosuchfielderror62) {
                ;
            }

            try {
                aint1[Material.COCOA.ordinal()] = 128;
            } catch (NoSuchFieldError nosuchfielderror63) {
                ;
            }

            try {
                aint1[Material.COMMAND.ordinal()] = 138;
            } catch (NoSuchFieldError nosuchfielderror64) {
                ;
            }

            try {
                aint1[Material.COMMAND_MINECART.ordinal()] = 365;
            } catch (NoSuchFieldError nosuchfielderror65) {
                ;
            }

            try {
                aint1[Material.COMPASS.ordinal()] = 288;
            } catch (NoSuchFieldError nosuchfielderror66) {
                ;
            }

            try {
                aint1[Material.COOKED_BEEF.ordinal()] = 307;
            } catch (NoSuchFieldError nosuchfielderror67) {
                ;
            }

            try {
                aint1[Material.COOKED_CHICKEN.ordinal()] = 309;
            } catch (NoSuchFieldError nosuchfielderror68) {
                ;
            }

            try {
                aint1[Material.COOKED_FISH.ordinal()] = 293;
            } catch (NoSuchFieldError nosuchfielderror69) {
                ;
            }

            try {
                aint1[Material.COOKED_MUTTON.ordinal()] = 367;
            } catch (NoSuchFieldError nosuchfielderror70) {
                ;
            }

            try {
                aint1[Material.COOKED_RABBIT.ordinal()] = 355;
            } catch (NoSuchFieldError nosuchfielderror71) {
                ;
            }

            try {
                aint1[Material.COOKIE.ordinal()] = 300;
            } catch (NoSuchFieldError nosuchfielderror72) {
                ;
            }

            try {
                aint1[Material.CROPS.ordinal()] = 60;
            } catch (NoSuchFieldError nosuchfielderror73) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_DOOR.ordinal()] = 198;
            } catch (NoSuchFieldError nosuchfielderror74) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_DOOR_ITEM.ordinal()] = 373;
            } catch (NoSuchFieldError nosuchfielderror75) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_FENCE.ordinal()] = 192;
            } catch (NoSuchFieldError nosuchfielderror76) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_FENCE_GATE.ordinal()] = 187;
            } catch (NoSuchFieldError nosuchfielderror77) {
                ;
            }

            try {
                aint1[Material.DARK_OAK_STAIRS.ordinal()] = 165;
            } catch (NoSuchFieldError nosuchfielderror78) {
                ;
            }

            try {
                aint1[Material.DAYLIGHT_DETECTOR.ordinal()] = 152;
            } catch (NoSuchFieldError nosuchfielderror79) {
                ;
            }

            try {
                aint1[Material.DAYLIGHT_DETECTOR_INVERTED.ordinal()] = 179;
            } catch (NoSuchFieldError nosuchfielderror80) {
                ;
            }

            try {
                aint1[Material.DEAD_BUSH.ordinal()] = 33;
            } catch (NoSuchFieldError nosuchfielderror81) {
                ;
            }

            try {
                aint1[Material.DETECTOR_RAIL.ordinal()] = 29;
            } catch (NoSuchFieldError nosuchfielderror82) {
                ;
            }

            try {
                aint1[Material.DIAMOND.ordinal()] = 207;
            } catch (NoSuchFieldError nosuchfielderror83) {
                ;
            }

            try {
                aint1[Material.DIAMOND_AXE.ordinal()] = 222;
            } catch (NoSuchFieldError nosuchfielderror84) {
                ;
            }

            try {
                aint1[Material.DIAMOND_BARDING.ordinal()] = 362;
            } catch (NoSuchFieldError nosuchfielderror85) {
                ;
            }

            try {
                aint1[Material.DIAMOND_BLOCK.ordinal()] = 58;
            } catch (NoSuchFieldError nosuchfielderror86) {
                ;
            }

            try {
                aint1[Material.DIAMOND_BOOTS.ordinal()] = 256;
            } catch (NoSuchFieldError nosuchfielderror87) {
                ;
            }

            try {
                aint1[Material.DIAMOND_CHESTPLATE.ordinal()] = 254;
            } catch (NoSuchFieldError nosuchfielderror88) {
                ;
            }

            try {
                aint1[Material.DIAMOND_HELMET.ordinal()] = 253;
            } catch (NoSuchFieldError nosuchfielderror89) {
                ;
            }

            try {
                aint1[Material.DIAMOND_HOE.ordinal()] = 236;
            } catch (NoSuchFieldError nosuchfielderror90) {
                ;
            }

            try {
                aint1[Material.DIAMOND_LEGGINGS.ordinal()] = 255;
            } catch (NoSuchFieldError nosuchfielderror91) {
                ;
            }

            try {
                aint1[Material.DIAMOND_ORE.ordinal()] = 57;
            } catch (NoSuchFieldError nosuchfielderror92) {
                ;
            }

            try {
                aint1[Material.DIAMOND_PICKAXE.ordinal()] = 221;
            } catch (NoSuchFieldError nosuchfielderror93) {
                ;
            }

            try {
                aint1[Material.DIAMOND_SPADE.ordinal()] = 220;
            } catch (NoSuchFieldError nosuchfielderror94) {
                ;
            }

            try {
                aint1[Material.DIAMOND_SWORD.ordinal()] = 219;
            } catch (NoSuchFieldError nosuchfielderror95) {
                ;
            }

            try {
                aint1[Material.DIODE.ordinal()] = 299;
            } catch (NoSuchFieldError nosuchfielderror96) {
                ;
            }

            try {
                aint1[Material.DIODE_BLOCK_OFF.ordinal()] = 94;
            } catch (NoSuchFieldError nosuchfielderror97) {
                ;
            }

            try {
                aint1[Material.DIODE_BLOCK_ON.ordinal()] = 95;
            } catch (NoSuchFieldError nosuchfielderror98) {
                ;
            }

            try {
                aint1[Material.DIRT.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror99) {
                ;
            }

            try {
                aint1[Material.DISPENSER.ordinal()] = 24;
            } catch (NoSuchFieldError nosuchfielderror100) {
                ;
            }

            try {
                aint1[Material.DOUBLE_PLANT.ordinal()] = 176;
            } catch (NoSuchFieldError nosuchfielderror101) {
                ;
            }

            try {
                aint1[Material.DOUBLE_STEP.ordinal()] = 44;
            } catch (NoSuchFieldError nosuchfielderror102) {
                ;
            }

            try {
                aint1[Material.DOUBLE_STONE_SLAB2.ordinal()] = 182;
            } catch (NoSuchFieldError nosuchfielderror103) {
                ;
            }

            try {
                aint1[Material.DRAGON_EGG.ordinal()] = 123;
            } catch (NoSuchFieldError nosuchfielderror104) {
                ;
            }

            try {
                aint1[Material.DROPPER.ordinal()] = 159;
            } catch (NoSuchFieldError nosuchfielderror105) {
                ;
            }

            try {
                aint1[Material.EGG.ordinal()] = 287;
            } catch (NoSuchFieldError nosuchfielderror106) {
                ;
            }

            try {
                aint1[Material.EMERALD.ordinal()] = 331;
            } catch (NoSuchFieldError nosuchfielderror107) {
                ;
            }

            try {
                aint1[Material.EMERALD_BLOCK.ordinal()] = 134;
            } catch (NoSuchFieldError nosuchfielderror108) {
                ;
            }

            try {
                aint1[Material.EMERALD_ORE.ordinal()] = 130;
            } catch (NoSuchFieldError nosuchfielderror109) {
                ;
            }

            try {
                aint1[Material.EMPTY_MAP.ordinal()] = 338;
            } catch (NoSuchFieldError nosuchfielderror110) {
                ;
            }

            try {
                aint1[Material.ENCHANTED_BOOK.ordinal()] = 346;
            } catch (NoSuchFieldError nosuchfielderror111) {
                ;
            }

            try {
                aint1[Material.ENCHANTMENT_TABLE.ordinal()] = 117;
            } catch (NoSuchFieldError nosuchfielderror112) {
                ;
            }

            try {
                aint1[Material.ENDER_CHEST.ordinal()] = 131;
            } catch (NoSuchFieldError nosuchfielderror113) {
                ;
            }

            try {
                aint1[Material.ENDER_PEARL.ordinal()] = 311;
            } catch (NoSuchFieldError nosuchfielderror114) {
                ;
            }

            try {
                aint1[Material.ENDER_PORTAL.ordinal()] = 120;
            } catch (NoSuchFieldError nosuchfielderror115) {
                ;
            }

            try {
                aint1[Material.ENDER_PORTAL_FRAME.ordinal()] = 121;
            } catch (NoSuchFieldError nosuchfielderror116) {
                ;
            }

            try {
                aint1[Material.ENDER_STONE.ordinal()] = 122;
            } catch (NoSuchFieldError nosuchfielderror117) {
                ;
            }

            try {
                aint1[Material.EXPLOSIVE_MINECART.ordinal()] = 350;
            } catch (NoSuchFieldError nosuchfielderror118) {
                ;
            }

            try {
                aint1[Material.EXP_BOTTLE.ordinal()] = 327;
            } catch (NoSuchFieldError nosuchfielderror119) {
                ;
            }

            try {
                aint1[Material.EYE_OF_ENDER.ordinal()] = 324;
            } catch (NoSuchFieldError nosuchfielderror120) {
                ;
            }

            try {
                aint1[Material.FEATHER.ordinal()] = 231;
            } catch (NoSuchFieldError nosuchfielderror121) {
                ;
            }

            try {
                aint1[Material.FENCE.ordinal()] = 86;
            } catch (NoSuchFieldError nosuchfielderror122) {
                ;
            }

            try {
                aint1[Material.FENCE_GATE.ordinal()] = 108;
            } catch (NoSuchFieldError nosuchfielderror123) {
                ;
            }

            try {
                aint1[Material.FERMENTED_SPbiomeIDER_EYE.ordinal()] = 319;
            } catch (NoSuchFieldError nosuchfielderror124) {
                ;
            }

            try {
                aint1[Material.FIRE.ordinal()] = 52;
            } catch (NoSuchFieldError nosuchfielderror125) {
                ;
            }

            try {
                aint1[Material.FIREBALL.ordinal()] = 328;
            } catch (NoSuchFieldError nosuchfielderror126) {
                ;
            }

            try {
                aint1[Material.FIREWORK.ordinal()] = 344;
            } catch (NoSuchFieldError nosuchfielderror127) {
                ;
            }

            try {
                aint1[Material.FIREWORK_CHARGE.ordinal()] = 345;
            } catch (NoSuchFieldError nosuchfielderror128) {
                ;
            }

            try {
                aint1[Material.FISHING_ROD.ordinal()] = 289;
            } catch (NoSuchFieldError nosuchfielderror129) {
                ;
            }

            try {
                aint1[Material.FLINT.ordinal()] = 261;
            } catch (NoSuchFieldError nosuchfielderror130) {
                ;
            }

            try {
                aint1[Material.FLINT_AND_STEEL.ordinal()] = 202;
            } catch (NoSuchFieldError nosuchfielderror131) {
                ;
            }

            try {
                aint1[Material.FLOWER_POT.ordinal()] = 141;
            } catch (NoSuchFieldError nosuchfielderror132) {
                ;
            }

            try {
                aint1[Material.FLOWER_POT_ITEM.ordinal()] = 333;
            } catch (NoSuchFieldError nosuchfielderror133) {
                ;
            }

            try {
                aint1[Material.FURNACE.ordinal()] = 62;
            } catch (NoSuchFieldError nosuchfielderror134) {
                ;
            }

            try {
                aint1[Material.GHAST_TEAR.ordinal()] = 313;
            } catch (NoSuchFieldError nosuchfielderror135) {
                ;
            }

            try {
                aint1[Material.GLASS.ordinal()] = 21;
            } catch (NoSuchFieldError nosuchfielderror136) {
                ;
            }

            try {
                aint1[Material.GLASS_BOTTLE.ordinal()] = 317;
            } catch (NoSuchFieldError nosuchfielderror137) {
                ;
            }

            try {
                aint1[Material.GLOWING_REDSTONE_ORE.ordinal()] = 75;
            } catch (NoSuchFieldError nosuchfielderror138) {
                ;
            }

            try {
                aint1[Material.GLOWSTONE.ordinal()] = 90;
            } catch (NoSuchFieldError nosuchfielderror139) {
                ;
            }

            try {
                aint1[Material.GLOWSTONE_DUST.ordinal()] = 291;
            } catch (NoSuchFieldError nosuchfielderror140) {
                ;
            }

            try {
                aint1[Material.GOLDEN_APPLE.ordinal()] = 265;
            } catch (NoSuchFieldError nosuchfielderror141) {
                ;
            }

            try {
                aint1[Material.GOLDEN_CARROT.ordinal()] = 339;
            } catch (NoSuchFieldError nosuchfielderror142) {
                ;
            }

            try {
                aint1[Material.GOLD_AXE.ordinal()] = 229;
            } catch (NoSuchFieldError nosuchfielderror143) {
                ;
            }

            try {
                aint1[Material.GOLD_BARDING.ordinal()] = 361;
            } catch (NoSuchFieldError nosuchfielderror144) {
                ;
            }

            try {
                aint1[Material.GOLD_BLOCK.ordinal()] = 42;
            } catch (NoSuchFieldError nosuchfielderror145) {
                ;
            }

            try {
                aint1[Material.GOLD_BOOTS.ordinal()] = 260;
            } catch (NoSuchFieldError nosuchfielderror146) {
                ;
            }

            try {
                aint1[Material.GOLD_CHESTPLATE.ordinal()] = 258;
            } catch (NoSuchFieldError nosuchfielderror147) {
                ;
            }

            try {
                aint1[Material.GOLD_HELMET.ordinal()] = 257;
            } catch (NoSuchFieldError nosuchfielderror148) {
                ;
            }

            try {
                aint1[Material.GOLD_HOE.ordinal()] = 237;
            } catch (NoSuchFieldError nosuchfielderror149) {
                ;
            }

            try {
                aint1[Material.GOLD_INGOT.ordinal()] = 209;
            } catch (NoSuchFieldError nosuchfielderror150) {
                ;
            }

            try {
                aint1[Material.GOLD_LEGGINGS.ordinal()] = 259;
            } catch (NoSuchFieldError nosuchfielderror151) {
                ;
            }

            try {
                aint1[Material.GOLD_NUGGET.ordinal()] = 314;
            } catch (NoSuchFieldError nosuchfielderror152) {
                ;
            }

            try {
                aint1[Material.GOLD_ORE.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror153) {
                ;
            }

            try {
                aint1[Material.GOLD_PICKAXE.ordinal()] = 228;
            } catch (NoSuchFieldError nosuchfielderror154) {
                ;
            }

            try {
                aint1[Material.GOLD_PLATE.ordinal()] = 148;
            } catch (NoSuchFieldError nosuchfielderror155) {
                ;
            }

            try {
                aint1[Material.GOLD_RECORD.ordinal()] = 374;
            } catch (NoSuchFieldError nosuchfielderror156) {
                ;
            }

            try {
                aint1[Material.GOLD_SPADE.ordinal()] = 227;
            } catch (NoSuchFieldError nosuchfielderror157) {
                ;
            }

            try {
                aint1[Material.GOLD_SWORD.ordinal()] = 226;
            } catch (NoSuchFieldError nosuchfielderror158) {
                ;
            }

            try {
                aint1[Material.GRASS.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror159) {
                ;
            }

            try {
                aint1[Material.GRAVEL.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror160) {
                ;
            }

            try {
                aint1[Material.GREEN_RECORD.ordinal()] = 375;
            } catch (NoSuchFieldError nosuchfielderror161) {
                ;
            }

            try {
                aint1[Material.GRILLED_PORK.ordinal()] = 263;
            } catch (NoSuchFieldError nosuchfielderror162) {
                ;
            }

            try {
                aint1[Material.HARD_CLAY.ordinal()] = 173;
            } catch (NoSuchFieldError nosuchfielderror163) {
                ;
            }

            try {
                aint1[Material.HAY_BLOCK.ordinal()] = 171;
            } catch (NoSuchFieldError nosuchfielderror164) {
                ;
            }

            try {
                aint1[Material.HOPPER.ordinal()] = 155;
            } catch (NoSuchFieldError nosuchfielderror165) {
                ;
            }

            try {
                aint1[Material.HOPPER_MINECART.ordinal()] = 351;
            } catch (NoSuchFieldError nosuchfielderror166) {
                ;
            }

            try {
                aint1[Material.HUGE_MUSHROOM_1.ordinal()] = 100;
            } catch (NoSuchFieldError nosuchfielderror167) {
                ;
            }

            try {
                aint1[Material.HUGE_MUSHROOM_2.ordinal()] = 101;
            } catch (NoSuchFieldError nosuchfielderror168) {
                ;
            }

            try {
                aint1[Material.ICE.ordinal()] = 80;
            } catch (NoSuchFieldError nosuchfielderror169) {
                ;
            }

            try {
                aint1[Material.INK_SACK.ordinal()] = 294;
            } catch (NoSuchFieldError nosuchfielderror170) {
                ;
            }

            try {
                aint1[Material.IRON_AXE.ordinal()] = 201;
            } catch (NoSuchFieldError nosuchfielderror171) {
                ;
            }

            try {
                aint1[Material.IRON_BARDING.ordinal()] = 360;
            } catch (NoSuchFieldError nosuchfielderror172) {
                ;
            }

            try {
                aint1[Material.IRON_BLOCK.ordinal()] = 43;
            } catch (NoSuchFieldError nosuchfielderror173) {
                ;
            }

            try {
                aint1[Material.IRON_BOOTS.ordinal()] = 252;
            } catch (NoSuchFieldError nosuchfielderror174) {
                ;
            }

            try {
                aint1[Material.IRON_CHESTPLATE.ordinal()] = 250;
            } catch (NoSuchFieldError nosuchfielderror175) {
                ;
            }

            try {
                aint1[Material.IRON_DOOR.ordinal()] = 273;
            } catch (NoSuchFieldError nosuchfielderror176) {
                ;
            }

            try {
                aint1[Material.IRON_DOOR_BLOCK.ordinal()] = 72;
            } catch (NoSuchFieldError nosuchfielderror177) {
                ;
            }

            try {
                aint1[Material.IRON_FENCE.ordinal()] = 102;
            } catch (NoSuchFieldError nosuchfielderror178) {
                ;
            }

            try {
                aint1[Material.IRON_HELMET.ordinal()] = 249;
            } catch (NoSuchFieldError nosuchfielderror179) {
                ;
            }

            try {
                aint1[Material.IRON_HOE.ordinal()] = 235;
            } catch (NoSuchFieldError nosuchfielderror180) {
                ;
            }

            try {
                aint1[Material.IRON_INGOT.ordinal()] = 208;
            } catch (NoSuchFieldError nosuchfielderror181) {
                ;
            }

            try {
                aint1[Material.IRON_LEGGINGS.ordinal()] = 251;
            } catch (NoSuchFieldError nosuchfielderror182) {
                ;
            }

            try {
                aint1[Material.IRON_ORE.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror183) {
                ;
            }

            try {
                aint1[Material.IRON_PICKAXE.ordinal()] = 200;
            } catch (NoSuchFieldError nosuchfielderror184) {
                ;
            }

            try {
                aint1[Material.IRON_PLATE.ordinal()] = 149;
            } catch (NoSuchFieldError nosuchfielderror185) {
                ;
            }

            try {
                aint1[Material.IRON_SPADE.ordinal()] = 199;
            } catch (NoSuchFieldError nosuchfielderror186) {
                ;
            }

            try {
                aint1[Material.IRON_SWORD.ordinal()] = 210;
            } catch (NoSuchFieldError nosuchfielderror187) {
                ;
            }

            try {
                aint1[Material.IRON_TRAPDOOR.ordinal()] = 168;
            } catch (NoSuchFieldError nosuchfielderror188) {
                ;
            }

            try {
                aint1[Material.ITEM_FRAME.ordinal()] = 332;
            } catch (NoSuchFieldError nosuchfielderror189) {
                ;
            }

            try {
                aint1[Material.JACK_O_LANTERN.ordinal()] = 92;
            } catch (NoSuchFieldError nosuchfielderror190) {
                ;
            }

            try {
                aint1[Material.JUKEBOX.ordinal()] = 85;
            } catch (NoSuchFieldError nosuchfielderror191) {
                ;
            }

            try {
                aint1[Material.JUNGLE_DOOR.ordinal()] = 196;
            } catch (NoSuchFieldError nosuchfielderror192) {
                ;
            }

            try {
                aint1[Material.JUNGLE_DOOR_ITEM.ordinal()] = 371;
            } catch (NoSuchFieldError nosuchfielderror193) {
                ;
            }

            try {
                aint1[Material.JUNGLE_FENCE.ordinal()] = 191;
            } catch (NoSuchFieldError nosuchfielderror194) {
                ;
            }

            try {
                aint1[Material.JUNGLE_FENCE_GATE.ordinal()] = 186;
            } catch (NoSuchFieldError nosuchfielderror195) {
                ;
            }

            try {
                aint1[Material.JUNGLE_WOOD_STAIRS.ordinal()] = 137;
            } catch (NoSuchFieldError nosuchfielderror196) {
                ;
            }

            try {
                aint1[Material.LADDER.ordinal()] = 66;
            } catch (NoSuchFieldError nosuchfielderror197) {
                ;
            }

            try {
                aint1[Material.LAPIS_BLOCK.ordinal()] = 23;
            } catch (NoSuchFieldError nosuchfielderror198) {
                ;
            }

            try {
                aint1[Material.LAPIS_ORE.ordinal()] = 22;
            } catch (NoSuchFieldError nosuchfielderror199) {
                ;
            }

            try {
                aint1[Material.LAVA.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror200) {
                ;
            }

            try {
                aint1[Material.LAVA_BUCKET.ordinal()] = 270;
            } catch (NoSuchFieldError nosuchfielderror201) {
                ;
            }

            try {
                aint1[Material.LEASH.ordinal()] = 363;
            } catch (NoSuchFieldError nosuchfielderror202) {
                ;
            }

            try {
                aint1[Material.LEATHER.ordinal()] = 277;
            } catch (NoSuchFieldError nosuchfielderror203) {
                ;
            }

            try {
                aint1[Material.LEATHER_BOOTS.ordinal()] = 244;
            } catch (NoSuchFieldError nosuchfielderror204) {
                ;
            }

            try {
                aint1[Material.LEATHER_CHESTPLATE.ordinal()] = 242;
            } catch (NoSuchFieldError nosuchfielderror205) {
                ;
            }

            try {
                aint1[Material.LEATHER_HELMET.ordinal()] = 241;
            } catch (NoSuchFieldError nosuchfielderror206) {
                ;
            }

            try {
                aint1[Material.LEATHER_LEGGINGS.ordinal()] = 243;
            } catch (NoSuchFieldError nosuchfielderror207) {
                ;
            }

            try {
                aint1[Material.LEAVES.ordinal()] = 19;
            } catch (NoSuchFieldError nosuchfielderror208) {
                ;
            }

            try {
                aint1[Material.LEAVES_2.ordinal()] = 162;
            } catch (NoSuchFieldError nosuchfielderror209) {
                ;
            }

            try {
                aint1[Material.LEVER.ordinal()] = 70;
            } catch (NoSuchFieldError nosuchfielderror210) {
                ;
            }

            try {
                aint1[Material.LOG.ordinal()] = 18;
            } catch (NoSuchFieldError nosuchfielderror211) {
                ;
            }

            try {
                aint1[Material.LOG_2.ordinal()] = 163;
            } catch (NoSuchFieldError nosuchfielderror212) {
                ;
            }

            try {
                aint1[Material.LONG_GRASS.ordinal()] = 32;
            } catch (NoSuchFieldError nosuchfielderror213) {
                ;
            }

            try {
                aint1[Material.MAGMA_CREAM.ordinal()] = 321;
            } catch (NoSuchFieldError nosuchfielderror214) {
                ;
            }

            try {
                aint1[Material.MAP.ordinal()] = 301;
            } catch (NoSuchFieldError nosuchfielderror215) {
                ;
            }

            try {
                aint1[Material.MELON.ordinal()] = 303;
            } catch (NoSuchFieldError nosuchfielderror216) {
                ;
            }

            try {
                aint1[Material.MELON_BLOCK.ordinal()] = 104;
            } catch (NoSuchFieldError nosuchfielderror217) {
                ;
            }

            try {
                aint1[Material.MELON_SEEDS.ordinal()] = 305;
            } catch (NoSuchFieldError nosuchfielderror218) {
                ;
            }

            try {
                aint1[Material.MELON_STEM.ordinal()] = 106;
            } catch (NoSuchFieldError nosuchfielderror219) {
                ;
            }

            try {
                aint1[Material.MILK_BUCKET.ordinal()] = 278;
            } catch (NoSuchFieldError nosuchfielderror220) {
                ;
            }

            try {
                aint1[Material.MINECART.ordinal()] = 271;
            } catch (NoSuchFieldError nosuchfielderror221) {
                ;
            }

            try {
                aint1[Material.MOB_SPAWNER.ordinal()] = 53;
            } catch (NoSuchFieldError nosuchfielderror222) {
                ;
            }

            try {
                aint1[Material.MONSTER_EGG.ordinal()] = 326;
            } catch (NoSuchFieldError nosuchfielderror223) {
                ;
            }

            try {
                aint1[Material.MONSTER_EGGS.ordinal()] = 98;
            } catch (NoSuchFieldError nosuchfielderror224) {
                ;
            }

            try {
                aint1[Material.MOSSY_COBBLESTONE.ordinal()] = 49;
            } catch (NoSuchFieldError nosuchfielderror225) {
                ;
            }

            try {
                aint1[Material.MUSHROOM_SOUP.ordinal()] = 225;
            } catch (NoSuchFieldError nosuchfielderror226) {
                ;
            }

            try {
                aint1[Material.MUTTON.ordinal()] = 366;
            } catch (NoSuchFieldError nosuchfielderror227) {
                ;
            }

            try {
                aint1[Material.MYCEL.ordinal()] = 111;
            } catch (NoSuchFieldError nosuchfielderror228) {
                ;
            }

            try {
                aint1[Material.NAME_TAG.ordinal()] = 364;
            } catch (NoSuchFieldError nosuchfielderror229) {
                ;
            }

            try {
                aint1[Material.NETHERRACK.ordinal()] = 88;
            } catch (NoSuchFieldError nosuchfielderror230) {
                ;
            }

            try {
                aint1[Material.NETHER_BRICK.ordinal()] = 113;
            } catch (NoSuchFieldError nosuchfielderror231) {
                ;
            }

            try {
                aint1[Material.NETHER_BRICK_ITEM.ordinal()] = 348;
            } catch (NoSuchFieldError nosuchfielderror232) {
                ;
            }

            try {
                aint1[Material.NETHER_BRICK_STAIRS.ordinal()] = 115;
            } catch (NoSuchFieldError nosuchfielderror233) {
                ;
            }

            try {
                aint1[Material.NETHER_FENCE.ordinal()] = 114;
            } catch (NoSuchFieldError nosuchfielderror234) {
                ;
            }

            try {
                aint1[Material.NETHER_STALK.ordinal()] = 315;
            } catch (NoSuchFieldError nosuchfielderror235) {
                ;
            }

            try {
                aint1[Material.NETHER_STAR.ordinal()] = 342;
            } catch (NoSuchFieldError nosuchfielderror236) {
                ;
            }

            try {
                aint1[Material.NETHER_WARTS.ordinal()] = 116;
            } catch (NoSuchFieldError nosuchfielderror237) {
                ;
            }

            try {
                aint1[Material.NOTE_BLOCK.ordinal()] = 26;
            } catch (NoSuchFieldError nosuchfielderror238) {
                ;
            }

            try {
                aint1[Material.OBSbiomeIDIAN.ordinal()] = 50;
            } catch (NoSuchFieldError nosuchfielderror239) {
                ;
            }

            try {
                aint1[Material.PACKED_ICE.ordinal()] = 175;
            } catch (NoSuchFieldError nosuchfielderror240) {
                ;
            }

            try {
                aint1[Material.PAINTING.ordinal()] = 264;
            } catch (NoSuchFieldError nosuchfielderror241) {
                ;
            }

            try {
                aint1[Material.PAPER.ordinal()] = 282;
            } catch (NoSuchFieldError nosuchfielderror242) {
                ;
            }

            try {
                aint1[Material.PISTON_BASE.ordinal()] = 34;
            } catch (NoSuchFieldError nosuchfielderror243) {
                ;
            }

            try {
                aint1[Material.PISTON_EXTENSION.ordinal()] = 35;
            } catch (NoSuchFieldError nosuchfielderror244) {
                ;
            }

            try {
                aint1[Material.PISTON_MOVING_PIECE.ordinal()] = 37;
            } catch (NoSuchFieldError nosuchfielderror245) {
                ;
            }

            try {
                aint1[Material.PISTON_STICKY_BASE.ordinal()] = 30;
            } catch (NoSuchFieldError nosuchfielderror246) {
                ;
            }

            try {
                aint1[Material.POISONOUS_POTATO.ordinal()] = 337;
            } catch (NoSuchFieldError nosuchfielderror247) {
                ;
            }

            try {
                aint1[Material.PORK.ordinal()] = 262;
            } catch (NoSuchFieldError nosuchfielderror248) {
                ;
            }

            try {
                aint1[Material.PORTAL.ordinal()] = 91;
            } catch (NoSuchFieldError nosuchfielderror249) {
                ;
            }

            try {
                aint1[Material.POTATO.ordinal()] = 143;
            } catch (NoSuchFieldError nosuchfielderror250) {
                ;
            }

            try {
                aint1[Material.POTATO_ITEM.ordinal()] = 335;
            } catch (NoSuchFieldError nosuchfielderror251) {
                ;
            }

            try {
                aint1[Material.POTION.ordinal()] = 316;
            } catch (NoSuchFieldError nosuchfielderror252) {
                ;
            }

            try {
                aint1[Material.POWERED_MINECART.ordinal()] = 286;
            } catch (NoSuchFieldError nosuchfielderror253) {
                ;
            }

            try {
                aint1[Material.POWERED_RAIL.ordinal()] = 28;
            } catch (NoSuchFieldError nosuchfielderror254) {
                ;
            }

            try {
                aint1[Material.PRISMARINE.ordinal()] = 169;
            } catch (NoSuchFieldError nosuchfielderror255) {
                ;
            }

            try {
                aint1[Material.PRISMARINE_CRYSTALS.ordinal()] = 353;
            } catch (NoSuchFieldError nosuchfielderror256) {
                ;
            }

            try {
                aint1[Material.PRISMARINE_SHARD.ordinal()] = 352;
            } catch (NoSuchFieldError nosuchfielderror257) {
                ;
            }

            try {
                aint1[Material.PUMPKIN.ordinal()] = 87;
            } catch (NoSuchFieldError nosuchfielderror258) {
                ;
            }

            try {
                aint1[Material.PUMPKIN_PIE.ordinal()] = 343;
            } catch (NoSuchFieldError nosuchfielderror259) {
                ;
            }

            try {
                aint1[Material.PUMPKIN_SEEDS.ordinal()] = 304;
            } catch (NoSuchFieldError nosuchfielderror260) {
                ;
            }

            try {
                aint1[Material.PUMPKIN_STEM.ordinal()] = 105;
            } catch (NoSuchFieldError nosuchfielderror261) {
                ;
            }

            try {
                aint1[Material.QUARTZ.ordinal()] = 349;
            } catch (NoSuchFieldError nosuchfielderror262) {
                ;
            }

            try {
                aint1[Material.QUARTZ_BLOCK.ordinal()] = 156;
            } catch (NoSuchFieldError nosuchfielderror263) {
                ;
            }

            try {
                aint1[Material.QUARTZ_ORE.ordinal()] = 154;
            } catch (NoSuchFieldError nosuchfielderror264) {
                ;
            }

            try {
                aint1[Material.QUARTZ_STAIRS.ordinal()] = 157;
            } catch (NoSuchFieldError nosuchfielderror265) {
                ;
            }

            try {
                aint1[Material.RABBIT.ordinal()] = 354;
            } catch (NoSuchFieldError nosuchfielderror266) {
                ;
            }

            try {
                aint1[Material.RABBIT_FOOT.ordinal()] = 357;
            } catch (NoSuchFieldError nosuchfielderror267) {
                ;
            }

            try {
                aint1[Material.RABBIT_HbiomeIDE.ordinal()] = 358;
            } catch (NoSuchFieldError nosuchfielderror268) {
                ;
            }

            try {
                aint1[Material.RABBIT_STEW.ordinal()] = 356;
            } catch (NoSuchFieldError nosuchfielderror269) {
                ;
            }

            try {
                aint1[Material.RAILS.ordinal()] = 67;
            } catch (NoSuchFieldError nosuchfielderror270) {
                ;
            }

            try {
                aint1[Material.RAW_BEEF.ordinal()] = 306;
            } catch (NoSuchFieldError nosuchfielderror271) {
                ;
            }

            try {
                aint1[Material.RAW_CHICKEN.ordinal()] = 308;
            } catch (NoSuchFieldError nosuchfielderror272) {
                ;
            }

            try {
                aint1[Material.RAW_FISH.ordinal()] = 292;
            } catch (NoSuchFieldError nosuchfielderror273) {
                ;
            }

            try {
                aint1[Material.RECORD_10.ordinal()] = 383;
            } catch (NoSuchFieldError nosuchfielderror274) {
                ;
            }

            try {
                aint1[Material.RECORD_11.ordinal()] = 384;
            } catch (NoSuchFieldError nosuchfielderror275) {
                ;
            }

            try {
                aint1[Material.RECORD_12.ordinal()] = 385;
            } catch (NoSuchFieldError nosuchfielderror276) {
                ;
            }

            try {
                aint1[Material.RECORD_3.ordinal()] = 376;
            } catch (NoSuchFieldError nosuchfielderror277) {
                ;
            }

            try {
                aint1[Material.RECORD_4.ordinal()] = 377;
            } catch (NoSuchFieldError nosuchfielderror278) {
                ;
            }

            try {
                aint1[Material.RECORD_5.ordinal()] = 378;
            } catch (NoSuchFieldError nosuchfielderror279) {
                ;
            }

            try {
                aint1[Material.RECORD_6.ordinal()] = 379;
            } catch (NoSuchFieldError nosuchfielderror280) {
                ;
            }

            try {
                aint1[Material.RECORD_7.ordinal()] = 380;
            } catch (NoSuchFieldError nosuchfielderror281) {
                ;
            }

            try {
                aint1[Material.RECORD_8.ordinal()] = 381;
            } catch (NoSuchFieldError nosuchfielderror282) {
                ;
            }

            try {
                aint1[Material.RECORD_9.ordinal()] = 382;
            } catch (NoSuchFieldError nosuchfielderror283) {
                ;
            }

            try {
                aint1[Material.REDSTONE.ordinal()] = 274;
            } catch (NoSuchFieldError nosuchfielderror284) {
                ;
            }

            try {
                aint1[Material.REDSTONE_BLOCK.ordinal()] = 153;
            } catch (NoSuchFieldError nosuchfielderror285) {
                ;
            }

            try {
                aint1[Material.REDSTONE_COMPARATOR.ordinal()] = 347;
            } catch (NoSuchFieldError nosuchfielderror286) {
                ;
            }

            try {
                aint1[Material.REDSTONE_COMPARATOR_OFF.ordinal()] = 150;
            } catch (NoSuchFieldError nosuchfielderror287) {
                ;
            }

            try {
                aint1[Material.REDSTONE_COMPARATOR_ON.ordinal()] = 151;
            } catch (NoSuchFieldError nosuchfielderror288) {
                ;
            }

            try {
                aint1[Material.REDSTONE_LAMP_OFF.ordinal()] = 124;
            } catch (NoSuchFieldError nosuchfielderror289) {
                ;
            }

            try {
                aint1[Material.REDSTONE_LAMP_ON.ordinal()] = 125;
            } catch (NoSuchFieldError nosuchfielderror290) {
                ;
            }

            try {
                aint1[Material.REDSTONE_ORE.ordinal()] = 74;
            } catch (NoSuchFieldError nosuchfielderror291) {
                ;
            }

            try {
                aint1[Material.REDSTONE_TORCH_OFF.ordinal()] = 76;
            } catch (NoSuchFieldError nosuchfielderror292) {
                ;
            }

            try {
                aint1[Material.REDSTONE_TORCH_ON.ordinal()] = 77;
            } catch (NoSuchFieldError nosuchfielderror293) {
                ;
            }

            try {
                aint1[Material.REDSTONE_WIRE.ordinal()] = 56;
            } catch (NoSuchFieldError nosuchfielderror294) {
                ;
            }

            try {
                aint1[Material.RED_MUSHROOM.ordinal()] = 41;
            } catch (NoSuchFieldError nosuchfielderror295) {
                ;
            }

            try {
                aint1[Material.RED_ROSE.ordinal()] = 39;
            } catch (NoSuchFieldError nosuchfielderror296) {
                ;
            }

            try {
                aint1[Material.RED_SANDSTONE.ordinal()] = 180;
            } catch (NoSuchFieldError nosuchfielderror297) {
                ;
            }

            try {
                aint1[Material.RED_SANDSTONE_STAIRS.ordinal()] = 181;
            } catch (NoSuchFieldError nosuchfielderror298) {
                ;
            }

            try {
                aint1[Material.ROTTEN_FLESH.ordinal()] = 310;
            } catch (NoSuchFieldError nosuchfielderror299) {
                ;
            }

            try {
                aint1[Material.SADDLE.ordinal()] = 272;
            } catch (NoSuchFieldError nosuchfielderror300) {
                ;
            }

            try {
                aint1[Material.SAND.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror301) {
                ;
            }

            try {
                aint1[Material.SANDSTONE.ordinal()] = 25;
            } catch (NoSuchFieldError nosuchfielderror302) {
                ;
            }

            try {
                aint1[Material.SANDSTONE_STAIRS.ordinal()] = 129;
            } catch (NoSuchFieldError nosuchfielderror303) {
                ;
            }

            try {
                aint1[Material.SAPLING.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror304) {
                ;
            }

            try {
                aint1[Material.SEA_LANTERN.ordinal()] = 170;
            } catch (NoSuchFieldError nosuchfielderror305) {
                ;
            }

            try {
                aint1[Material.SEEDS.ordinal()] = 238;
            } catch (NoSuchFieldError nosuchfielderror306) {
                ;
            }

            try {
                aint1[Material.SHEARS.ordinal()] = 302;
            } catch (NoSuchFieldError nosuchfielderror307) {
                ;
            }

            try {
                aint1[Material.SIGN.ordinal()] = 266;
            } catch (NoSuchFieldError nosuchfielderror308) {
                ;
            }

            try {
                aint1[Material.SIGN_POST.ordinal()] = 64;
            } catch (NoSuchFieldError nosuchfielderror309) {
                ;
            }

            try {
                aint1[Material.SKULL.ordinal()] = 145;
            } catch (NoSuchFieldError nosuchfielderror310) {
                ;
            }

            try {
                aint1[Material.SKULL_ITEM.ordinal()] = 340;
            } catch (NoSuchFieldError nosuchfielderror311) {
                ;
            }

            try {
                aint1[Material.SLIME_BALL.ordinal()] = 284;
            } catch (NoSuchFieldError nosuchfielderror312) {
                ;
            }

            try {
                aint1[Material.SLIME_BLOCK.ordinal()] = 166;
            } catch (NoSuchFieldError nosuchfielderror313) {
                ;
            }

            try {
                aint1[Material.SMOOTH_BRICK.ordinal()] = 99;
            } catch (NoSuchFieldError nosuchfielderror314) {
                ;
            }

            try {
                aint1[Material.SMOOTH_STAIRS.ordinal()] = 110;
            } catch (NoSuchFieldError nosuchfielderror315) {
                ;
            }

            try {
                aint1[Material.SNOW.ordinal()] = 79;
            } catch (NoSuchFieldError nosuchfielderror316) {
                ;
            }

            try {
                aint1[Material.SNOW_BALL.ordinal()] = 275;
            } catch (NoSuchFieldError nosuchfielderror317) {
                ;
            }

            try {
                aint1[Material.SNOW_BLOCK.ordinal()] = 81;
            } catch (NoSuchFieldError nosuchfielderror318) {
                ;
            }

            try {
                aint1[Material.SOIL.ordinal()] = 61;
            } catch (NoSuchFieldError nosuchfielderror319) {
                ;
            }

            try {
                aint1[Material.SOUL_SAND.ordinal()] = 89;
            } catch (NoSuchFieldError nosuchfielderror320) {
                ;
            }

            try {
                aint1[Material.SPECKLED_MELON.ordinal()] = 325;
            } catch (NoSuchFieldError nosuchfielderror321) {
                ;
            }

            try {
                aint1[Material.SPbiomeIDER_EYE.ordinal()] = 318;
            } catch (NoSuchFieldError nosuchfielderror322) {
                ;
            }

            try {
                aint1[Material.SPONGE.ordinal()] = 20;
            } catch (NoSuchFieldError nosuchfielderror323) {
                ;
            }

            try {
                aint1[Material.SPRUCE_DOOR.ordinal()] = 194;
            } catch (NoSuchFieldError nosuchfielderror324) {
                ;
            }

            try {
                aint1[Material.SPRUCE_DOOR_ITEM.ordinal()] = 369;
            } catch (NoSuchFieldError nosuchfielderror325) {
                ;
            }

            try {
                aint1[Material.SPRUCE_FENCE.ordinal()] = 189;
            } catch (NoSuchFieldError nosuchfielderror326) {
                ;
            }

            try {
                aint1[Material.SPRUCE_FENCE_GATE.ordinal()] = 184;
            } catch (NoSuchFieldError nosuchfielderror327) {
                ;
            }

            try {
                aint1[Material.SPRUCE_WOOD_STAIRS.ordinal()] = 135;
            } catch (NoSuchFieldError nosuchfielderror328) {
                ;
            }

            try {
                aint1[Material.STAINED_CLAY.ordinal()] = 160;
            } catch (NoSuchFieldError nosuchfielderror329) {
                ;
            }

            try {
                aint1[Material.STAINED_GLASS.ordinal()] = 96;
            } catch (NoSuchFieldError nosuchfielderror330) {
                ;
            }

            try {
                aint1[Material.STAINED_GLASS_PANE.ordinal()] = 161;
            } catch (NoSuchFieldError nosuchfielderror331) {
                ;
            }

            try {
                aint1[Material.STANDING_BANNER.ordinal()] = 177;
            } catch (NoSuchFieldError nosuchfielderror332) {
                ;
            }

            try {
                aint1[Material.STATIONARY_LAVA.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror333) {
                ;
            }

            try {
                aint1[Material.STATIONARY_WATER.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror334) {
                ;
            }

            try {
                aint1[Material.STEP.ordinal()] = 45;
            } catch (NoSuchFieldError nosuchfielderror335) {
                ;
            }

            try {
                aint1[Material.STICK.ordinal()] = 223;
            } catch (NoSuchFieldError nosuchfielderror336) {
                ;
            }

            try {
                aint1[Material.STONE.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror337) {
                ;
            }

            try {
                aint1[Material.STONE_AXE.ordinal()] = 218;
            } catch (NoSuchFieldError nosuchfielderror338) {
                ;
            }

            try {
                aint1[Material.STONE_BUTTON.ordinal()] = 78;
            } catch (NoSuchFieldError nosuchfielderror339) {
                ;
            }

            try {
                aint1[Material.STONE_HOE.ordinal()] = 234;
            } catch (NoSuchFieldError nosuchfielderror340) {
                ;
            }

            try {
                aint1[Material.STONE_PICKAXE.ordinal()] = 217;
            } catch (NoSuchFieldError nosuchfielderror341) {
                ;
            }

            try {
                aint1[Material.STONE_PLATE.ordinal()] = 71;
            } catch (NoSuchFieldError nosuchfielderror342) {
                ;
            }

            try {
                aint1[Material.STONE_SLAB2.ordinal()] = 183;
            } catch (NoSuchFieldError nosuchfielderror343) {
                ;
            }

            try {
                aint1[Material.STONE_SPADE.ordinal()] = 216;
            } catch (NoSuchFieldError nosuchfielderror344) {
                ;
            }

            try {
                aint1[Material.STONE_SWORD.ordinal()] = 215;
            } catch (NoSuchFieldError nosuchfielderror345) {
                ;
            }

            try {
                aint1[Material.STORAGE_MINECART.ordinal()] = 285;
            } catch (NoSuchFieldError nosuchfielderror346) {
                ;
            }

            try {
                aint1[Material.STRING.ordinal()] = 230;
            } catch (NoSuchFieldError nosuchfielderror347) {
                ;
            }

            try {
                aint1[Material.SUGAR.ordinal()] = 296;
            } catch (NoSuchFieldError nosuchfielderror348) {
                ;
            }

            try {
                aint1[Material.SUGAR_CANE.ordinal()] = 281;
            } catch (NoSuchFieldError nosuchfielderror349) {
                ;
            }

            try {
                aint1[Material.SUGAR_CANE_BLOCK.ordinal()] = 84;
            } catch (NoSuchFieldError nosuchfielderror350) {
                ;
            }

            try {
                aint1[Material.SULPHUR.ordinal()] = 232;
            } catch (NoSuchFieldError nosuchfielderror351) {
                ;
            }

            try {
                aint1[Material.THIN_GLASS.ordinal()] = 103;
            } catch (NoSuchFieldError nosuchfielderror352) {
                ;
            }

            try {
                aint1[Material.TNT.ordinal()] = 47;
            } catch (NoSuchFieldError nosuchfielderror353) {
                ;
            }

            try {
                aint1[Material.TORCH.ordinal()] = 51;
            } catch (NoSuchFieldError nosuchfielderror354) {
                ;
            }

            try {
                aint1[Material.TRAPPED_CHEST.ordinal()] = 147;
            } catch (NoSuchFieldError nosuchfielderror355) {
                ;
            }

            try {
                aint1[Material.TRAP_DOOR.ordinal()] = 97;
            } catch (NoSuchFieldError nosuchfielderror356) {
                ;
            }

            try {
                aint1[Material.TRIPWIRE.ordinal()] = 133;
            } catch (NoSuchFieldError nosuchfielderror357) {
                ;
            }

            try {
                aint1[Material.TRIPWIRE_HOOK.ordinal()] = 132;
            } catch (NoSuchFieldError nosuchfielderror358) {
                ;
            }

            try {
                aint1[Material.VINE.ordinal()] = 107;
            } catch (NoSuchFieldError nosuchfielderror359) {
                ;
            }

            try {
                aint1[Material.WALL_BANNER.ordinal()] = 178;
            } catch (NoSuchFieldError nosuchfielderror360) {
                ;
            }

            try {
                aint1[Material.WALL_SIGN.ordinal()] = 69;
            } catch (NoSuchFieldError nosuchfielderror361) {
                ;
            }

            try {
                aint1[Material.WATCH.ordinal()] = 290;
            } catch (NoSuchFieldError nosuchfielderror362) {
                ;
            }

            try {
                aint1[Material.WATER.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror363) {
                ;
            }

            try {
                aint1[Material.WATER_BUCKET.ordinal()] = 269;
            } catch (NoSuchFieldError nosuchfielderror364) {
                ;
            }

            try {
                aint1[Material.WATER_LILY.ordinal()] = 112;
            } catch (NoSuchFieldError nosuchfielderror365) {
                ;
            }

            try {
                aint1[Material.WEB.ordinal()] = 31;
            } catch (NoSuchFieldError nosuchfielderror366) {
                ;
            }

            try {
                aint1[Material.WHEAT.ordinal()] = 239;
            } catch (NoSuchFieldError nosuchfielderror367) {
                ;
            }

            try {
                aint1[Material.WOOD.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror368) {
                ;
            }

            try {
                aint1[Material.WOODEN_DOOR.ordinal()] = 65;
            } catch (NoSuchFieldError nosuchfielderror369) {
                ;
            }

            try {
                aint1[Material.WOOD_AXE.ordinal()] = 214;
            } catch (NoSuchFieldError nosuchfielderror370) {
                ;
            }

            try {
                aint1[Material.WOOD_BUTTON.ordinal()] = 144;
            } catch (NoSuchFieldError nosuchfielderror371) {
                ;
            }

            try {
                aint1[Material.WOOD_DOOR.ordinal()] = 267;
            } catch (NoSuchFieldError nosuchfielderror372) {
                ;
            }

            try {
                aint1[Material.WOOD_DOUBLE_STEP.ordinal()] = 126;
            } catch (NoSuchFieldError nosuchfielderror373) {
                ;
            }

            try {
                aint1[Material.WOOD_HOE.ordinal()] = 233;
            } catch (NoSuchFieldError nosuchfielderror374) {
                ;
            }

            try {
                aint1[Material.WOOD_PICKAXE.ordinal()] = 213;
            } catch (NoSuchFieldError nosuchfielderror375) {
                ;
            }

            try {
                aint1[Material.WOOD_PLATE.ordinal()] = 73;
            } catch (NoSuchFieldError nosuchfielderror376) {
                ;
            }

            try {
                aint1[Material.WOOD_SPADE.ordinal()] = 212;
            } catch (NoSuchFieldError nosuchfielderror377) {
                ;
            }

            try {
                aint1[Material.WOOD_STAIRS.ordinal()] = 54;
            } catch (NoSuchFieldError nosuchfielderror378) {
                ;
            }

            try {
                aint1[Material.WOOD_STEP.ordinal()] = 127;
            } catch (NoSuchFieldError nosuchfielderror379) {
                ;
            }

            try {
                aint1[Material.WOOD_SWORD.ordinal()] = 211;
            } catch (NoSuchFieldError nosuchfielderror380) {
                ;
            }

            try {
                aint1[Material.WOOL.ordinal()] = 36;
            } catch (NoSuchFieldError nosuchfielderror381) {
                ;
            }

            try {
                aint1[Material.WORKBENCH.ordinal()] = 59;
            } catch (NoSuchFieldError nosuchfielderror382) {
                ;
            }

            try {
                aint1[Material.WRITTEN_BOOK.ordinal()] = 330;
            } catch (NoSuchFieldError nosuchfielderror383) {
                ;
            }

            try {
                aint1[Material.YELLOW_FLOWER.ordinal()] = 38;
            } catch (NoSuchFieldError nosuchfielderror384) {
                ;
            }

            CraftBlock.$SWITCH_TABLE$org$bukkit$Material = aint1;
            return aint1;
        }
    }
}
