package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class CraftInventoryCustom extends CraftInventory {

    public CraftInventoryCustom(InventoryHolder owner, InventoryType type) {
        super(new CraftInventoryCustom.MinecraftInventory(owner, type));
    }

    public CraftInventoryCustom(InventoryHolder owner, InventoryType type, String title) {
        super(new CraftInventoryCustom.MinecraftInventory(owner, type, title));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size) {
        super(new CraftInventoryCustom.MinecraftInventory(owner, size));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size, String title) {
        super(new CraftInventoryCustom.MinecraftInventory(owner, size, title));
    }

    static class MinecraftInventory implements IInventory {

        private final ItemStack[] items;
        private int maxStack;
        private final List viewers;
        private final String title;
        private InventoryType type;
        private final InventoryHolder owner;

        public MinecraftInventory(InventoryHolder owner, InventoryType type) {
            this(owner, type.getDefaultSize(), type.getDefaultTitle());
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, InventoryType type, String title) {
            this(owner, type.getDefaultSize(), title);
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, int size) {
            this(owner, size, "Chest");
        }

        public MinecraftInventory(InventoryHolder owner, int size, String title) {
            this.maxStack = 64;
            Validate.notNull(title, "Title cannot be null");
            Validate.isTrue(title.length() <= 32, "Title cannot be longer than 32 characters");
            this.items = new ItemStack[size];
            this.title = title;
            this.viewers = new ArrayList();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }

        public int getSize() {
            return this.items.length;
        }

        public ItemStack getItem(int i) {
            return this.items[i];
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
            this.items[i] = itemstack;
            if (itemstack != null && this.getMaxStackSize() > 0 && itemstack.stackSize > this.getMaxStackSize()) {
                itemstack.stackSize = this.getMaxStackSize();
            }

        }

        public int getMaxStackSize() {
            return this.maxStack;
        }

        public void setMaxStackSize(int size) {
            this.maxStack = size;
        }

        public void update() {}

        public boolean a(EntityPlayer entityhuman) {
            return true;
        }

        public ItemStack[] getContents() {
            return this.items;
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

        public InventoryType getType() {
            return this.type;
        }

        public InventoryHolder getOwner() {
            return this.owner;
        }

        public boolean b(int i, ItemStack itemstack) {
            return true;
        }

        public void startOpen(EntityPlayer entityHuman) {}

        public void closeContainer(EntityPlayer entityHuman) {}

        public int getProperty(int i) {
            return 0;
        }

        public void b(int i, int i1) {}

        public int g() {
            return 0;
        }

        public void l() {}

        public String getName() {
            return this.title;
        }

        public boolean hasCustomName() {
            return this.title != null;
        }

        public IChatComponent getDisplayName() {
            return null;
        }

        public IChatComponent getScoreboardDisplayName() {
            return new ChatComponentText(this.title);
        }

        public int getSizeInventory() {
            return type.getDefaultSize();
        }

        public ItemStack getStackInSlot(int index) {
            return items[index];
        }

        public ItemStack decrStackSize(int index, int count) {
            return null;
        }

        public ItemStack removeStackFromSlot(int index) {
            return null;
        }

        public void setInventorySlotContents(int index, ItemStack stack) {
            items[index] = stack;
            if (stack != null && this.getInventoryStackLimit() > 0 && stack.stackSize > this.getInventoryStackLimit()) {
                stack.stackSize = this.getInventoryStackLimit();
            }
        }

        public int getInventoryStackLimit() {
            return maxStack;
        }

        public void markDirty() {}

        public boolean isUseableByPlayer(EntityPlayer player) {
            return true;
        }

        public void openInventory(EntityPlayer player) {}

        public void closeInventory(EntityPlayer player) {}

        public boolean isItemValidForSlot(int index, ItemStack stack) {
            return false;
        }

        public int getField(int id) {
            return 0;
        }

        public void setField(int id, int value) {}

        public int getFieldCount() {
            return 0;
        }

        public void clear() {}
    }
}
