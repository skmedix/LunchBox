package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CraftInventoryPlayer extends CraftInventory implements PlayerInventory, EntityEquipment {

    public CraftInventoryPlayer(InventoryPlayer inventory) {
        super(inventory);
    }

    public InventoryPlayer getInventory() {
        return (InventoryPlayer) this.inventory;
    }

    public int getSize() {
        return super.getSize() - 4;
    }

    public ItemStack getItemInHand() {
        return CraftItemStack.asCraftMirror(this.getInventory().getCurrentItem());
    }

    public void setItemInHand(ItemStack stack) {
        this.setItem(this.getHeldItemSlot(), stack);
    }

    public void setItem(int index, ItemStack item) {
        super.setItem(index, item);
        if (this.getHolder() != null) {
            EntityPlayerMP player = this.getHolder().getHandle();

            if (player.playerNetServerHandler != null) {
                if (index < InventoryPlayer.getHotbarSize()) {
                    index += 36;
                } else if (index > 35) {
                    index = 8 - (index - 36);
                }

                player.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.inventoryContainer.windowId, index, CraftItemStack.asNMSCopy(item)));
            }
        }
    }

    public int getHeldItemSlot() {
        return this.getInventory().currentItem;
    }

    public void setHeldItemSlot(int slot) {
        Validate.isTrue(slot >= 0 && slot < InventoryPlayer.getHotbarSize(), "Slot is not between 0 and 8 inclusive");
        this.getInventory().currentItem = slot;
        ((CraftPlayer) this.getHolder()).getHandle().playerConnection.sendPacket(new PacketPlayOutHeldItemSlot(slot));
    }

    public ItemStack getHelmet() {
        return this.getItem(this.getSize() + 3);
    }

    public ItemStack getChestplate() {
        return this.getItem(this.getSize() + 2);
    }

    public ItemStack getLeggings() {
        return this.getItem(this.getSize() + 1);
    }

    public ItemStack getBoots() {
        return this.getItem(this.getSize() + 0);
    }

    public void setHelmet(ItemStack helmet) {
        this.setItem(this.getSize() + 3, helmet);
    }

    public void setChestplate(ItemStack chestplate) {
        this.setItem(this.getSize() + 2, chestplate);
    }

    public void setLeggings(ItemStack leggings) {
        this.setItem(this.getSize() + 1, leggings);
    }

    public void setBoots(ItemStack boots) {
        this.setItem(this.getSize() + 0, boots);
    }

    public ItemStack[] getArmorContents() {
        net.minecraft.item.ItemStack[] mcItems = this.getInventory().armorInventory;
        ItemStack[] ret = new ItemStack[mcItems.length];

        for (int i = 0; i < mcItems.length; ++i) {
            ret[i] = CraftItemStack.asCraftMirror(mcItems[i]);
        }

        return ret;
    }

    public void setArmorContents(ItemStack[] items) {
        int cnt = this.getSize();

        if (items == null) {
            items = new ItemStack[4];
        }

        ItemStack[] aitemstack = items;
        int i = items.length;

        for (int j = 0; j < i; ++j) {
            ItemStack item = aitemstack[j];

            if (item != null && item.getTypeId() != 0) {
                this.setItem(cnt++, item);
            } else {
                this.clear(cnt++);
            }
        }

    }

    public int clear(int id, int data) {
        int count = 0;
        ItemStack[] items = this.getContents();
        ItemStack[] armor = this.getArmorContents();
        int armorSlot = this.getSize();

        for (int item = 0; item < items.length; ++item) {
            ItemStack item1 = items[item];

            if (item1 != null && (id <= -1 || item1.getTypeId() == id) && (data <= -1 || item1.getData().getData() == data)) {
                count += item1.getAmount();
                this.setItem(item, (ItemStack) null);
            }
        }

        ItemStack[] aitemstack = armor;
        int i = armor.length;

        for (int j = 0; j < i; ++j) {
            ItemStack itemstack = aitemstack[j];

            if (itemstack != null && (id <= -1 || itemstack.getTypeId() == id) && (data <= -1 || itemstack.getData().getData() == data)) {
                count += itemstack.getAmount();
                this.setItem(armorSlot++, (ItemStack) null);
            }
        }

        return count;
    }

    public HumanEntity getHolder() {
        return (HumanEntity) this.inventory.getOwner();
    }

    public float getItemInHandDropChance() {
        return 1.0F;
    }

    public void setItemInHandDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    public float getHelmetDropChance() {
        return 1.0F;
    }

    public void setHelmetDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    public float getChestplateDropChance() {
        return 1.0F;
    }

    public void setChestplateDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    public float getLeggingsDropChance() {
        return 1.0F;
    }

    public void setLeggingsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    public float getBootsDropChance() {
        return 1.0F;
    }

    public void setBootsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }
}
