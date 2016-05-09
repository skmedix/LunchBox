package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class CraftItem extends CraftEntity implements Item {

    private final EntityItem item;

    public CraftItem(CraftServer server, Entity entity, EntityItem item) {
        super(server, entity);
        this.item = item;
    }

    public CraftItem(CraftServer server, EntityItem entity) {
        this(server, entity, entity);
    }

    public ItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(this.item.getEntityItem());
    }

    public void setItemStack(ItemStack stack) {
        this.item.setEntityItemStack(CraftItemStack.asNMSCopy(stack));
    }

    public int getPickupDelay() {
        return this.item.getEntityItem().animationsToGo;//todo: need to find a field/method that returns the item's pickup delay
    }

    public void setPickupDelay(int delay) {
        this.item.setPickupDelay(delay);
    }

    public String toString() {
        return "CraftItem";
    }

    public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}
