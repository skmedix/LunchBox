package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

public class CraftExperienceOrb extends CraftEntity implements ExperienceOrb {

    public CraftExperienceOrb(CraftServer server, EntityExperienceOrb entity) {
        super(server, entity);
    }

    public int getExperience() {
        return this.getHandle().value;
    }

    public void setExperience(int value) {
        this.getHandle().value = value;
    }

    public EntityExperienceOrb getHandle() {
        return (EntityExperienceOrb) this.entity;
    }

    public String toString() {
        return "CraftExperienceOrb";
    }

    public EntityType getType() {
        return EntityType.EXPERIENCE_ORB;
    }
}
