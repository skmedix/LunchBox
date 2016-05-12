package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.projectiles.CraftBlockProjectileSource;
import org.bukkit.inventory.Inventory;
import org.bukkit.projectiles.BlockProjectileSource;

public class CraftDispenser extends CraftBlockState implements Dispenser {

    private final CraftWorld world;
    private final TileEntityDispenser dispenser;

    public CraftDispenser(Block block) {
        super(block);
        this.world = (CraftWorld) block.getWorld();
        this.dispenser = (TileEntityDispenser) this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }

    public CraftDispenser(Material material, TileEntityDispenser te) {
        super(material);
        this.world = null;
        this.dispenser = te;
    }

    public Inventory getInventory() {
        return new CraftInventory(this.dispenser);
    }

    public BlockProjectileSource getBlockProjectileSource() {
        Block block = this.getBlock();

        return block.getType() != Material.DISPENSER ? null : new CraftBlockProjectileSource(this.dispenser);
    }

    public boolean dispense() {
        Block block = this.getBlock();
        //todo: redo this.
        if (block.getType() == Material.DISPENSER) {
            BlockDispenser dispense = (BlockDispenser) Blocks.dispenser;

            dispense.dispense(this.world.getHandle(), new BlockPos(this.getX(), this.getY(), this.getZ()));
            return true;
        } else {
            return false;
        }
    }

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            this.dispenser.updateContainingBlockInfo();
        }

        return result;
    }

    public TileEntityDispenser getTileEntity() {
        return this.dispenser;
    }
}
