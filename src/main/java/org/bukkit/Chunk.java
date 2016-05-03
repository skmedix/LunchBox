package org.bukkit;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

public interface Chunk {

    int getX();

    int getZ();

    World getWorld();

    Block getBlock(int i, int j, int k);

    ChunkSnapshot getChunkSnapshot();

    ChunkSnapshot getChunkSnapshot(boolean flag, boolean flag1, boolean flag2);

    Entity[] getEntities();

    BlockState[] getTileEntities();

    boolean isLoaded();

    boolean load(boolean flag);

    boolean load();

    boolean unload(boolean flag, boolean flag1);

    boolean unload(boolean flag);

    boolean unload();
}
