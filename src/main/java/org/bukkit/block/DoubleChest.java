package org.bukkit.block;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class DoubleChest implements InventoryHolder {

    private DoubleChestInventory inventory;

    public DoubleChest(DoubleChestInventory chest) {
        this.inventory = chest;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public InventoryHolder getLeftSide() {
        return this.inventory.getLeftSide().getHolder();
    }

    public InventoryHolder getRightSide() {
        return this.inventory.getRightSide().getHolder();
    }

    public Location getLocation() {
        return new Location(this.getWorld(), this.getX(), this.getY(), this.getZ());
    }

    public World getWorld() {
        return ((Chest) this.getLeftSide()).getWorld();
    }

    public double getX() {
        return 0.5D * (double) (((Chest) this.getLeftSide()).getX() + ((Chest) this.getRightSide()).getX());
    }

    public double getY() {
        return 0.5D * (double) (((Chest) this.getLeftSide()).getY() + ((Chest) this.getRightSide()).getY());
    }

    public double getZ() {
        return 0.5D * (double) (((Chest) this.getLeftSide()).getZ() + ((Chest) this.getRightSide()).getZ());
    }
}
