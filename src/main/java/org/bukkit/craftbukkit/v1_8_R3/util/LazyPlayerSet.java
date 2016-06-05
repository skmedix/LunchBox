package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

public class LazyPlayerSet extends LazyHashSet {

    HashSet makeReference() {
        if (this.reference != null) {
            throw new IllegalStateException("Reference already created!");
        } else {
            List players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
            HashSet reference = new HashSet(players.size());
            Iterator iterator = players.iterator();

            while (iterator.hasNext()) {
                EntityPlayer player = (EntityPlayer) iterator.next();

                reference.add(CraftEntity.getEntity((CraftServer) Bukkit.getServer(), player));
            }

            return reference;
        }
    }
}
