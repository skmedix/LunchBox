package org.bukkit.block;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.Metadatable;

public interface BlockState extends Metadatable {

    Block getBlock();

    MaterialData getData();

    Material getType();

    /** @deprecated */
    @Deprecated
    int getTypeId();

    byte getLightLevel();

    World getWorld();

    int getX();

    int getY();

    int getZ();

    Location getLocation();

    Location getLocation(Location location);

    Chunk getChunk();

    void setData(MaterialData materialdata);

    void setType(Material material);

    /** @deprecated */
    @Deprecated
    boolean setTypeId(int i);

    boolean update();

    boolean update(boolean flag);

    boolean update(boolean flag, boolean flag1);

    /** @deprecated */
    @Deprecated
    byte getRawData();

    /** @deprecated */
    @Deprecated
    void setRawData(byte b0);

    boolean isPlaced();
}
