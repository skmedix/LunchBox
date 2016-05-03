package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityWeather;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Weather;

public class CraftWeather extends CraftEntity implements Weather {

    public CraftWeather(CraftServer server, EntityWeather entity) {
        super(server, entity);
    }

    public EntityWeather getHandle() {
        return (EntityWeather) this.entity;
    }

    public String toString() {
        return "CraftWeather";
    }

    public EntityType getType() {
        return EntityType.WEATHER;
    }
}
