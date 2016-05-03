package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryWrapper implements IInventory {

    private final Inventory inventory;
    private final List viewers = new ArrayList();

    public InventoryWrapper(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getSize() {
        return this.inventory.getSize();
    }

    public ItemStack getItem(int i) {
        return CraftItemStack.asNMSCopy(this.inventory.getItem(i));
    }

    public ItemStack splitStack(int i, int j) {
        ItemStack stack = this.getItem(i);

        if (stack == null) {
            return null;
        } else {
            ItemStack result;

            if (stack.count <= j) {
                this.setItem(i, (ItemStack) null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, j);
                stack.count -= j;
            }

            this.update();
            return result;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        ItemStack stack = this.getItem(i);

        if (stack == null) {
            return null;
        } else {
            ItemStack result;

            if (stack.count <= 1) {
                this.setItem(i, (ItemStack) null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, 1);
                --stack.count;
            }

            return result;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.inventory.setItem(i, CraftItemStack.asBukkitCopy(itemstack));
    }

    public int getMaxStackSize() {
        return this.inventory.getMaxStackSize();
    }

    public void update() {}

    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    public void startOpen(EntityHuman entityhuman) {}

    public void closeContainer(EntityHuman entityhuman) {}

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    public int getProperty(int i) {
        return 0;
    }

    public void b(int i, int j) {}

    public int g() {
        return 0;
    }

    public void l() {
        this.inventory.clear();
    }

    public ItemStack[] getContents() {
        int size = this.getSize();
        ItemStack[] items = new ItemStack[size];

        for (int i = 0; i < size; ++i) {
            items[i] = this.getItem(i);
        }

        return items;
    }

    public void onOpen(CraftHumanEntity who) {
        this.viewers.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        this.viewers.remove(who);
    }

    public List getViewers() {
        return this.viewers;
    }

    public InventoryHolder getOwner() {
        return this.inventory.getHolder();
    }

    public void setMaxStackSize(int size) {
        this.inventory.setMaxStackSize(size);
    }

    public String getName() {
        return this.inventory.getName();
    }

    public boolean hasCustomName() {
        return this.getName() != null;
    }

    public IChatBaseComponent getScoreboardDisplayName() {
        return CraftChatMessage.fromString(this.getName())[0];
    }
}
