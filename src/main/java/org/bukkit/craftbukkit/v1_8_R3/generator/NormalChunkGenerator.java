package org.bukkit.craftbukkit.v1_8_R3.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.EnumCreatureType;
import net.minecraft.server.v1_8_R3.IChunkProvider;
import net.minecraft.server.v1_8_R3.IProgressUpdate;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class NormalChunkGenerator extends InternalChunkGenerator {

    private final IChunkProvider provider;

    public NormalChunkGenerator(World world, long seed) {
        this.provider = world.worldProvider.getChunkProvider();
    }

    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return ((CraftWorld) world).getHandle().worldProvider.canSpawn(x, z);
    }

    public List getDefaultPopulators(org.bukkit.World world) {
        return new ArrayList();
    }

    public boolean isChunkLoaded(int i, int i1) {
        return this.provider.isChunkLoaded(i, i1);
    }

    public Chunk getOrCreateChunk(int i, int i1) {
        return this.provider.getOrCreateChunk(i, i1);
    }

    public Chunk getChunkAt(BlockPosition blockPosition) {
        return this.provider.getChunkAt(blockPosition);
    }

    public void getChunkAt(IChunkProvider icp, int i, int i1) {
        this.provider.getChunkAt(icp, i, i1);
    }

    public boolean a(IChunkProvider iChunkProvider, Chunk chunk, int i, int i1) {
        return this.provider.a(this.provider, chunk, i, i1);
    }

    public boolean saveChunks(boolean bln, IProgressUpdate ipu) {
        return this.provider.saveChunks(bln, ipu);
    }

    public boolean unloadChunks() {
        return this.provider.unloadChunks();
    }

    public boolean canSave() {
        return this.provider.canSave();
    }

    public List getMobsFor(EnumCreatureType ect, BlockPosition position) {
        return this.provider.getMobsFor(ect, position);
    }

    public BlockPosition findNearestMapFeature(World world, String string, BlockPosition position) {
        return this.provider.findNearestMapFeature(world, string, position);
    }

    public int getLoadedChunks() {
        return 0;
    }

    public void recreateStructures(Chunk chunk, int i, int i1) {
        this.provider.recreateStructures(chunk, i, i1);
    }

    public String getName() {
        return "NormalWorldGenerator";
    }

    public void c() {}
}
