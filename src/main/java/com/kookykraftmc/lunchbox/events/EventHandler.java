package com.kookykraftmc.lunchbox.events;

import com.kookykraftmc.lunchbox.LunchBox;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by TimeTheCat on 5/3/2016.
 */
public class EventHandler {
    @SubscribeEvent
    public void onLoginEvent(PlayerEvent.PlayerLoggedInEvent e) {
        Player p = (Player) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), e.player);
        SocketAddress ip = ((EntityPlayerMP) e.player).playerNetServerHandler.netManager.getRemoteAddress();
        PlayerLoginEvent event = new PlayerLoginEvent(p, null, ((InetSocketAddress) ip).getAddress(), ((java.net.InetSocketAddress) ip).getAddress());
        Bukkit.getPluginManager().callEvent(event);
    }
    @SubscribeEvent
    public void onPlayerChangeWorldEvent(PlayerEvent.PlayerChangedDimensionEvent e) {
        int f = e.fromDim;
        WorldServer w = DimensionManager.getWorld(f);
        CraftWorld from = CraftWorld.worldServerAsCBWorld(w);
        PlayerChangedWorldEvent event = new PlayerChangedWorldEvent((Player) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), e.player), from);
        Bukkit.getPluginManager().callEvent(event);
    }
    @SubscribeEvent
    public void onLogoutEvent(PlayerEvent.PlayerLoggedOutEvent e) {
        Player p = (Player) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), e.player);
        PlayerQuitEvent event = new PlayerQuitEvent(p, "LB Disconnect");
        Bukkit.getPluginManager().callEvent(event);
    }
    @SubscribeEvent
    public void onRespawnEvent(PlayerEvent.PlayerRespawnEvent e) {
        boolean isBed = false;
        Location l = null;
        Player p = (Player) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), e.player);
        l = new Location(p.getWorld(), e.player.posX, e.player.posY, e.player.posZ, e.player.cameraYaw, e.player.cameraPitch);
        if (e.player.getBedLocation() == null) isBed = true;
        PlayerRespawnEvent event = new PlayerRespawnEvent(p, l, isBed);
    }
    @SubscribeEvent
    public void onItemPickupEvent(PlayerEvent.ItemPickupEvent e) {
        Player p = (Player) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), e.player);
        Item i = (Item) e.pickedUp;
        PlayerPickupItemEvent event = new PlayerPickupItemEvent(p, i, i.getPickupDelay());
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            e.setCanceled(event.isCancelled());
        }
    }
    @SubscribeEvent
    public void onAchievementWvent(AchievementEvent e) {
        Player p = (Player) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), e.entityPlayer);
        PlayerAchievementAwardedEvent event = new PlayerAchievementAwardedEvent(p, e.achievement);
        Bukkit.getPluginManager().callEvent(event);
    }
    /* todo: for later
    @SubscribeEvent
    public void onEvent(net.minecraftforge.event.entity.player.AttackEntityEvent e) {
        Entity entity = (Entity) e.entityPlayer;
        Entity target = (Entity) e.target;

        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(entity, target, EntityDamageEvent.DamageCause.ENTITY_ATTACK, ((Entity) e.entityPlayer).getLastDamageCause().getFinalDamage());
    }
    */
}