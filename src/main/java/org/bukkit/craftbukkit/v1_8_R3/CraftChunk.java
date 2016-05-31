package org.bukkit.craftbukkit.v1_8_R3;

import java.lang.ref.WeakReference;
import java.util.Arrays;


import com.kookykraftmc.lunchbox.LunchBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
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

    public CraftChunk(net.minecraft.world.chunk.Chunk chunk) {
        if (!(chunk instanceof net.minecraft.world.chunk.EmptyChunk)) {
            this.weakChunk = new WeakReference<net.minecraft.world.chunk.Chunk>(chunk);
        }

        worldServer = chunk.getWorld() instanceof net.minecraft.world.World ? (WorldServer) chunk.getWorld() : null; // Thermos - use world instead of worldserver for NatureOverhaul
        x = chunk.xPosition;
        z = chunk.zPosition;
    }

    public World getWorld() {
        return (World) this.worldServer;
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld) this.getWorld();
    }

    public net.minecraft.world.chunk.Chunk getHandle() {
        net.minecraft.world.chunk.Chunk c = (net.minecraft.world.chunk.Chunk) this.weakChunk.get();

        if (c == null) {
            c = this.worldServer.getChunkFromChunkCoords(this.x, this.z);
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
        int count = 0, index = 0;
        net.minecraft.world.chunk.Chunk chunk = getHandle();

        for (int i = 0; i < 16; i++) {
            count += chunk.getEntityLists()[i].size();
        }

        Entity[] entities = new Entity[count];

        for (int i = 0; i < 16; i++) {
            for (Object obj : chunk.getEntityLists()[i].toArray()) {
                if (!(obj instanceof net.minecraft.entity.Entity)) {
                    continue;
                }

                entities[index++] = CraftEntity.getEntity(LunchBox.getServer(), (net.minecraft.entity.Entity) obj);
            }
        }

        return entities;
    }
    //TODO: need to rework getTileEntities().
    public BlockState[] getTileEntities() {
        int index = 0;
        net.minecraft.world.chunk.Chunk chunk = getHandle();
        BlockState[] entities = new BlockState[chunk.getTileEntityMap().size()];

        for (Object obj : chunk.getTileEntityMap().keySet().toArray()) {
            if (!(obj instanceof net.minecraft.world.chunk.Chunk)) {
                continue;
            }

            net.minecraft.world.chunk.Chunk position = (net.minecraft.world.chunk.Chunk) obj;
            entities[index++] = chunk.getBlock(position.chunkXPos + (chunk.xPosition << 4), position.chunkPosY, position.chunkZPos + (chunk.zPosition << 4)).getState();
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
        net.minecraft.world.chunk.Chunk chunk = this.getHandle();
        ExtendedBlockStorage[] cs = chunk.getBlockStorageArray();
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
                //TODO: work on this
                short[] biome = new short[4096];
                char[] biomeTemp = cs[hmap].getData();
                byte[] biomeRain = sectionBlockData[hmap] = new byte[2048];

                for (int world = 0; world < 4096; ++world) {
                    if (biomeTemp[world] != 0) {
                        IBlockState dat = (IBlockState) net.minecraft.block.Block.getStateId(biomeTemp[world]);

                        if (dat != null) {
                            biome[world] = (short) net.minecraft.block.Block.getIdFromBlock(dat.getBlock());
                            i = dat.getBlock().getMetaFromState(dat);
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
                if (cs[hmap].getSkylightArray() == null) {
                    sectionSkyLights[hmap] = CraftChunk.emptyData;
                } else {
                    sectionSkyLights[hmap] = new byte[2048];
                    System.arraycopy(cs[hmap].getSkylightArray().getData(), 0, sectionSkyLights[hmap], 0, 2048);
                }

                sectionEmitLights[hmap] = new byte[2048];
                System.arraycopy(cs[hmap].getBlocklightArray().getData(), 0, sectionEmitLights[hmap], 0, 2048);
            }
        }

        int[] aint = null;

        if (includeMaxBlockY) {
            aint = new int[256];
            System.arraycopy(chunk.getHeightMap(), 0, aint, 0, 256);
        }

        BiomeGenBase[] abiomebase = null;
        double[] adouble = null;
        double[] adouble1 = null;

        if (includeBiome || includeBiomeTempRain) {
            WorldChunkManager worldchunkmanager = chunk.getWorld().getWorldChunkManager();

            if (includeBiome) {
                abiomebase = new BiomeGenBase[256];

                for (int i = 0; i < 256; ++i) {
                    abiomebase[i] = chunk.getBiome(new BlockPos(i & 15, 0, i >> 4), worldchunkmanager);
                }
            }

            if (includeBiomeTempRain) {
                adouble = new double[256];
                adouble1 = new double[256];
                float[] afloat = getTemperatures(worldchunkmanager, this.getX() << 4, this.getZ() << 4);

                for (i = 0; i < 256; ++i) {
                    adouble[i] = (double) afloat[i];
                }

                afloat = worldchunkmanager.getRainfall(null, this.getX() << 4, this.getZ() << 4, 16, 16);

                for (i = 0; i < 256; ++i) {
                    adouble1[i] = (double) afloat[i];
                }
            }
        }

        World world = this.getWorld();

        return new CraftChunkSnapshot(this.getX(), this.getZ(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionBlockData, sectionSkyLights, sectionEmitLights, sectionEmpty, aint, abiomebase, adouble, adouble1);
    }

    public static ChunkSnapshot getEmptyChunkSnapshot(int x, int z, CraftWorld world, boolean includeBiome, boolean includeBiomeTempRain) {
        BiomeGenBase[] biome = null;
        double[] biomeTemp = null;
        double[] biomeRain = null;

        if (includeBiome || includeBiomeTempRain) {
            WorldServer w = world.getHandle();
            WorldChunkManager hSection = w.getWorldChunkManager();

            if (includeBiome) {
                biome = new BiomeGenBase[256];

                for (int blockIDs = 0; blockIDs < 256; ++blockIDs) {
                    biome[blockIDs] = world.getHandle().getBiomeGenForCoords(new BlockPos((x << 4) + (blockIDs & 15), 0, (z << 4) + (blockIDs >> 4)));
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

                afloat = hSection.getRainfall(null, x << 4, z << 4, 16, 16);

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
        BiomeGenBase[] biomes = chunkmanager.getBiomesForGeneration(null, chunkX, chunkZ, 16, 16);
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
