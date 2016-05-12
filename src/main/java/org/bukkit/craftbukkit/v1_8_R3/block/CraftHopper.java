package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.server.v1_8_R3.TileEntityHopper;
import net.minecraft.tileentity.TileEntityHopper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftHopper extends CraftBlockState implements Hopper {

    private final TileEntityHopper hopper;

    public CraftHopper(Block block) {
        super(block);
        this.hopper = (TileEntityHopper) ((CraftWorld) block.getWorld()).getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }

    public CraftHopper(Material material, TileEntityHopper te) {
        super(material);
        this.hopper = te;
    }

    public Inventory getInventory() {
        return new CraftInventory(this.hopper);
    }

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            this.hopper.update();
        }

        return result;
    }

    public TileEntityHopper getTileEntity() {
        return this.hopper;
    }
}
