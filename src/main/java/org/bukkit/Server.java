package org.bukkit;

import com.avaje.ebean.config.ServerConfig;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

public interface Server extends PluginMessageRecipient {

    String BROADCAST_CHANNEL_ADMINISTRATIVE = "bukkit.broadcast.admin";
    String BROADCAST_CHANNEL_USERS = "bukkit.broadcast.user";

    String getName();

    String getVersion();

    String getBukkitVersion();

    /** @deprecated */
    @Deprecated
    Player[] _getOnlinePlayers();

    Collection<? extends Player> getOnlinePlayers();

    int getMaxPlayers();

    int getPort();

    int getViewDistance();

    String getIp();

    String getServerName();

    String getServerId();

    String getWorldType();

    boolean getGenerateStructures();

    boolean getAllowEnd();

    boolean getAllowNether();

    boolean hasWhitelist();

    void setWhitelist(boolean flag);

    Set getWhitelistedPlayers();

    void reloadWhitelist();

    int broadcastMessage(String s);

    String getUpdateFolder();

    File getUpdateFolderFile();

    long getConnectionThrottle();

    int getTicksPerAnimalSpawns();

    int getTicksPerMonsterSpawns();

    Player getPlayer(String s);

    Player getPlayerExact(String s);

    List matchPlayer(String s);

    Player getPlayer(UUID uuid);

    PluginManager getPluginManager();

    BukkitScheduler getScheduler();

    ServicesManager getServicesManager();

    List getWorlds();

    World createWorld(WorldCreator worldcreator);

    boolean unloadWorld(String s, boolean flag);

    boolean unloadWorld(World world, boolean flag);

    World getWorld(String s);

    World getWorld(UUID uuid);

    /** @deprecated */
    @Deprecated
    MapView getMap(short short0);

    MapView createMap(World world);

    void reload();

    Logger getLogger();

    PluginCommand getPluginCommand(String s);

    void savePlayers();

    boolean dispatchCommand(CommandSender commandsender, String s) throws CommandException;

    void configureDbConfig(ServerConfig serverconfig);

    boolean addRecipe(Recipe recipe);

    List getRecipesFor(ItemStack itemstack);

    Iterator recipeIterator();

    void clearRecipes();

    void resetRecipes();

    Map getCommandAliases();

    int getSpawnRadius();

    void setSpawnRadius(int i);

    boolean getOnlineMode();

    boolean getAllowFlight();

    boolean isHardcore();

    /** @deprecated */
    @Deprecated
    boolean useExactLoginLocation();

    void shutdown();

    int broadcast(String s, String s1);

    /** @deprecated */
    @Deprecated
    OfflinePlayer getOfflinePlayer(String s);

    OfflinePlayer getOfflinePlayer(UUID uuid);

    Set getIPBans();

    void banIP(String s);

    void unbanIP(String s);

    Set getBannedPlayers();

    BanList getBanList(BanList.Type banlist_type);

    Set getOperators();

    GameMode getDefaultGameMode();

    void setDefaultGameMode(GameMode gamemode);

    ConsoleCommandSender getConsoleSender();

    File getWorldContainer();

    OfflinePlayer[] getOfflinePlayers();

    Messenger getMessenger();

    HelpMap getHelpMap();

    Inventory createInventory(InventoryHolder inventoryholder, InventoryType inventorytype);

    Inventory createInventory(InventoryHolder inventoryholder, InventoryType inventorytype, String s);

    Inventory createInventory(InventoryHolder inventoryholder, int i) throws IllegalArgumentException;

    Inventory createInventory(InventoryHolder inventoryholder, int i, String s) throws IllegalArgumentException;

    int getMonsterSpawnLimit();

    int getAnimalSpawnLimit();

    int getWaterAnimalSpawnLimit();

    int getAmbientSpawnLimit();

    boolean isPrimaryThread();

    String getMotd();

    String getShutdownMessage();

    Warning.WarningState getWarningState();

    ItemFactory getItemFactory();

    ScoreboardManager getScoreboardManager();

    CachedServerIcon getServerIcon();

    CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception;

    CachedServerIcon loadServerIcon(BufferedImage bufferedimage) throws IllegalArgumentException, Exception;

    void setIdleTimeout(int i);

    int getIdleTimeout();

    ChunkGenerator.ChunkData createChunkData(World world);

    /** @deprecated */
    @Deprecated
    UnsafeValues getUnsafe();
    /*
    Server.Spigot spigot();

    public static class Spigot {

        public YamlConfiguration getConfig() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void broadcast(BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void broadcast(BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void restart() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }*/
}
