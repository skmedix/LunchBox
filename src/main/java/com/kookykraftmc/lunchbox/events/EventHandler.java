package com.kookykraftmc.lunchbox.events;

import com.kookykraftmc.lunchbox.LunchBox;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by TimeTheCat on 5/3/2016.
 */
public class EventHandler {
    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        Player p = (Player) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), e.player);
        SocketAddress ip = ((EntityPlayerMP) e.player).playerNetServerHandler.netManager.getRemoteAddress();
        PlayerLoginEvent event = new PlayerLoginEvent(p, null, ((InetSocketAddress) ip).getAddress(), ((java.net.InetSocketAddress) ip).getAddress());
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
    @SubscribeEvent
    public void onPlayerChangeWorldEvent(PlayerEvent.PlayerChangedDimensionEvent e) {
        int f = e.fromDim;
        WorldServer w = DimensionManager.getWorld(f);
        CraftWorld from = CraftWorld.worldServerAsCBWorld(w);
        PlayerChangedWorldEvent event = new PlayerChangedWorldEvent((Player) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), e.player), from);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
}
