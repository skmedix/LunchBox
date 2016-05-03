package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityAgeable;
import net.minecraft.server.v1_8_R3.EntityAnimal;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Animals;

public class CraftAnimals extends CraftAgeable implements Animals {

    public CraftAnimals(CraftServer server, EntityAnimal entity) {
        super(server, (EntityAgeable) entity);
    }

    public EntityAnimal getHandle() {
        return (EntityAnimal) this.entity;
    }

    public String toString() {
        return "CraftAnimals";
    }
}
