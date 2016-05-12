package org.bukkit.craftbukkit.v1_8_R3.block;

import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class CraftBlockState implements BlockState {

    private final CraftWorld world;
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;
    protected int type;
    protected MaterialData data;
    protected int flag;
    protected final byte light;

    public CraftBlockState(Block block) {
        this.world = (CraftWorld) block.getWorld();
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.type = block.getTypeId();
        this.light = block.getLightLevel();
        this.chunk = (CraftChunk) block.getChunk();
        this.flag = 3;
        this.createData(block.getData());
    }

    public CraftBlockState(Block block, int flag) {
        this(block);
        this.flag = flag;
    }

    public CraftBlockState(Material material) {
        this.world = null;
        this.type = material.getId();
        this.light = 0;
        this.chunk = null;
        this.x = this.y = this.z = 0;
    }

    public static CraftBlockState getBlockState(World world, int x, int y, int z) {
        return new CraftBlockState((Block) world.getBlockState(new BlockPos(x, y, z)));
    }

    public static CraftBlockState getBlockState(World world, int x, int y, int z, int flag) {
        return new CraftBlockState((Block) world.getBlockState(new BlockPos(x, y, z)), flag);
    }

    public org.bukkit.World getWorld() {
        this.requirePlaced();
        return this.world;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Chunk getChunk() {
        this.requirePlaced();
        return this.chunk;
    }

    public void setData(MaterialData data) {
        Material mat = this.getType();

        if (mat != null && mat.getData() != null) {
            if (data.getClass() != mat.getData() && data.getClass() != MaterialData.class) {
                throw new IllegalArgumentException("Provided data is not of type " + mat.getData().getName() + ", found " + data.getClass().getName());
            }

            this.data = data;
        } else {
            this.data = data;
        }

    }

    public MaterialData getData() {
        return this.data;
    }

    public void setType(Material type) {
        this.setTypeId(type.getId());
    }

    public boolean setTypeId(int type) {
        if (this.type != type) {
            this.type = type;
            this.createData((byte) 0);
        }

        return true;
    }

    public Material getType() {
        return Material.getMaterial(this.getTypeId());
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return this.flag;
    }

    public int getTypeId() {
        return this.type;
    }

    public byte getLightLevel() {
        return this.light;
    }

    public Block getBlock() {
        this.requirePlaced();
        return this.world.getBlockAt(this.x, this.y, this.z);
    }

    public boolean update() {
        return this.update(false);
    }

    public boolean update(boolean force) {
        return this.update(force, true);
    }

    public boolean update(boolean force, boolean applyPhysics) {
        this.requirePlaced();
        Block block = this.getBlock();

        if (block.getType() != this.getType() && !force) {
            return false;
        } else {
            block.setTypeIdAndData(this.getTypeId(), this.getRawData(), applyPhysics);
            this.world.getHandle().notifyLightSet(new BlockPos(this.x, this.y, this.z));
            return true;
        }
    }

    private void createData(byte data) {
        Material mat = this.getType();

        if (mat != null && mat.getData() != null) {
            this.data = mat.getNewData(data);
        } else {
            this.data = new MaterialData(this.type, data);
        }

    }

    public byte getRawData() {
        return this.data.getData();
    }

    public Location getLocation() {
        return new Location(this.world, (double) this.x, (double) this.y, (double) this.z);
    }

    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(this.world);
            loc.setX((double) this.x);
            loc.setY((double) this.y);
            loc.setZ((double) this.z);
            loc.setYaw(0.0F);
            loc.setPitch(0.0F);
        }

        return loc;
    }

    public void setRawData(byte data) {
        this.data.setData(data);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            CraftBlockState other = (CraftBlockState) obj;

            return this.world != other.world && (this.world == null || !this.world.equals(other.world)) ? false : (this.x != other.x ? false : (this.y != other.y ? false : (this.z != other.z ? false : (this.type != other.type ? false : this.data == other.data || this.data != null && this.data.equals(other.data)))));
        }
    }

    public int hashCode() {
        byte hash = 7;
        int hash1 = 73 * hash + (this.world != null ? this.world.hashCode() : 0);

        hash1 = 73 * hash1 + this.x;
        hash1 = 73 * hash1 + this.y;
        hash1 = 73 * hash1 + this.z;
        hash1 = 73 * hash1 + this.type;
        hash1 = 73 * hash1 + (this.data != null ? this.data.hashCode() : 0);
        return hash1;
    }

    public TileEntity getTileEntity() {
        return null;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.requirePlaced();
        this.chunk.getCraftWorld().getBlockMetadata().setMetadata(this.getBlock(), metadataKey, newMetadataValue);
    }

    public List getMetadata(String metadataKey) {
        this.requirePlaced();
        return this.chunk.getCraftWorld().getBlockMetadata().getMetadata(this.getBlock(), metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        this.requirePlaced();
        return this.chunk.getCraftWorld().getBlockMetadata().hasMetadata(this.getBlock(), metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.requirePlaced();
        this.chunk.getCraftWorld().getBlockMetadata().removeMetadata(this.getBlock(), metadataKey, owningPlugin);
    }

    public boolean isPlaced() {
        return this.world != null;
    }

    protected void requirePlaced() {
        if (!this.isPlaced()) {
            throw new IllegalStateException("The blockState must be placed to call this method");
        }
    }
}
