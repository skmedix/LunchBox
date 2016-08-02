package com.kookykraftmc.lunchbox;

import com.kookykraftmc.lunchbox.impl.LBServer;
import com.kookykraftmc.lunchbox.impl.entity.LBEntity;
import net.minecraft.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

/**
 * Created by TimeTheCat on 5/2/2016.
 */
public class EntityHelper {

    public static CraftEntity getBukkitEntity(Entity entity) {
        return CraftEntity.getEntity((CraftServer) Bukkit.getServer(), entity);
    }
}
