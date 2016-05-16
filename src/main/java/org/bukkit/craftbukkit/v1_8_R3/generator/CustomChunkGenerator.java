package org.bukkit.craftbukkit.v1_8_R3.generator;

import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.EnumCreatureType;
import net.minecraft.server.v1_8_R3.IChunkProvider;
import net.minecraft.server.v1_8_R3.IProgressUpdate;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldGenStronghold;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.MapGenStronghold;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.generator.ChunkGenerator;

public class CustomChunkGenerator extends InternalChunkGenerator {

    private final ChunkGenerator generator;
    private final WorldServer world;
    private final Random random;
    private final MapGenStronghold strongholdGen = new MapGenStronghold();

    public CustomChunkGenerator(World world, long seed, ChunkGenerator generator) {
        this.world = (WorldServer) world;
        this.generator = generator;
        this.random = new Random(seed);
    }

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public Chunk getOrCreateChunk(int x, int z) {
        this.random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        CustomChunkGenerator.CustomBiomeGrid biomegrid = new CustomChunkGenerator.CustomBiomeGrid((CustomChunkGenerator.CustomBiomeGrid) null);

        biomegrid.biome = new BiomeGenBase[256];
        this.world.getWorldChunkManager().loadBlockGeneratorData(biomegrid.biome, x << 4, z << 4, 16, 16);
        CraftChunkData data = (CraftChunkData) this.generator.generateChunkData(this.world.getWorld(), this.random, x, z, biomegrid);
        Chunk chunk;
        ChunkSection[] i;
        int types;
        int ydim;
        char[] scnt;
        int sec;

        if (data != null) {
            char[][] biomeIndex = data.getRawChunkData();

            chunk = new Chunk(this.world, x, z);
            i = chunk.getSections();
            types = Math.min(i.length, biomeIndex.length);

            for (ydim = 0; ydim < types; ++ydim) {
                if (biomeIndex[ydim] != null) {
                    scnt = biomeIndex[ydim];
                    char csect = 0;

                    for (sec = 0; sec < 4096; ++sec) {
                        if (Block.d.a(scnt[sec]) == null) {
                            scnt[sec] = 0;
                        }

                        csect |= scnt[sec];
                    }

                    if (csect != 0) {
                        i[ydim] = new ChunkSection(ydim << 4, true, scnt);
                    }
                }
            }
        } else {
            short[][] ashort = this.generator.generateExtBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);
            Block cs;

            if (ashort != null) {
                chunk = new Chunk(this.world, x, z);
                i = chunk.getSections();
                types = Math.min(i.length, ashort.length);

                for (ydim = 0; ydim < types; ++ydim) {
                    if (ashort[ydim] != null) {
                        scnt = new char[4096];
                        short[] ashort1 = ashort[ydim];

                        for (sec = 0; sec < ashort1.length; ++sec) {
                            cs = Block.getById(ashort1[sec]);
                            scnt[sec] = (char) Block.d.b(cs.getBlockData());
                        }

                        i[ydim] = new ChunkSection(ydim << 4, true, scnt);
                    }
                }
            } else {
                byte[][] abyte = this.generator.generateBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);
                int i;

                if (abyte != null) {
                    chunk = new Chunk(this.world, x, z);
                    ChunkSection[] achunksection = chunk.getSections();

                    ydim = Math.min(achunksection.length, abyte.length);

                    for (i = 0; i < ydim; ++i) {
                        if (abyte[i] != null) {
                            char[] achar = new char[4096];

                            for (sec = 0; sec < achar.length; ++sec) {
                                cs = Block.getById(abyte[i][sec] & 255);
                                achar[sec] = (char) Block.d.b(cs.getBlockData());
                            }

                            achunksection[i] = new ChunkSection(i << 4, true, achar);
                        }
                    }
                } else {
                    byte[] abyte1 = this.generator.generate(this.world.getWorld(), this.random, x, z);

                    ydim = abyte1.length / 256;
                    i = ydim / 16;
                    chunk = new Chunk(this.world, x, z);
                    ChunkSection[] achunksection1 = chunk.getSections();

                    i = Math.min(i, achunksection1.length);

                    for (sec = 0; sec < i; ++sec) {
                        ChunkSection chunksection = null;
                        char[] csbytes = null;

                        for (int cy = 0; cy < 16; ++cy) {
                            int cyoff = cy | sec << 4;

                            for (int cx = 0; cx < 16; ++cx) {
                                int cxyoff = cx * ydim * 16 + cyoff;

                                for (int cz = 0; cz < 16; ++cz) {
                                    byte blk = abyte1[cxyoff + cz * ydim];

                                    if (blk != 0) {
                                        if (chunksection == null) {
                                            chunksection = achunksection1[sec] = new ChunkSection(sec << 4, true);
                                            csbytes = chunksection.getIdArray();
                                        }

                                        Block b = Block.getById(blk & 255);

                                        csbytes[cy << 8 | cz << 4 | cx] = (char) Block.d.b(b.getBlockData());
                                    }
                                }
                            }
                        }

                        if (chunksection != null) {
                            chunksection.recalcBlockCounts();
                        }
                    }
                }
            }
        }

        byte[] abyte2 = chunk.getBiomeIndex();

        for (int j = 0; j < abyte2.length; ++j) {
            abyte2[j] = (byte) (biomegrid.biome[j].id & 255);
        }

        chunk.initLighting();
        return chunk;
    }

    public Chunk getChunkAt(BlockPosition blockPosition) {
        return this.getChunkAt(blockPosition.getX() >> 4, blockPosition.getZ() >> 4);
    }

    public void getChunkAt(IChunkProvider icp, int i, int i1) {}

    public boolean a(IChunkProvider iChunkProvider, Chunk chunk, int i, int i1) {
        return false;
    }

    public boolean saveChunks(boolean bln, IProgressUpdate ipu) {
        return true;
    }

    public boolean unloadChunks() {
        return false;
    }

    public boolean canSave() {
        return true;
    }

    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        return this.generator.generate(world, random, x, z);
    }

    public byte[][] generateBlockSections(org.bukkit.World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biomes) {
        return this.generator.generateBlockSections(world, random, x, z, biomes);
    }

    public short[][] generateExtBlockSections(org.bukkit.World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biomes) {
        return this.generator.generateExtBlockSections(world, random, x, z, biomes);
    }

    public Chunk getChunkAt(int x, int z) {
        return this.getOrCreateChunk(x, z);
    }

    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return this.generator.canSpawn(world, x, z);
    }

    public List getDefaultPopulators(org.bukkit.World world) {
        return this.generator.getDefaultPopulators(world);
    }

    public List getMobsFor(EnumCreatureType type, BlockPosition position) {
        BiomeBase biomebase = this.world.getBiome(position);

        return biomebase == null ? null : biomebase.getMobs(type);
    }

    public BlockPosition findNearestMapFeature(World world, String type, BlockPosition position) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.getNearestGeneratedFeature(world, position) : null;
    }

    public void recreateStructures(int i, int j) {}

    public int getLoadedChunks() {
        return 0;
    }

    public void recreateStructures(Chunk chunk, int i, int i1) {}

    public String getName() {
        return "CustomChunkGenerator";
    }

    public void c() {}

    private static class CustomBiomeGrid implements ChunkGenerator.BiomeGrid {

        BiomeGenBase[] biome;

        private CustomBiomeGrid() {}

        public Biome getBiome(int x, int z) {
            return CraftBlock.biomeBaseToBiome(this.biome[z << 4 | x]);
        }

        public void setBiome(int x, int z, Biome bio) {
            this.biome[z << 4 | x] = CraftBlock.biomeToBiomeBase(bio);
        }

        CustomBiomeGrid(CustomChunkGenerator.CustomBiomeGrid customchunkgenerator_custombiomegrid) {
            this();
        }
    }
}
