package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityEgg;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;

public class CraftEgg extends CraftProjectile implements Egg {

    public CraftEgg(CraftServer server, EntityEgg entity) {
        super(server, entity);
    }

    public EntityEgg getHandle() {
        return (EntityEgg) this.entity;
    }

    public String toString() {
        return "CraftEgg";
    }

    public EntityType getType() {
        return EntityType.EGG;
    }
}
