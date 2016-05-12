package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;

public class CraftChest extends CraftBlockState implements Chest {

    private final CraftWorld world;
    private final TileEntityChest chest;

    public CraftChest(Block block) {
        super(block);
        this.world = (CraftWorld) block.getWorld();
        this.chest = (TileEntityChest) this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }

    public CraftChest(Material material, TileEntityChest te) {
        super(material);
        this.chest = te;
        this.world = null;
    }

    public Inventory getBlockInventory() {
        return new CraftInventory(this.chest);
    }

    public Inventory getInventory() {
        int x = this.getX();
        int y = this.getY();
        int z = this.getZ();
        Object inventory = new CraftInventory(this.chest);

        if (!this.isPlaced()) {
            return (Inventory) inventory;
        } else {
            int id;

            if (this.world.getBlockTypeIdAt(x, y, z) == Material.CHEST.getId()) {
                id = Material.CHEST.getId();
            } else {
                if (this.world.getBlockTypeIdAt(x, y, z) != Material.TRAPPED_CHEST.getId()) {
                    throw new IllegalStateException("CraftChest is not a chest but is instead " + this.world.getBlockAt(x, y, z));
                }

                id = Material.TRAPPED_CHEST.getId();
            }

            CraftInventory right;

            if (this.world.getBlockTypeIdAt(x - 1, y, z) == id) {
                right = new CraftInventory((TileEntityChest) this.world.getHandle().getTileEntity(new BlockPos(x - 1, y, z)));
                inventory = new CraftInventoryDoubleChest(right, (CraftInventory) inventory);
            }

            if (this.world.getBlockTypeIdAt(x + 1, y, z) == id) {
                right = new CraftInventory((TileEntityChest) this.world.getHandle().getTileEntity(new BlockPos(x + 1, y, z)));
                inventory = new CraftInventoryDoubleChest((CraftInventory) inventory, right);
            }

            if (this.world.getBlockTypeIdAt(x, y, z - 1) == id) {
                right = new CraftInventory((TileEntityChest) this.world.getHandle().getTileEntity(new BlockPos(x, y, z - 1)));
                inventory = new CraftInventoryDoubleChest(right, (CraftInventory) inventory);
            }

            if (this.world.getBlockTypeIdAt(x, y, z + 1) == id) {
                right = new CraftInventory((TileEntityChest) this.world.getHandle().getTileEntity(new BlockPos(x, y, z + 1)));
                inventory = new CraftInventoryDoubleChest((CraftInventory) inventory, right);
            }

            return (Inventory) inventory;
        }
    }

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            this.chest.update();
        }

        return result;
    }

    public TileEntityChest getTileEntity() {
        return this.chest;
    }
}
