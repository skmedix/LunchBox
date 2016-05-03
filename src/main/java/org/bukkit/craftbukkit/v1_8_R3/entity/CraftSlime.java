package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntitySlime;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;

public class CraftSlime extends CraftLivingEntity implements Slime {

    public CraftSlime(CraftServer server, EntitySlime entity) {
        super(server, (EntityLiving) entity);
    }

    public int getSize() {
        return this.getHandle().getSize();
    }

    public void setSize(int size) {
        this.getHandle().setSize(size);
    }

    public EntitySlime getHandle() {
        return (EntitySlime) this.entity;
    }

    public String toString() {
        return "CraftSlime";
    }

    public EntityType getType() {
        return EntityType.SLIME;
    }
}
