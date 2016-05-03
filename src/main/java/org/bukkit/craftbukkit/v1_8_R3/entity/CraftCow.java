package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityCow;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;

public class CraftCow extends CraftAnimals implements Cow {

    public CraftCow(CraftServer server, EntityCow entity) {
        super(server, (EntityAnimal) entity);
    }

    public EntityCow getHandle() {
        return (EntityCow) this.entity;
    }

    public String toString() {
        return "CraftCow";
    }

    public EntityType getType() {
        return EntityType.COW;
    }
}
