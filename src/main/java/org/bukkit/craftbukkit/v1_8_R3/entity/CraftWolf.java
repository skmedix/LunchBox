package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityTameableAnimal;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.EnumColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf {

    public CraftWolf(CraftServer server, EntityWolf wolf) {
        super(server, (EntityTameableAnimal) wolf);
    }

    public boolean isAngry() {
        return this.getHandle().isAngry();
    }

    public void setAngry(boolean angry) {
        this.getHandle().setAngry(angry);
    }

    public EntityWolf getHandle() {
        return (EntityWolf) this.entity;
    }

    public EntityType getType() {
        return EntityType.WOLF;
    }

    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) this.getHandle().getCollarColor().getColorIndex());
    }

    public void setCollarColor(DyeColor color) {
        this.getHandle().setCollarColor(EnumColor.fromColorIndex(color.getWoolData()));
    }
}
