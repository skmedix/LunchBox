package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.server.v1_8_R3.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityBrewingStand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryBrewer;
import org.bukkit.inventory.BrewerInventory;

public class CraftBrewingStand extends CraftBlockState implements BrewingStand {

    private final TileEntityBrewingStand brewingStand;

    public CraftBrewingStand(Block block) {
        super(block);
        this.brewingStand = (TileEntityBrewingStand) ((CraftWorld) block.getWorld()).getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }

    public CraftBrewingStand(Material material, TileEntityBrewingStand te) {
        super(material);
        this.brewingStand = te;
    }

    public BrewerInventory getInventory() {
        return new CraftInventoryBrewer(this.brewingStand);
    }

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            this.brewingStand.update();
        }

        return result;
    }
    //todo
    public int getBrewingTime() {
        return this.brewingStand.brewTime;
    }

    public void setBrewingTime(int brewTime) {
        this.brewingStand.brewTime = brewTime;
    }

    public TileEntityBrewingStand getTileEntity() {
        return this.brewingStand;
    }
}
