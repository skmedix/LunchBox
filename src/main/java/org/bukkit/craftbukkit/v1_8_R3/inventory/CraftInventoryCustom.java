package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.apache.commons.lang.Validate;
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
            this.items[i] = itemstack;
            if (itemstack != null && this.getMaxStackSize() > 0 && itemstack.count > this.getMaxStackSize()) {
                itemstack.count = this.getMaxStackSize();
            }

        }

        public int getMaxStackSize() {
            return this.maxStack;
        }

        public void setMaxStackSize(int size) {
            this.maxStack = size;
        }

        public void update() {}

        public boolean a(EntityHuman entityhuman) {
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

        public void startOpen(EntityHuman entityHuman) {}

        public void closeContainer(EntityHuman entityHuman) {}

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

        public IChatBaseComponent getScoreboardDisplayName() {
            return new ChatComponentText(this.title);
        }
    }
}
