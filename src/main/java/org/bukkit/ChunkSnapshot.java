package org.bukkit;

import org.bukkit.block.Biome;

public interface ChunkSnapshot {

    int getX();

    int getZ();

    String getWorldName();

    /** @deprecated */
    @Deprecated
    int getBlockTypeId(int i, int j, int k);

    /** @deprecated */
    @Deprecated
    int getBlockData(int i, int j, int k);

    int getBlockSkyLight(int i, int j, int k);

    int getBlockEmittedLight(int i, int j, int k);

    int getHighestBlockYAt(int i, int j);

    Biome getBiome(int i, int j);

    double getRawBiomeTemperature(int i, int j);

    double getRawBiomeRainfall(int i, int j);

    long getCaptureFullTime();

    boolean isSectionEmpty(int i);
}
