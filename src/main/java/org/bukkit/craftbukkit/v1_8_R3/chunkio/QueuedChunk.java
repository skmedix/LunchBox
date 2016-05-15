package org.bukkit.craftbukkit.v1_8_R3.chunkio;

import net.minecraft.server.v1_8_R3.ChunkProviderServer;
import net.minecraft.server.v1_8_R3.ChunkRegionLoader;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;
//todo: save for later.
class QueuedChunk {

    final int x;
    final int z;
    final ChunkRegionLoader loader;
    final World world;
    final ChunkProviderServer provider;
    NBTTagCompound compound;

    public QueuedChunk(int x, int z, ChunkRegionLoader loader, World world, ChunkProviderServer provider) {
        this.x = x;
        this.z = z;
        this.loader = loader;
        this.world = world;
        this.provider = provider;
    }

    public int hashCode() {
        return this.x * 31 + this.z * 29 ^ this.world.hashCode();
    }

    public boolean equals(Object object) {
        if (object instanceof QueuedChunk) {
            QueuedChunk other = (QueuedChunk) object;

            return this.x == other.x && this.z == other.z && this.world == other.world;
        } else {
            return false;
        }
    }
}
