package org.spigotmc;

import gnu.trove.set.hash.TByteHashSet;
import java.util.Iterator;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

public class AntiXray {

    private static final CustomTimingsHandler update = new CustomTimingsHandler("xray - update");
    private static final CustomTimingsHandler obfuscate = new CustomTimingsHandler("xray - obfuscate");
    private final boolean[] obfuscateBlocks = new boolean[32767];
    private final byte[] replacementOres;
    private static int[] $SWITCH_TABLE$org$bukkit$World$Environment;

    public AntiXray(SpigotWorldConfig config) {
        int blocks;

        for (Iterator i = (config.engineMode == 1 ? config.hiddenBlocks : config.replaceBlocks).iterator(); i.hasNext(); this.obfuscateBlocks[blocks] = true) {
            blocks = ((Integer) i.next()).intValue();
        }

        TByteHashSet blocks1 = new TByteHashSet();
        Iterator iterator = config.hiddenBlocks.iterator();

        while (iterator.hasNext()) {
            Integer i1 = (Integer) iterator.next();
            Block block = Block.getById(i1.intValue());

            if (block != null && !block.isTileEntity()) {
                blocks1.add((byte) i1.intValue());
            }
        }

        this.replacementOres = blocks1.toArray();
    }

    public void updateNearbyBlocks(World world, BlockPosition position) {
        if (world.spigotConfig.antiXray) {
            AntiXray.update.startTiming();
            this.updateNearbyBlocks(world, position, 2, false);
            AntiXray.update.stopTiming();
        }

    }

    public void obfuscateSync(int chunkX, int chunkY, int bitmask, byte[] buffer, World world) {
        if (world.spigotConfig.antiXray) {
            AntiXray.obfuscate.startTiming();
            this.obfuscate(chunkX, chunkY, bitmask, buffer, world);
            AntiXray.obfuscate.stopTiming();
        }

    }

    public void obfuscate(int chunkX, int chunkY, int bitmask, byte[] buffer, World world) {
        if (world.spigotConfig.antiXray) {
            byte initialRadius = 1;
            int index = 0;
            int randomOre = 0;
            int startX = chunkX << 4;
            int startZ = chunkY << 4;
            byte replaceWithTypeId;

            switch ($SWITCH_TABLE$org$bukkit$World$Environment()[world.getWorld().getEnvironment().ordinal()]) {
            case 2:
                replaceWithTypeId = (byte) CraftMagicNumbers.getId(Blocks.NETHERRACK);
                break;

            case 3:
                replaceWithTypeId = (byte) CraftMagicNumbers.getId(Blocks.END_STONE);
                break;

            default:
                replaceWithTypeId = (byte) CraftMagicNumbers.getId(Blocks.STONE);
            }

            for (int i = 0; i < 16; ++i) {
                if ((bitmask & 1 << i) != 0) {
                    for (int y = 0; y < 16; ++y) {
                        for (int z = 0; z < 16; ++z) {
                            for (int x = 0; x < 16; ++x) {
                                if (index >= buffer.length) {
                                    ++index;
                                } else {
                                    int blockId = buffer[index << 1] & 255 | (buffer[(index << 1) + 1] & 255) << 8;

                                    blockId >>>= 4;
                                    if (this.obfuscateBlocks[blockId]) {
                                        if (!isLoaded(world, new BlockPosition(startX + x, (i << 4) + y, startZ + z), initialRadius)) {
                                            ++index;
                                            continue;
                                        }

                                        if (!hasTransparentBlockAdjacent(world, new BlockPosition(startX + x, (i << 4) + y, startZ + z), initialRadius)) {
                                            int newId = blockId;

                                            switch (world.spigotConfig.engineMode) {
                                            case 1:
                                                newId = replaceWithTypeId & 255;
                                                break;

                                            case 2:
                                                if (randomOre >= this.replacementOres.length) {
                                                    randomOre = 0;
                                                }

                                                newId = this.replacementOres[randomOre++] & 255;
                                            }

                                            newId <<= 4;
                                            buffer[index << 1] = (byte) (newId & 255);
                                            buffer[(index << 1) + 1] = (byte) (newId >> 8 & 255);
                                        }
                                    }

                                    ++index;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void updateNearbyBlocks(World world, BlockPosition position, int radius, boolean updateSelf) {
        if (world.isLoaded(position)) {
            Block block = world.getType(position).getBlock();

            if (updateSelf && this.obfuscateBlocks[Block.getId(block)]) {
                world.notify(position);
            }

            if (radius > 0) {
                this.updateNearbyBlocks(world, position.east(), radius - 1, true);
                this.updateNearbyBlocks(world, position.west(), radius - 1, true);
                this.updateNearbyBlocks(world, position.up(), radius - 1, true);
                this.updateNearbyBlocks(world, position.down(), radius - 1, true);
                this.updateNearbyBlocks(world, position.south(), radius - 1, true);
                this.updateNearbyBlocks(world, position.north(), radius - 1, true);
            }
        }

    }

    private static boolean isLoaded(World world, BlockPosition position, int radius) {
        return world.isLoaded(position) && (radius == 0 || isLoaded(world, position.east(), radius - 1) && isLoaded(world, position.west(), radius - 1) && isLoaded(world, position.up(), radius - 1) && isLoaded(world, position.down(), radius - 1) && isLoaded(world, position.south(), radius - 1) && isLoaded(world, position.north(), radius - 1));
    }

    private static boolean hasTransparentBlockAdjacent(World world, BlockPosition position, int radius) {
        return !isSolidBlock(world.getType(position, false).getBlock()) || radius > 0 && (hasTransparentBlockAdjacent(world, position.east(), radius - 1) || hasTransparentBlockAdjacent(world, position.west(), radius - 1) || hasTransparentBlockAdjacent(world, position.up(), radius - 1) || hasTransparentBlockAdjacent(world, position.down(), radius - 1) || hasTransparentBlockAdjacent(world, position.south(), radius - 1) || hasTransparentBlockAdjacent(world, position.north(), radius - 1));
    }

    private static boolean isSolidBlock(Block block) {
        return block.isOccluding() && block != Blocks.MOB_SPAWNER && block != Blocks.BARRIER;
    }

    static int[] $SWITCH_TABLE$org$bukkit$World$Environment() {
        int[] aint = AntiXray.$SWITCH_TABLE$org$bukkit$World$Environment;

        if (AntiXray.$SWITCH_TABLE$org$bukkit$World$Environment != null) {
            return aint;
        } else {
            int[] aint1 = new int[org.bukkit.World.values().length];

            try {
                aint1[org.bukkit.World.NETHER.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[org.bukkit.World.NORMAL.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[org.bukkit.World.THE_END.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            AntiXray.$SWITCH_TABLE$org$bukkit$World$Environment = aint1;
            return aint1;
        }
    }
}
