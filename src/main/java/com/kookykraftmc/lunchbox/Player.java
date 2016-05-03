package com.kookykraftmc.lunchbox;

import net.minecraft.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

/**
 * Created by TimeTheCat on 5/2/2016.
 */
public class Player {

    public static CraftEntity getBukkitEntity(Entity entity) {
        return CraftEntity.getEntity((CraftServer) Bukkit.getServer(), entity);
    }
}
