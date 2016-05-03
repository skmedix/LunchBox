package org.bukkit.craftbukkit.v1_8_R3.chunkio;

import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkProviderServer;
import net.minecraft.server.v1_8_R3.ChunkRegionLoader;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.util.AsynchronousExecutor;

public class ChunkIOExecutor {

    static final int BASE_THREADS = 1;
    static final int PLAYERS_PER_THREAD = 50;
    private static final AsynchronousExecutor instance = new AsynchronousExecutor(new ChunkIOProvider(), 1);

    public static Chunk syncChunkLoad(World world, ChunkRegionLoader loader, ChunkProviderServer provider, int x, int z) {
        return (Chunk) ChunkIOExecutor.instance.getSkipQueue(new QueuedChunk(x, z, loader, world, provider));
    }

    public static void queueChunkLoad(World world, ChunkRegionLoader loader, ChunkProviderServer provider, int x, int z, Runnable runnable) {
        ChunkIOExecutor.instance.add(new QueuedChunk(x, z, loader, world, provider), runnable);
    }

    public static void dropQueuedChunkLoad(World world, int x, int z, Runnable runnable) {
        ChunkIOExecutor.instance.drop(new QueuedChunk(x, z, (ChunkRegionLoader) null, world, (ChunkProviderServer) null), runnable);
    }

    public static void adjustPoolSize(int players) {
        int size = Math.max(1, (int) Math.ceil((double) (players / 50)));

        ChunkIOExecutor.instance.setActiveThreads(size);
    }

    public static void tick() {
        ChunkIOExecutor.instance.finishActive();
    }
}
