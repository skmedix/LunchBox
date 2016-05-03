package org.bukkit.craftbukkit.v1_8_R3.generator;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;

public final class CraftChunkData implements ChunkGenerator.ChunkData {

    private final int maxHeight;
    private final char[][] sections;

    public CraftChunkData(World world) {
        this(world.getMaxHeight());
    }

    CraftChunkData(int maxHeight) {
        if (maxHeight > 256) {
            throw new IllegalArgumentException("World height exceeded max chunk height");
        } else {
            this.maxHeight = maxHeight;
            this.sections = new char[16][];
        }
    }

    public int getMaxHeight() {
        return this.maxHeight;
    }

    public void setBlock(int x, int y, int z, Material material) {
        this.setBlock(x, y, z, material.getId());
    }

    public void setBlock(int x, int y, int z, MaterialData material) {
        this.setBlock(x, y, z, material.getItemTypeId(), material.getData());
    }

    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Material material) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, material.getId());
    }

    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, MaterialData material) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, material.getItemTypeId(), material.getData());
    }

    public Material getType(int x, int y, int z) {
        return Material.getMaterial(this.getTypeId(x, y, z));
    }

    public MaterialData getTypeAndData(int x, int y, int z) {
        return this.getType(x, y, z).getNewData(this.getData(x, y, z));
    }

    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, int blockId) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, blockId, 0);
    }

    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, int blockId, int data) {
        if (xMin <= 15 && yMin < this.maxHeight && zMin <= 15) {
            if (xMin < 0) {
                xMin = 0;
            }

            if (yMin < 0) {
                yMin = 0;
            }

            if (zMin < 0) {
                zMin = 0;
            }

            if (xMax > 16) {
                xMax = 16;
            }

            if (yMax > this.maxHeight) {
                yMax = this.maxHeight;
            }

            if (zMax > 16) {
                zMax = 16;
            }

            if (xMin < xMax && yMin < yMax && zMin < zMax) {
                char typeChar = (char) (blockId << 4 | data);
                int y;
                char[] section;
                int offsetBase;
                int z;
                int offset;

                if (xMin == 0 && xMax == 16) {
                    if (zMin == 0 && zMax == 16) {
                        for (y = yMin & 240; y < yMax; y += 16) {
                            section = this.getChunkSection(y, true);
                            if (y <= yMin) {
                                if (y + 16 > yMax) {
                                    Arrays.fill(section, (yMin & 15) << 8, (yMax & 15) << 8, typeChar);
                                } else {
                                    Arrays.fill(section, (yMin & 15) << 8, 4096, typeChar);
                                }
                            } else if (y + 16 >= yMax) {
                                Arrays.fill(section, 0, (yMax & 15) << 8, typeChar);
                            } else {
                                Arrays.fill(section, 0, 4096, typeChar);
                            }
                        }
                    } else {
                        for (y = yMin; y < yMax; ++y) {
                            section = this.getChunkSection(y, true);
                            offsetBase = (y & 15) << 8;
                            z = offsetBase | zMin << 4;
                            offset = offsetBase | zMax << 4;
                            Arrays.fill(section, z, offset, typeChar);
                        }
                    }
                } else {
                    for (y = yMin; y < yMax; ++y) {
                        section = this.getChunkSection(y, true);
                        offsetBase = (y & 15) << 8;

                        for (z = zMin; z < zMax; ++z) {
                            offset = offsetBase | z << 4;
                            Arrays.fill(section, offset | xMin, offset | xMax, typeChar);
                        }
                    }
                }

            }
        }
    }

    public void setBlock(int x, int y, int z, int blockId) {
        this.setBlock(x, y, z, blockId, (byte) 0);
    }

    public void setBlock(int x, int y, int z, int blockId, byte data) {
        this.setBlock(x, y, z, (char) (blockId << 4 | data));
    }

    public int getTypeId(int x, int y, int z) {
        if (x == (x & 15) && y >= 0 && y < this.maxHeight && z == (z & 15)) {
            char[] section = this.getChunkSection(y, false);

            return section == null ? 0 : section[(y & 15) << 8 | z << 4 | x] >> 4;
        } else {
            return 0;
        }
    }

    public byte getData(int x, int y, int z) {
        if (x == (x & 15) && y >= 0 && y < this.maxHeight && z == (z & 15)) {
            char[] section = this.getChunkSection(y, false);

            return section == null ? 0 : (byte) (section[(y & 15) << 8 | z << 4 | x] & 15);
        } else {
            return (byte) 0;
        }
    }

    private void setBlock(int x, int y, int z, char type) {
        if (x == (x & 15) && y >= 0 && y < this.maxHeight && z == (z & 15)) {
            char[] section = this.getChunkSection(y, true);

            section[(y & 15) << 8 | z << 4 | x] = type;
        }
    }

    private char[] getChunkSection(int y, boolean create) {
        char[] section = this.sections[y >> 4];

        if (create && section == null) {
            this.sections[y >> 4] = section = new char[4096];
        }

        return section;
    }

    char[][] getRawChunkData() {
        return this.sections;
    }
}
