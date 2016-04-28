package com.kookykraftmc.lunchbox;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.plugin.PluginLoadOrder;

import static java.util.logging.Level.INFO;

import java.util.logging.Logger;

/**
 * Created by TimeTheCat on 4/28/2016.
 */
@Mod(name = "LunchBox", modid = "lunchbox", serverSideOnly = true, version = "0.0.1")
public class LunchBox {
    public Logger logger = Logger.getLogger("lunchbox");
    private CraftServer server;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) throws Exception {
        server = new CraftServer(MinecraftServer.getServer(), MinecraftServer.getServer().getConfigurationManager());
        //will work on registering events later...
        //logger.log(INFO, "Registering events...");

        logger.log(INFO, "Loading plugins...");
        server.loadPlugins();
    }

    @Mod.EventHandler
    public void onAboutStart(FMLServerAboutToStartEvent event) throws Exception {
        logger.log(INFO, "Startup enabling plugins...");
        server.enablePlugins(PluginLoadOrder.STARTUP);
    }

    @Mod.EventHandler
    public void onStarting(FMLServerStartingEvent event) throws Exception {
        logger.log(INFO, "Postworld enabling plugins...");
        server.enablePlugins(PluginLoadOrder.POSTWORLD);
    }

    @Mod.EventHandler
    public void onStopping(FMLServerStoppingEvent event) throws Exception {
        logger.log(INFO, "Disabling plugins...");
        server.disablePlugins();
    }
}
