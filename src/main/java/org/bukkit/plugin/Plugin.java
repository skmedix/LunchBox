package org.bukkit.plugin;

import com.avaje.ebean.EbeanServer;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

public interface Plugin extends TabExecutor {

    File getDataFolder();

    PluginDescriptionFile getDescription();

    FileConfiguration getConfig();

    InputStream getResource(String s);

    void saveConfig();

    void saveDefaultConfig();

    void saveResource(String s, boolean flag);

    void reloadConfig();

    PluginLoader getPluginLoader();

    Server getServer();

    boolean isEnabled();

    void onDisable();

    void onLoad();

    void onEnable();

    boolean isNaggable();

    void setNaggable(boolean flag);

    EbeanServer getDatabase();

    ChunkGenerator getDefaultWorldGenerator(String s, String s1);

    Logger getLogger();

    String getName();
}
