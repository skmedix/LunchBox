package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMinecartAbstract;
import net.minecraft.server.v1_8_R3.EntityMinecartHopper;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.inventory.Inventory;

final class CraftMinecartHopper extends CraftMinecart implements HopperMinecart {

    private final CraftInventory inventory;

    CraftMinecartHopper(CraftServer server, EntityMinecartHopper entity) {
        super(server, (EntityMinecartAbstract) entity);
        this.inventory = new CraftInventory(entity);
    }

    public String toString() {
        return "CraftMinecartHopper{inventory=" + this.inventory + '}';
    }

    public EntityType getType() {
        return EntityType.MINECART_HOPPER;
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
