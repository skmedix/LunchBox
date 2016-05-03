package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMinecartAbstract;
import net.minecraft.server.v1_8_R3.EntityMinecartChest;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.Inventory;

public class CraftMinecartChest extends CraftMinecart implements StorageMinecart {

    private final CraftInventory inventory;

    public CraftMinecartChest(CraftServer server, EntityMinecartChest entity) {
        super(server, (EntityMinecartAbstract) entity);
        this.inventory = new CraftInventory(entity);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public String toString() {
        return "CraftMinecartChest{inventory=" + this.inventory + '}';
    }

    public EntityType getType() {
        return EntityType.MINECART_CHEST;
    }
}
