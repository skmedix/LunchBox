package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EnumColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;

public class CraftSheep extends CraftAnimals implements Sheep {

    public CraftSheep(CraftServer server, EntitySheep entity) {
        super(server, (EntityAnimal) entity);
    }

    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) this.getHandle().getColor().getColorIndex());
    }

    public void setColor(DyeColor color) {
        this.getHandle().setColor(EnumColor.fromColorIndex(color.getWoolData()));
    }

    public boolean isSheared() {
        return this.getHandle().isSheared();
    }

    public void setSheared(boolean flag) {
        this.getHandle().setSheared(flag);
    }

    public EntitySheep getHandle() {
        return (EntitySheep) this.entity;
    }

    public String toString() {
        return "CraftSheep";
    }

    public EntityType getType() {
        return EntityType.SHEEP;
    }
}
