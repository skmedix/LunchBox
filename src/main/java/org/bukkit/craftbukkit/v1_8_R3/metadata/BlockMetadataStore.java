package org.bukkit.craftbukkit.v1_8_R3.metadata;

import java.util.List;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class BlockMetadataStore extends MetadataStoreBase<Block> implements MetadataStore<Block> {

    private final World owningWorld;

    public BlockMetadataStore(World owningWorld) {
        this.owningWorld = owningWorld;
    }
    @Override
    protected String disambiguate(Block block, String metadataKey) {
        return Integer.toString(block.getX()) + ":" + Integer.toString(block.getY()) + ":" + Integer.toString(block.getZ()) + ":" + metadataKey;
    }
    @Override
    public List getMetadata(Block block, String metadataKey) {
        if (block.getWorld() == this.owningWorld) {
            return super.getMetadata(block, metadataKey);
        } else {
            throw new IllegalArgumentException("Block does not belong to world " + this.owningWorld.getName());
        }
    }
    @Override
    public boolean hasMetadata(Block block, String metadataKey) {
        if (block.getWorld() == this.owningWorld) {
            return super.hasMetadata(block, metadataKey);
        } else {
            throw new IllegalArgumentException("Block does not belong to world " + this.owningWorld.getName());
        }
    }
    @Override
    public void removeMetadata(Block block, String metadataKey, Plugin owningPlugin) {
        if (block.getWorld() == this.owningWorld) {
            super.removeMetadata(block, metadataKey, owningPlugin);
        } else {
            throw new IllegalArgumentException("Block does not belong to world " + this.owningWorld.getName());
        }
    }
    @Override
    public void setMetadata(Block block, String metadataKey, MetadataValue newMetadataValue) {
        if (block.getWorld() == this.owningWorld) {
            super.setMetadata(block, metadataKey, newMetadataValue);
        } else {
            throw new IllegalArgumentException("Block does not belong to world " + this.owningWorld.getName());
        }
    }
}
