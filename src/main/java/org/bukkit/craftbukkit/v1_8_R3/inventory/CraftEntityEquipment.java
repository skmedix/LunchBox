package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CraftEntityEquipment implements EntityEquipment {

    private static final int WEAPON_SLOT = 0;
    private static final int HELMET_SLOT = 4;
    private static final int CHEST_SLOT = 3;
    private static final int LEG_SLOT = 2;
    private static final int BOOT_SLOT = 1;
    private static final int INVENTORY_SLOTS = 5;
    private final CraftLivingEntity entity;

    public CraftEntityEquipment(CraftLivingEntity entity) {
        this.entity = entity;
    }

    public ItemStack getItemInHand() {
        return this.getEquipment(0);
    }

    public void setItemInHand(ItemStack stack) {
        this.setEquipment(0, stack);
    }

    public ItemStack getHelmet() {
        return this.getEquipment(4);
    }

    public void setHelmet(ItemStack helmet) {
        this.setEquipment(4, helmet);
    }

    public ItemStack getChestplate() {
        return this.getEquipment(3);
    }

    public void setChestplate(ItemStack chestplate) {
        this.setEquipment(3, chestplate);
    }

    public ItemStack getLeggings() {
        return this.getEquipment(2);
    }

    public void setLeggings(ItemStack leggings) {
        this.setEquipment(2, leggings);
    }

    public ItemStack getBoots() {
        return this.getEquipment(1);
    }

    public void setBoots(ItemStack boots) {
        this.setEquipment(1, boots);
    }

    public ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[4];

        for (int slot = 1; slot < 5; ++slot) {
            armor[slot - 1] = this.getEquipment(slot);
        }

        return armor;
    }

    public void setArmorContents(ItemStack[] items) {
        for (int slot = 1; slot < 5; ++slot) {
            ItemStack equipment = items != null && slot <= items.length ? items[slot - 1] : null;

            this.setEquipment(slot, equipment);
        }

    }

    private ItemStack getEquipment(int slot) {
        return CraftItemStack.asBukkitCopy(this.entity.getHandle().getEquipment(slot));
    }

    private void setEquipment(int slot, ItemStack stack) {
        this.entity.getHandle().setEquipment(slot, CraftItemStack.asNMSCopy(stack));
    }

    public void clear() {
        for (int i = 0; i < 5; ++i) {
            this.setEquipment(i, (ItemStack) null);
        }

    }

    public Entity getHolder() {
        return this.entity;
    }

    public float getItemInHandDropChance() {
        return this.getDropChance(0);
    }

    public void setItemInHandDropChance(float chance) {
        this.setDropChance(0, chance);
    }

    public float getHelmetDropChance() {
        return this.getDropChance(4);
    }

    public void setHelmetDropChance(float chance) {
        this.setDropChance(4, chance);
    }

    public float getChestplateDropChance() {
        return this.getDropChance(3);
    }

    public void setChestplateDropChance(float chance) {
        this.setDropChance(3, chance);
    }

    public float getLeggingsDropChance() {
        return this.getDropChance(2);
    }

    public void setLeggingsDropChance(float chance) {
        this.setDropChance(2, chance);
    }

    public float getBootsDropChance() {
        return this.getDropChance(1);
    }

    public void setBootsDropChance(float chance) {
        this.setDropChance(1, chance);
    }

    private void setDropChance(int slot, float chance) {
        ((EntityInsentient) this.entity.getHandle()).dropChances[slot] = chance - 0.1F;
    }

    private float getDropChance(int slot) {
        return ((EntityInsentient) this.entity.getHandle()).dropChances[slot] + 0.1F;
    }
}
