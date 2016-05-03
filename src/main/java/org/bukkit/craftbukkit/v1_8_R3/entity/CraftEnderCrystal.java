package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;

public class CraftEnderCrystal extends CraftEntity implements EnderCrystal {

    public CraftEnderCrystal(CraftServer server, EntityEnderCrystal entity) {
        super(server, entity);
    }

    public EntityEnderCrystal getHandle() {
        return (EntityEnderCrystal) this.entity;
    }

    public String toString() {
        return "CraftEnderCrystal";
    }

    public EntityType getType() {
        return EntityType.ENDER_CRYSTAL;
    }
}
