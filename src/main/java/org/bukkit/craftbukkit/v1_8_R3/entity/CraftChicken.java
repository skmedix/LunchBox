package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityChicken;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;

public class CraftChicken extends CraftAnimals implements Chicken {

    public CraftChicken(CraftServer server, EntityChicken entity) {
        super(server, (EntityAnimal) entity);
    }

    public EntityChicken getHandle() {
        return (EntityChicken) this.entity;
    }

    public String toString() {
        return "CraftChicken";
    }

    public EntityType getType() {
        return EntityType.CHICKEN;
    }
}
