package org.bukkit.craftbukkit.v1_8_R3;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.EmptyChunk;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.WorldChunkManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.entity.Entity;

public class CraftChunk implements Chunk {

    private WeakReference weakChunk;
    private final WorldServer worldServer;
    private final int x;
    private final int z;
    private static final byte[] emptyData = new byte[2048];
    private static final short[] emptyBlockIDs = new short[4096];
    private static final byte[] emptySkyLight = new byte[2048];

    static {
        Arrays.fill(CraftChunk.emptySkyLight, (byte) -1);
    }

    public CraftChunk(net.minecraft.server.v1_8_R3.Chunk chunk) {
        if (!(chunk instanceof EmptyChunk)) {
            this.weakChunk = new WeakReference(chunk);
        }

        this.worldServer = (WorldServer) this.getHandle().world;
        this.x = this.getHandle().locX;
        this.z = this.getHandle().locZ;
    }

    public World getWorld() {
        return this.worldServer.getWorld();
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld) this.getWorld();
    }

    public net.minecraft.server.v1_8_R3.Chunk getHandle() {
        net.minecraft.server.v1_8_R3.Chunk c = (net.minecraft.server.v1_8_R3.Chunk) this.weakChunk.get();

        if (c == null) {
            c = this.worldServer.getChunkAt(this.x, this.z);
            if (!(c instanceof EmptyChunk)) {
                this.weakChunk = new WeakReference(c);
            }
        }

        return c;
    }

    void breakLink() {
        this.weakChunk.clear();
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public String toString() {
        return "CraftChunk{x=" + this.getX() + "z=" + this.getZ() + '}';
    }

    public Block getBlock(int x, int y, int z) {
        return new CraftBlock(this, this.getX() << 4 | x & 15, y, this.getZ() << 4 | z & 15);
    }

    public Entity[] getEntities() {
        int count = 0;
        int index = 0;
        net.minecraft.server.v1_8_R3.Chunk chunk = this.getHandle();

        for (int entities = 0; entities < 16; ++entities) {
            count += chunk.entitySlices[entities].size();
        }

        Entity[] aentity = new Entity[count];

        for (int i = 0; i < 16; ++i) {
            Object[] aobject;
            int i = (aobject = chunk.entitySlices[i].toArray()).length;

            for (int j = 0; j < i; ++j) {
                Object obj = aobject[j];

                if (obj instanceof net.minecraft.server.v1_8_R3.Entity) {
                    aentity[index++] = ((net.minecraft.server.v1_8_R3.Entity) obj).getBukkitEntity();
                }
            }
        }

        return aentity;
    }

    public BlockState[] getTileEntities() {
        int index = 0;
        net.minecraft.server.v1_8_R3.Chunk chunk = this.getHandle();
        BlockState[] entities = new BlockState[chunk.tileEntities.size()];
        Object[] aobject;
        int i = (aobject = chunk.tileEntities.keySet().toArray()).length;

        for (int j = 0; j < i; ++j) {
            Object obj = aobject[j];

            if (obj instanceof BlockPosition) {
                BlockPosition position = (BlockPosition) obj;

                entities[index++] = this.worldServer.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState();
            }
        }

        return entities;
    }

    public boolean isLoaded() {
        return this.getWorld().isChunkLoaded(this);
    }

    public boolean load() {
        return this.getWorld().loadChunk(this.getX(), this.getZ(), true);
    }

    public boolean load(boolean generate) {
        return this.getWorld().loadChunk(this.getX(), this.getZ(), generate);
    }

    public boolean unload() {
        return this.getWorld().unloadChunk(this.getX(), this.getZ());
    }

    public boolean unload(boolean save) {
        return this.getWorld().unloadChunk(this.getX(), this.getZ(), save);
    }

    public boolean unload(boolean save, boolean safe) {
        return this.getWorld().unloadChunk(this.getX(), this.getZ(), save, safe);
    }

    public ChunkSnapshot getChunkSnapshot() {
        return this.getChunkSnapshot(true, false, false);
    }

    public ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain) {
        net.minecraft.server.v1_8_R3.Chunk chunk = this.getHandle();
        ChunkSection[] cs = chunk.getSections();
        short[][] sectionBlockIDs = new short[cs.length][];
        byte[][] sectionBlockData = new byte[cs.length][];
        byte[][] sectionSkyLights = new byte[cs.length][];
        byte[][] sectionEmitLights = new byte[cs.length][];
        boolean[] sectionEmpty = new boolean[cs.length];

        int i;

        for (int hmap = 0; hmap < cs.length; ++hmap) {
            if (cs[hmap] == null) {
                sectionBlockIDs[hmap] = CraftChunk.emptyBlockIDs;
                sectionBlockData[hmap] = CraftChunk.emptyData;
                sectionSkyLights[hmap] = CraftChunk.emptySkyLight;
                sectionEmitLights[hmap] = CraftChunk.emptyData;
                sectionEmpty[hmap] = true;
            } else {
                short[] biome = new short[4096];
                char[] biomeTemp = cs[hmap].getIdArray();
                byte[] biomeRain = sectionBlockData[hmap] = new byte[2048];

                for (int world = 0; world < 4096; ++world) {
                    if (biomeTemp[world] != 0) {
                        IBlockData dat = (IBlockData) net.minecraft.server.v1_8_R3.Block.d.a(biomeTemp[world]);

                        if (dat != null) {
                            biome[world] = (short) net.minecraft.server.v1_8_R3.Block.getId(dat.getBlock());
                            i = dat.getBlock().toLegacyData(dat);
                            int jj = world >> 1;

                            if ((world & 1) == 0) {
                                biomeRain[jj] = (byte) (biomeRain[jj] & 240 | i & 15);
                            } else {
                                biomeRain[jj] = (byte) (biomeRain[jj] & 15 | (i & 15) << 4);
                            }
                        }
                    }
                }

                sectionBlockIDs[hmap] = biome;
                if (cs[hmap].getSkyLightArray() == null) {
                    sectionSkyLights[hmap] = CraftChunk.emptyData;
                } else {
                    sectionSkyLights[hmap] = new byte[2048];
                    System.arraycopy(cs[hmap].getSkyLightArray().a(), 0, sectionSkyLights[hmap], 0, 2048);
                }

                sectionEmitLights[hmap] = new byte[2048];
                System.arraycopy(cs[hmap].getEmittedLightArray().a(), 0, sectionEmitLights[hmap], 0, 2048);
            }
        }

        int[] aint = null;

        if (includeMaxBlockY) {
            aint = new int[256];
            System.arraycopy(chunk.heightMap, 0, aint, 0, 256);
        }

        BiomeBase[] abiomebase = null;
        double[] adouble = null;
        double[] adouble1 = null;

        if (includeBiome || includeBiomeTempRain) {
            WorldChunkManager worldchunkmanager = chunk.world.getWorldChunkManager();

            if (includeBiome) {
                abiomebase = new BiomeBase[256];

                for (int i = 0; i < 256; ++i) {
                    abiomebase[i] = chunk.getBiome(new BlockPosition(i & 15, 0, i >> 4), worldchunkmanager);
                }
            }

            if (includeBiomeTempRain) {
                adouble = new double[256];
                adouble1 = new double[256];
                float[] afloat = getTemperatures(worldchunkmanager, this.getX() << 4, this.getZ() << 4);

                for (i = 0; i < 256; ++i) {
                    adouble[i] = (double) afloat[i];
                }

                afloat = worldchunkmanager.getWetness((float[]) null, this.getX() << 4, this.getZ() << 4, 16, 16);

                for (i = 0; i < 256; ++i) {
                    adouble1[i] = (double) afloat[i];
                }
            }
        }

        World world = this.getWorld();

        return new CraftChunkSnapshot(this.getX(), this.getZ(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionBlockData, sectionSkyLights, sectionEmitLights, sectionEmpty, aint, abiomebase, adouble, adouble1);
    }

    public static ChunkSnapshot getEmptyChunkSnapshot(int x, int z, CraftWorld world, boolean includeBiome, boolean includeBiomeTempRain) {
        BiomeBase[] biome = null;
        double[] biomeTemp = null;
        double[] biomeRain = null;

        if (includeBiome || includeBiomeTempRain) {
            WorldChunkManager hSection = world.getHandle().getWorldChunkManager();

            if (includeBiome) {
                biome = new BiomeBase[256];

                for (int blockIDs = 0; blockIDs < 256; ++blockIDs) {
                    biome[blockIDs] = world.getHandle().getBiome(new BlockPosition((x << 4) + (blockIDs & 15), 0, (z << 4) + (blockIDs >> 4)));
                }
            }

            if (includeBiomeTempRain) {
                biomeTemp = new double[256];
                biomeRain = new double[256];
                float[] afloat = getTemperatures(hSection, x << 4, z << 4);

                int skyLight;

                for (skyLight = 0; skyLight < 256; ++skyLight) {
                    biomeTemp[skyLight] = (double) afloat[skyLight];
                }

                afloat = hSection.getWetness((float[]) null, x << 4, z << 4, 16, 16);

                for (skyLight = 0; skyLight < 256; ++skyLight) {
                    biomeRain[skyLight] = (double) afloat[skyLight];
                }
            }
        }

        int i = world.getMaxHeight() >> 4;
        short[][] ashort = new short[i][];
        byte[][] abyte = new byte[i][];
        byte[][] emitLight = new byte[i][];
        byte[][] blockData = new byte[i][];
        boolean[] empty = new boolean[i];

        for (int i = 0; i < i; ++i) {
            ashort[i] = CraftChunk.emptyBlockIDs;
            abyte[i] = CraftChunk.emptySkyLight;
            emitLight[i] = CraftChunk.emptyData;
            blockData[i] = CraftChunk.emptyData;
            empty[i] = true;
        }

        return new CraftChunkSnapshot(x, z, world.getName(), world.getFullTime(), ashort, blockData, abyte, emitLight, empty, new int[256], biome, biomeTemp, biomeRain);
    }

    private static float[] getTemperatures(WorldChunkManager chunkmanager, int chunkX, int chunkZ) {
        BiomeBase[] biomes = chunkmanager.getBiomes((BiomeBase[]) null, chunkX, chunkZ, 16, 16);
        float[] temps = new float[biomes.length];

        for (int i = 0; i < biomes.length; ++i) {
            float temp = biomes[i].temperature;

            if (temp > 1.0F) {
                temp = 1.0F;
            }

            temps[i] = temp;
        }

        return temps;
    }
}
