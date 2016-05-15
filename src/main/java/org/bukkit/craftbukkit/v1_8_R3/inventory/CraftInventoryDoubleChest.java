package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.server.v1_8_R3.ITileInventory;
import net.minecraft.server.v1_8_R3.InventoryLargeChest;
import net.minecraft.world.ILockableContainer;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
//todo: come back to this later.
public class CraftInventoryDoubleChest extends CraftInventory implements DoubleChestInventory {

    private final CraftInventory left;
    private final CraftInventory right;

    public CraftInventoryDoubleChest(CraftInventory left, CraftInventory right) {
        super(new InventoryLargeChest("Large chest", (ILockableContainer) left.getInventory(), (ILockableContainer) right.getInventory()));
        this.left = left;
        this.right = right;
    }

    public CraftInventoryDoubleChest(InventoryLargeChest largeChest) {
        super(largeChest);
        if (largeChest.left instanceof InventoryLargeChest) {
            this.left = new CraftInventoryDoubleChest((InventoryLargeChest) largeChest.left);
        } else {
            this.left = new CraftInventory(largeChest.left);
        }

        if (largeChest.right instanceof InventoryLargeChest) {
            this.right = new CraftInventoryDoubleChest((InventoryLargeChest) largeChest.right);
        } else {
            this.right = new CraftInventory(largeChest.right);
        }

    }

    public Inventory getLeftSide() {
        return this.left;
    }

    public Inventory getRightSide() {
        return this.right;
    }

    public void setContents(ItemStack[] items) {
        if (this.getInventory().getContents().length < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + this.getInventory().getContents().length + " or less");
        } else {
            ItemStack[] leftItems = new ItemStack[this.left.getSize()];
            ItemStack[] rightItems = new ItemStack[this.right.getSize()];

            System.arraycopy(items, 0, leftItems, 0, Math.min(this.left.getSize(), items.length));
            this.left.setContents(leftItems);
            if (items.length >= this.left.getSize()) {
                System.arraycopy(items, this.left.getSize(), rightItems, 0, Math.min(this.right.getSize(), items.length - this.left.getSize()));
                this.right.setContents(rightItems);
            }

        }
    }

    public DoubleChest getHolder() {
        return new DoubleChest(this);
    }
}
