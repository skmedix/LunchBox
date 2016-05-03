package org.bukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

public abstract class ChunkGenerator {

    private static int[] $SWITCH_TABLE$org$bukkit$World$Environment;

    /** @deprecated */
    @Deprecated
    public byte[] generate(World world, Random random, int x, int z) {
        throw new UnsupportedOperationException("Custom generator is missing required methods: generate(), generateBlockSections() and generateExtBlockSections()");
    }

    /** @deprecated */
    @Deprecated
    public short[][] generateExtBlockSections(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biomes) {
        return null;
    }

    /** @deprecated */
    @Deprecated
    public byte[][] generateBlockSections(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biomes) {
        return null;
    }

    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biome) {
        return null;
    }

    protected final ChunkGenerator.ChunkData createChunkData(World world) {
        return Bukkit.getServer().createChunkData(world);
    }

    public boolean canSpawn(World world, int x, int z) {
        Block highest = world.getBlockAt(x, world.getHighestBlockYAt(x, z), z);

        switch ($SWITCH_TABLE$org$bukkit$World$Environment()[world.getEnvironment().ordinal()]) {
        case 1:
        default:
            if (highest.getType() != Material.SAND && highest.getType() != Material.GRAVEL) {
                return false;
            }

            return true;

        case 2:
            return true;

        case 3:
            return highest.getType() != Material.AIR && highest.getType() != Material.WATER && highest.getType() != Material.LAVA;
        }
    }

    public List getDefaultPopulators(World world) {
        return new ArrayList();
    }

    public Location getFixedSpawnLocation(World world, Random random) {
        return null;
    }

    static int[] $SWITCH_TABLE$org$bukkit$World$Environment() {
        int[] aint = ChunkGenerator.$SWITCH_TABLE$org$bukkit$World$Environment;

        if (ChunkGenerator.$SWITCH_TABLE$org$bukkit$World$Environment != null) {
            return aint;
        } else {
            int[] aint1 = new int[World.Environment.values().length];

            try {
                aint1[World.Environment.NETHER.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[World.Environment.NORMAL.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[World.Environment.THE_END.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            ChunkGenerator.$SWITCH_TABLE$org$bukkit$World$Environment = aint1;
            return aint1;
        }
    }

    public interface BiomeGrid {

        Biome getBiome(int i, int j);

        void setBiome(int i, int j, Biome biome);
    }

    public interface ChunkData {

        int getMaxHeight();

        void setBlock(int i, int j, int k, Material material);

        void setBlock(int i, int j, int k, MaterialData materialdata);

        void setRegion(int i, int j, int k, int l, int i1, int j1, Material material);

        void setRegion(int i, int j, int k, int l, int i1, int j1, MaterialData materialdata);

        Material getType(int i, int j, int k);

        MaterialData getTypeAndData(int i, int j, int k);

        /** @deprecated */
        @Deprecated
        void setRegion(int i, int j, int k, int l, int i1, int j1, int k1);

        /** @deprecated */
        @Deprecated
        void setRegion(int i, int j, int k, int l, int i1, int j1, int k1, int l1);

        /** @deprecated */
        @Deprecated
        void setBlock(int i, int j, int k, int l);

        /** @deprecated */
        @Deprecated
        void setBlock(int i, int j, int k, int l, byte b0);

        /** @deprecated */
        @Deprecated
        int getTypeId(int i, int j, int k);

        /** @deprecated */
        @Deprecated
        byte getData(int i, int j, int k);
    }
}
