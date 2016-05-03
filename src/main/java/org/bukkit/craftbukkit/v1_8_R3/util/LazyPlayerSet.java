package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public class LazyPlayerSet extends LazyHashSet {

    HashSet makeReference() {
        if (this.reference != null) {
            throw new IllegalStateException("Reference already created!");
        } else {
            List players = MinecraftServer.getServer().getPlayerList().players;
            HashSet reference = new HashSet(players.size());
            Iterator iterator = players.iterator();

            while (iterator.hasNext()) {
                EntityPlayer player = (EntityPlayer) iterator.next();

                reference.add(player.getBukkitEntity());
            }

            return reference;
        }
    }
}
