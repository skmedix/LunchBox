package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.server.v1_8_R3.BlockDropper;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.TileEntityDropper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftDropper extends CraftBlockState implements Dropper {

    private final CraftWorld world;
    private final TileEntityDropper dropper;

    public CraftDropper(Block block) {
        super(block);
        this.world = (CraftWorld) block.getWorld();
        this.dropper = (TileEntityDropper) this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }

    public CraftDropper(Material material, TileEntityDropper te) {
        super(material);
        this.world = null;
        this.dropper = te;
    }

    public Inventory getInventory() {
        return new CraftInventory(this.dropper);
    }

    public void drop() {
        Block block = this.getBlock();

        if (block.getType() == Material.DROPPER) {
            BlockDropper drop = (BlockDropper) Blocks.DROPPER;

            drop.dispense(this.world.getHandle(), new BlockPosition(this.getX(), this.getY(), this.getZ()));
        }

    }

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            this.dropper.update();
        }

        return result;
    }

    public TileEntityDropper getTileEntity() {
        return this.dropper;
    }
}
