package org.bukkit.block;

import java.util.Collection;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;

public interface Block extends Metadatable {

    /** @deprecated */
    @Deprecated
    byte getData();

    Block getRelative(int i, int j, int k);

    Block getRelative(BlockFace blockface);

    Block getRelative(BlockFace blockface, int i);

    Material getType();

    /** @deprecated */
    @Deprecated
    int getTypeId();

    byte getLightLevel();

    byte getLightFromSky();

    byte getLightFromBlocks();

    World getWorld();

    int getX();

    int getY();

    int getZ();

    Location getLocation();

    Location getLocation(Location location);

    Chunk getChunk();

    /** @deprecated */
    @Deprecated
    void setData(byte b0);

    /** @deprecated */
    @Deprecated
    void setData(byte b0, boolean flag);

    void setType(Material material);

    void setType(Material material, boolean flag);

    /** @deprecated */
    @Deprecated
    boolean setTypeId(int i);

    /** @deprecated */
    @Deprecated
    boolean setTypeId(int i, boolean flag);

    /** @deprecated */
    @Deprecated
    boolean setTypeIdAndData(int i, byte b0, boolean flag);

    BlockFace getFace(Block block);

    BlockState getState();

    Biome getBiome();

    void setBiome(Biome biome);

    boolean isBlockPowered();

    boolean isBlockIndirectlyPowered();

    boolean isBlockFacePowered(BlockFace blockface);

    boolean isBlockFaceIndirectlyPowered(BlockFace blockface);

    int getBlockPower(BlockFace blockface);

    int getBlockPower();

    boolean isEmpty();

    boolean isLiquid();

    double getTemperature();

    double getHumidity();

    PistonMoveReaction getPistonMoveReaction();

    boolean breakNaturally();

    boolean breakNaturally(ItemStack itemstack);

    Collection getDrops();

    Collection getDrops(ItemStack itemstack);
}
