package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityAgeable;
import net.minecraft.server.v1_8_R3.EntityVillager;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftVillager extends CraftAgeable implements Villager, InventoryHolder {

    public CraftVillager(CraftServer server, EntityVillager entity) {
        super(server, (EntityAgeable) entity);
    }

    public EntityVillager getHandle() {
        return (EntityVillager) this.entity;
    }

    public String toString() {
        return "CraftVillager";
    }

    public EntityType getType() {
        return EntityType.VILLAGER;
    }

    public Villager.Profession getProfession() {
        return Villager.Profession.getProfession(this.getHandle().getProfession());
    }

    public void setProfession(Villager.Profession profession) {
        Validate.notNull(profession);
        this.getHandle().setProfession(profession.getId());
    }

    public Inventory getInventory() {
        return new CraftInventory(this.getHandle().inventory);
    }
}
