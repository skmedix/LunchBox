package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryWrapper implements IInventory {

    private final Inventory inventory;
    private final IInventory iInventory;
    private final List viewers = new ArrayList();

    public InventoryWrapper(Inventory inventory) {
        this.inventory = inventory;
        this.iInventory = (IInventory) inventory;
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

            if (stack.stackSize <= j) {
                this.setItem(i, (ItemStack) null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, j);
                stack.stackSize -= j;
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

            if (stack.stackSize <= 1) {
                this.setItem(i, (ItemStack) null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, 1);
                --stack.stackSize;
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

    public boolean a(EntityPlayerMP entityhuman) {
        return true;
    }

    public void startOpen(EntityPlayerMP entityhuman) {}

    public void closeContainer(EntityPlayerMP entityhuman) {}

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
        return this.iInventory.getName();
    }

    public boolean hasCustomName() {
        return this.getName() != null;
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.iInventory.getDisplayName();
    }

    public IChatComponent getScoreboardDisplayName() {
        return CraftChatMessage.fromString(this.getName())[0];
    }

    @Override
    public int getSizeInventory() {
        return this.iInventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.iInventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return this.iInventory.decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return this.iInventory.removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory.setItem(index, CraftItemStack.asBukkitCopy(stack));
    }

    @Override
    public int getInventoryStackLimit() {
        return this.iInventory.getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
        this.iInventory.markDirty();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return iInventory.isUseableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        this.iInventory.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        this.iInventory.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return this.iInventory.isItemValidForSlot(index, stack);
    }

    @Override
    public int getField(int id) {
        return this.iInventory.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        this.iInventory.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return iInventory.getFieldCount();
    }

    @Override
    public void clear() {
        this.iInventory.clear();
    }
}
