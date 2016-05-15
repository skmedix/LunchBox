package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.command.WrongUsageException;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.tileentity.*;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CraftInventory implements Inventory {

    protected final IInventory inventory;

    public CraftInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    public IInventory getInventory() {
        return this.inventory;
    }

    public int getSize() {
        return this.getInventory().getSizeInventory();
    }

    public String getName() {
        return this.getInventory().getName();
    }

    public ItemStack getItem(int index) {
        net.minecraft.item.ItemStack item = this.getInventory().getStackInSlot(index);

        return item == null ? null : CraftItemStack.asCraftMirror(item);
    }

    public ItemStack[] getContents() {
        ItemStack[] items = new ItemStack[this.getSize()];
        //LunchBox start
        net.minecraft.item.ItemStack[] mcItems = new net.minecraft.item.ItemStack[this.getSize()];

        int s = 0;
        while (s <= this.getSize()) {
            mcItems[s] = this.getInventory().getStackInSlot(s);
            s++;
        }
        //LunchBox end
        int size = Math.min(items.length, mcItems.length);

        for (int i = 0; i < size; ++i) {
            items[i] = mcItems[i] == null ? null : CraftItemStack.asCraftMirror(mcItems[i]);
        }

        return items;
    }

    public void setContents(ItemStack[] items) {
        if (this.getContents().length < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + this.getContents().length + " or less");
        } else {
            int mcItems = this.getInventory().getSizeInventory();
            //todo: make sure this is correct.
            for (int i = 0; i < mcItems; ++i) {
                if (i >= items.length) {
                    //mcItems[i] = null;
                    this.getInventory().setInventorySlotContents(i, null);
                } else {
                    this.getInventory().setInventorySlotContents(i, CraftItemStack.asNMSCopy(items[i]));
                }
            }

        }
    }

    public void setItem(int index, ItemStack item) {
        this.getInventory().setInventorySlotContents(index, item != null && item.getTypeId() != 0 ? CraftItemStack.asNMSCopy(item) : null);
    }

    public boolean contains(int materialId) {
        ItemStack[] aitemstack;
        int i = (aitemstack = this.getContents()).length;

        for (int j = 0; j < i; ++j) {
            ItemStack item = aitemstack[j];

            if (item != null && item.getTypeId() == materialId) {
                return true;
            }
        }

        return false;
    }

    public boolean contains(Material material) {
        Validate.notNull(material, "Material cannot be null");
        return this.contains(material.getId());
    }

    public boolean contains(ItemStack item) {
        if (item == null) {
            return false;
        } else {
            ItemStack[] aitemstack;
            int i = (aitemstack = this.getContents()).length;

            for (int j = 0; j < i; ++j) {
                ItemStack c = aitemstack[j];

                if (item.equals(c)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean contains(int materialId, int amount) {
        if (amount <= 0) {
            return true;
        } else {
            ItemStack[] aitemstack;
            int i = (aitemstack = this.getContents()).length;

            for (int j = 0; j < i; ++j) {
                ItemStack item = aitemstack[j];

                if (item != null && item.getTypeId() == materialId && (amount -= item.getAmount()) <= 0) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean contains(Material material, int amount) {
        Validate.notNull(material, "Material cannot be null");
        return this.contains(material.getId(), amount);
    }

    public boolean contains(ItemStack item, int amount) {
        if (item == null) {
            return false;
        } else if (amount <= 0) {
            return true;
        } else {
            ItemStack[] aitemstack;
            int i = (aitemstack = this.getContents()).length;

            for (int j = 0; j < i; ++j) {
                ItemStack c = aitemstack[j];

                if (item.equals(c)) {
                    --amount;
                    if (amount <= 0) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public boolean containsAtLeast(ItemStack item, int amount) {
        if (item == null) {
            return false;
        } else if (amount <= 0) {
            return true;
        } else {
            ItemStack[] aitemstack;
            int i = (aitemstack = this.getContents()).length;

            for (int j = 0; j < i; ++j) {
                ItemStack c = aitemstack[j];

                if (item.isSimilar(c) && (amount -= c.getAmount()) <= 0) {
                    return true;
                }
            }

            return false;
        }
    }

    public HashMap all(int materialId) {
        HashMap slots = new HashMap();
        ItemStack[] inventory = this.getContents();

        for (int i = 0; i < inventory.length; ++i) {
            ItemStack item = inventory[i];

            if (item != null && item.getTypeId() == materialId) {
                slots.put(Integer.valueOf(i), item);
            }
        }

        return slots;
    }

    public HashMap all(Material material) {
        Validate.notNull(material, "Material cannot be null");
        return this.all(material.getId());
    }

    public HashMap all(ItemStack item) {
        HashMap slots = new HashMap();

        if (item != null) {
            ItemStack[] inventory = this.getContents();

            for (int i = 0; i < inventory.length; ++i) {
                if (item.equals(inventory[i])) {
                    slots.put(Integer.valueOf(i), inventory[i]);
                }
            }
        }

        return slots;
    }

    public int first(int materialId) {
        ItemStack[] inventory = this.getContents();

        for (int i = 0; i < inventory.length; ++i) {
            ItemStack item = inventory[i];

            if (item != null && item.getTypeId() == materialId) {
                return i;
            }
        }

        return -1;
    }

    public int first(Material material) {
        Validate.notNull(material, "Material cannot be null");
        return this.first(material.getId());
    }

    public int first(ItemStack item) {
        return this.first(item, true);
    }

    private int first(ItemStack item, boolean withAmount) {
        if (item == null) {
            return -1;
        } else {
            ItemStack[] inventory = this.getContents();
            int i = 0;

            while (true) {
                if (i >= inventory.length) {
                    return -1;
                }

                if (inventory[i] != null) {
                    if (withAmount) {
                        if (item.equals(inventory[i])) {
                            break;
                        }
                    } else if (item.isSimilar(inventory[i])) {
                        break;
                    }
                }

                ++i;
            }

            return i;
        }
    }

    public int firstEmpty() {
        ItemStack[] inventory = this.getContents();

        for (int i = 0; i < inventory.length; ++i) {
            if (inventory[i] == null) {
                return i;
            }
        }

        return -1;
    }

    public int firstPartial(int materialId) {
        ItemStack[] inventory = this.getContents();

        for (int i = 0; i < inventory.length; ++i) {
            ItemStack item = inventory[i];

            if (item != null && item.getTypeId() == materialId && item.getAmount() < item.getMaxStackSize()) {
                return i;
            }
        }

        return -1;
    }

    public int firstPartial(Material material) {
        Validate.notNull(material, "Material cannot be null");
        return this.firstPartial(material.getId());
    }

    private int firstPartial(ItemStack item) {
        ItemStack[] inventory = this.getContents();
        CraftItemStack filteredItem = CraftItemStack.asCraftCopy(item);

        if (item == null) {
            return -1;
        } else {
            for (int i = 0; i < inventory.length; ++i) {
                ItemStack cItem = inventory[i];

                if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(filteredItem)) {
                    return i;
                }
            }

            return -1;
        }
    }

    public HashMap addItem(ItemStack... items) {
        Validate.noNullElements((Object[]) items, "Item cannot be null");
        HashMap leftover = new HashMap();
        int i = 0;

        while (i < items.length) {
            ItemStack item = items[i];

            while (true) {
                int firstPartial = this.firstPartial(item);

                if (firstPartial == -1) {
                    int partialItem = this.firstEmpty();

                    if (partialItem == -1) {
                        leftover.put(Integer.valueOf(i), item);
                    } else {
                        if (item.getAmount() > this.getMaxItemStack()) {
                            CraftItemStack amount = CraftItemStack.asCraftCopy(item);

                            amount.setAmount(this.getMaxItemStack());
                            this.setItem(partialItem, amount);
                            item.setAmount(item.getAmount() - this.getMaxItemStack());
                            continue;
                        }

                        this.setItem(partialItem, item);
                    }
                } else {
                    ItemStack itemstack = this.getItem(firstPartial);
                    int amount = item.getAmount();
                    int partialAmount = itemstack.getAmount();
                    int maxAmount = itemstack.getMaxStackSize();

                    if (amount + partialAmount > maxAmount) {
                        itemstack.setAmount(maxAmount);
                        this.setItem(firstPartial, itemstack);
                        item.setAmount(amount + partialAmount - maxAmount);
                        continue;
                    }

                    itemstack.setAmount(amount + partialAmount);
                    this.setItem(firstPartial, itemstack);
                }

                ++i;
                break;
            }
        }

        return leftover;
    }

    public HashMap removeItem(ItemStack... items) {
        Validate.notNull(items, "Items cannot be null");
        HashMap leftover = new HashMap();
        int i = 0;

        while (i < items.length) {
            ItemStack item = items[i];
            int toDelete = item.getAmount();

            while (true) {
                int first = this.first(item, false);

                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(Integer.valueOf(i), item);
                } else {
                    ItemStack itemStack = this.getItem(first);
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        this.clear(first);
                    } else {
                        itemStack.setAmount(amount - toDelete);
                        this.setItem(first, itemStack);
                        toDelete = 0;
                    }

                    if (toDelete > 0) {
                        continue;
                    }
                }

                ++i;
                break;
            }
        }

        return leftover;
    }

    private int getMaxItemStack() {
        return this.getInventory().getInventoryStackLimit();
    }

    public void remove(int materialId) {
        ItemStack[] items = this.getContents();

        for (int i = 0; i < items.length; ++i) {
            if (items[i] != null && items[i].getTypeId() == materialId) {
                this.clear(i);
            }
        }

    }

    public void remove(Material material) {
        Validate.notNull(material, "Material cannot be null");
        this.remove(material.getId());
    }

    public void remove(ItemStack item) {
        ItemStack[] items = this.getContents();

        for (int i = 0; i < items.length; ++i) {
            if (items[i] != null && items[i].equals(item)) {
                this.clear(i);
            }
        }

    }

    public void clear(int index) {
        this.setItem(index, (ItemStack) null);
    }

    public void clear() {
        for (int i = 0; i < this.getSize(); ++i) {
            this.clear(i);
        }

    }

    public ListIterator iterator() {
        return new InventoryIterator(this);
    }

    public ListIterator iterator(int index) {
        if (index < 0) {
            index += this.getSize() + 1;
        }

        return new InventoryIterator(this, index);
    }
    //todo: getViewers(), getHolder(), setMaxStackSize()
    public List getViewers() {
        return null;
        //return this.inventory.getViewers();
    }

    public String getTitle() {
        return this.inventory.getName();
    }

    public InventoryType getType() {
        return this.inventory instanceof InventoryCrafting ? (this.inventory.getSizeInventory() >= 9 ? InventoryType.WORKBENCH : InventoryType.CRAFTING) : (this.inventory instanceof PlayerInventory ? InventoryType.PLAYER : (this.inventory instanceof TileEntityDropper ? InventoryType.DROPPER : (this.inventory instanceof TileEntityDispenser ? InventoryType.DISPENSER : (this.inventory instanceof TileEntityFurnace ? InventoryType.FURNACE : (this instanceof CraftInventoryEnchanting ? InventoryType.ENCHANTING : (this.inventory instanceof TileEntityBrewingStand ? InventoryType.BREWING : (this.inventory instanceof CraftInventoryCustom.MinecraftInventory ? ((CraftInventoryCustom.MinecraftInventory) this.inventory).getType() : (this.inventory instanceof InventoryEnderChest ? InventoryType.ENDER_CHEST : (this.inventory instanceof InventoryMerchant ? InventoryType.MERCHANT : (this.inventory instanceof TileEntityBeacon ? InventoryType.BEACON : (this instanceof CraftInventoryAnvil ? InventoryType.ANVIL : (this.inventory instanceof IHopper ? InventoryType.HOPPER : InventoryType.CHEST))))))))))));
    }

    public InventoryHolder getHolder() {
        return null;
        //return this.inventory.getHolder();
    }

    public int getMaxStackSize() {
        return this.inventory.getInventoryStackLimit();
    }

    public void setMaxStackSize(int size) {
        //this.inventory.setMaxStackSize(size);
    }

    public int hashCode() {
        return this.inventory.hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof CraftInventory && ((CraftInventory) obj).inventory.equals(this.inventory);
    }
}
