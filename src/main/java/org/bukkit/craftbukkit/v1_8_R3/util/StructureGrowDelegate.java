package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.material.MaterialData;

public class StructureGrowDelegate implements BlockChangeDelegate {

    private final CraftWorld world;
    private final List blocks = new ArrayList();

    public StructureGrowDelegate(WorldServer world) {
        this.world = (CraftWorld) (org.bukkit.World) world;
    }

    public boolean setRawTypeId(int x, int y, int z, int type) {
        return this.setRawTypeIdAndData(x, y, z, type, 0);
    }

    public boolean setRawTypeIdAndData(int x, int y, int z, int type, int data) {
        BlockState state = this.world.getBlockAt(x, y, z).getState();

        state.setTypeId(type);
        state.setData(new MaterialData(type, (byte) data));
        this.blocks.add(state);
        return true;
    }

    public boolean setTypeId(int x, int y, int z, int typeId) {
        return this.setRawTypeId(x, y, z, typeId);
    }

    public boolean setTypeIdAndData(int x, int y, int z, int typeId, int data) {
        return this.setRawTypeIdAndData(x, y, z, typeId, data);
    }

    public int getTypeId(int x, int y, int z) {
        Iterator iterator = this.blocks.iterator();

        BlockState state;

        do {
            if (!iterator.hasNext()) {
                return this.world.getBlockTypeIdAt(x, y, z);
            }

            state = (BlockState) iterator.next();
        } while (state.getX() != x || state.getY() != y || state.getZ() != z);

        return state.getTypeId();
    }

    public int getHeight() {
        return this.world.getMaxHeight();
    }

    public List getBlocks() {
        return this.blocks;
    }

    public boolean isEmpty(int x, int y, int z) {
        Iterator iterator = this.blocks.iterator();

        BlockState state;

        do {
            if (!iterator.hasNext()) {
                return this.world.getBlockAt(x, y, z).isEmpty();
            }

            state = (BlockState) iterator.next();
        } while (state.getX() != x || state.getY() != y || state.getZ() != z);

        if (Block.getBlockById(state.getTypeId()) == Blocks.air) {
            return true;
        } else {
            return false;
        }
    }
}
