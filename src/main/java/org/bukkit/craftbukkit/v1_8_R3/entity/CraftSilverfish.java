package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntitySilverfish;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Silverfish;

public class CraftSilverfish extends CraftMonster implements Silverfish {

    public CraftSilverfish(CraftServer server, EntitySilverfish entity) {
        super(server, (EntityMonster) entity);
    }

    public EntitySilverfish getHandle() {
        return (EntitySilverfish) this.entity;
    }

    public String toString() {
        return "CraftSilverfish";
    }

    public EntityType getType() {
        return EntityType.SILVERFISH;
    }
}
