package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.block.BlockDropper;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.util.BlockPos;
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
            BlockDropper drop = (BlockDropper) Blocks.dropper;
            //todo
            drop.dispense(this.world.getHandle(), new BlockPos(this.getX(), this.getY(), this.getZ()));
        }

    }

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            this.dropper.updateContainingBlockInfo();
        }

        return result;
    }

    public TileEntityDropper getTileEntity() {
        return this.dropper;
    }
}
