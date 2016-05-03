package org.bukkit.craftbukkit.v1_8_R3.chunkio;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkRegionLoader;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.util.AsynchronousExecutor;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;
import org.bukkit.event.world.ChunkLoadEvent;

class ChunkIOProvider implements AsynchronousExecutor.CallBackProvider {

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public Chunk callStage1(QueuedChunk queuedChunk) throws RuntimeException {
        try {
            ChunkRegionLoader ex = queuedChunk.loader;
            Object[] data = ex.loadChunk(queuedChunk.world, queuedChunk.x, queuedChunk.z);

            if (data != null) {
                queuedChunk.compound = (NBTTagCompound) data[1];
                return (Chunk) data[0];
            } else {
                return null;
            }
        } catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        }
    }

    public void callStage2(QueuedChunk queuedChunk, Chunk chunk) throws RuntimeException {
        if (chunk == null) {
            queuedChunk.provider.originalGetChunkAt(queuedChunk.x, queuedChunk.z);
        } else {
            queuedChunk.loader.loadEntities(chunk, queuedChunk.compound.getCompound("Level"), queuedChunk.world);
            chunk.setLastSaved(queuedChunk.provider.world.getTime());
            queuedChunk.provider.chunks.put(LongHash.toLong(queuedChunk.x, queuedChunk.z), chunk);
            chunk.addEntities();
            if (queuedChunk.provider.chunkProvider != null) {
                queuedChunk.provider.world.timings.syncChunkLoadStructuresTimer.startTiming();
                queuedChunk.provider.chunkProvider.recreateStructures(chunk, queuedChunk.x, queuedChunk.z);
                queuedChunk.provider.world.timings.syncChunkLoadStructuresTimer.stopTiming();
            }

            CraftServer server = queuedChunk.provider.world.getServer();

            if (server != null) {
                server.getPluginManager().callEvent(new ChunkLoadEvent(chunk.bukkitChunk, false));
            }

            for (int x = -2; x < 3; ++x) {
                for (int z = -2; z < 3; ++z) {
                    if (x != 0 || z != 0) {
                        Chunk neighbor = queuedChunk.provider.getChunkIfLoaded(chunk.locX + x, chunk.locZ + z);

                        if (neighbor != null) {
                            neighbor.setNeighborLoaded(-x, -z);
                            chunk.setNeighborLoaded(x, z);
                        }
                    }
                }
            }

            chunk.loadNearby(queuedChunk.provider, queuedChunk.provider, queuedChunk.x, queuedChunk.z);
        }
    }

    public void callStage3(QueuedChunk queuedChunk, Chunk chunk, Runnable runnable) throws RuntimeException {
        runnable.run();
    }

    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "Chunk I/O Executor Thread-" + this.threadNumber.getAndIncrement());

        thread.setDaemon(true);
        return thread;
    }
}
