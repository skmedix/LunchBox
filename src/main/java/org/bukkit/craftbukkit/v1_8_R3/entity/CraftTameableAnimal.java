package org.bukkit.craftbukkit.v1_8_R3.entity;

import java.util.UUID;
import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityTameableAnimal;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityTargetEvent;

public class CraftTameableAnimal extends CraftAnimals implements Tameable, Creature {

    public CraftTameableAnimal(CraftServer server, EntityTameableAnimal entity) {
        super(server, (EntityAnimal) entity);
    }

    public EntityTameableAnimal getHandle() {
        return (EntityTameableAnimal) super.getHandle();
    }

    public UUID getOwnerUUID() {
        try {
            return UUID.fromString(this.getHandle().getOwnerUUID());
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public void setOwnerUUID(UUID uuid) {
        if (uuid == null) {
            this.getHandle().setOwnerUUID("");
        } else {
            this.getHandle().setOwnerUUID(uuid.toString());
        }

    }

    public AnimalTamer getOwner() {
        if (this.getOwnerUUID() == null) {
            return null;
        } else {
            Object owner = this.getServer().getPlayer(this.getOwnerUUID());

            if (owner == null) {
                owner = this.getServer().getOfflinePlayer(this.getOwnerUUID());
            }

            return (AnimalTamer) owner;
        }
    }

    public boolean isTamed() {
        return this.getHandle().isTamed();
    }

    public void setOwner(AnimalTamer tamer) {
        if (tamer != null) {
            this.setTamed(true);
            this.getHandle().setGoalTarget((EntityLiving) null, (EntityTargetEvent.TargetReason) null, false);
            this.setOwnerUUID(tamer.getUniqueId());
        } else {
            this.setTamed(false);
            this.setOwnerUUID((UUID) null);
        }

    }

    public void setTamed(boolean tame) {
        this.getHandle().setTamed(tame);
        if (!tame) {
            this.setOwnerUUID((UUID) null);
        }

    }

    public boolean isSitting() {
        return this.getHandle().isSitting();
    }

    public void setSitting(boolean sitting) {
        this.getHandle().getGoalSit().setSitting(sitting);
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{owner=" + this.getOwner() + ",tamed=" + this.isTamed() + "}";
    }
}
