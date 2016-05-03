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
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
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
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.spigotmc.CustomTimingsHandler;

public final class Bukkit {

    private static Server server;

    public static Server getServer() {
        return Bukkit.server;
    }

    public static void setServer(Server server) {
        if (Bukkit.server != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Server");
        } else {
            Bukkit.server = server;
            server.getLogger().info("This server is running " + getName() + " version " + getVersion() + " (Implementing API version " + getBukkitVersion() + ")");
        }
    }

    public static String getName() {
        return Bukkit.server.getName();
    }

    public static String getVersion() {
        return Bukkit.server.getVersion();
    }

    public static String getBukkitVersion() {
        return Bukkit.server.getBukkitVersion();
    }

    /** @deprecated */
    @Deprecated
    public static Player[] getOnlinePlayers() {
        return Bukkit.server.getOnlinePlayers();
    }

    public static Collection getOnlinePlayers() {
        return Bukkit.server.getOnlinePlayers();
    }

    public static int getMaxPlayers() {
        return Bukkit.server.getMaxPlayers();
    }

    public static int getPort() {
        return Bukkit.server.getPort();
    }

    public static int getViewDistance() {
        return Bukkit.server.getViewDistance();
    }

    public static String getIp() {
        return Bukkit.server.getIp();
    }

    public static String getServerName() {
        return Bukkit.server.getServerName();
    }

    public static String getServerId() {
        return Bukkit.server.getServerId();
    }

    public static String getWorldType() {
        return Bukkit.server.getWorldType();
    }

    public static boolean getGenerateStructures() {
        return Bukkit.server.getGenerateStructures();
    }

    public static boolean getAllowEnd() {
        return Bukkit.server.getAllowEnd();
    }

    public static boolean getAllowNether() {
        return Bukkit.server.getAllowNether();
    }

    public static boolean hasWhitelist() {
        return Bukkit.server.hasWhitelist();
    }

    public static void setWhitelist(boolean value) {
        Bukkit.server.setWhitelist(value);
    }

    public static Set getWhitelistedPlayers() {
        return Bukkit.server.getWhitelistedPlayers();
    }

    public static void reloadWhitelist() {
        Bukkit.server.reloadWhitelist();
    }

    public static int broadcastMessage(String message) {
        return Bukkit.server.broadcastMessage(message);
    }

    public static String getUpdateFolder() {
        return Bukkit.server.getUpdateFolder();
    }

    public static File getUpdateFolderFile() {
        return Bukkit.server.getUpdateFolderFile();
    }

    public static long getConnectionThrottle() {
        return Bukkit.server.getConnectionThrottle();
    }

    public static int getTicksPerAnimalSpawns() {
        return Bukkit.server.getTicksPerAnimalSpawns();
    }

    public static int getTicksPerMonsterSpawns() {
        return Bukkit.server.getTicksPerMonsterSpawns();
    }

    public static Player getPlayer(String name) {
        return Bukkit.server.getPlayer(name);
    }

    public static Player getPlayerExact(String name) {
        return Bukkit.server.getPlayerExact(name);
    }

    public static List matchPlayer(String name) {
        return Bukkit.server.matchPlayer(name);
    }

    public static Player getPlayer(UUID id) {
        return Bukkit.server.getPlayer(id);
    }

    public static PluginManager getPluginManager() {
        return Bukkit.server.getPluginManager();
    }

    public static BukkitScheduler getScheduler() {
        return Bukkit.server.getScheduler();
    }

    public static ServicesManager getServicesManager() {
        return Bukkit.server.getServicesManager();
    }

    public static List getWorlds() {
        return Bukkit.server.getWorlds();
    }

    public static World createWorld(WorldCreator creator) {
        return Bukkit.server.createWorld(creator);
    }

    public static boolean unloadWorld(String name, boolean save) {
        return Bukkit.server.unloadWorld(name, save);
    }

    public static boolean unloadWorld(World world, boolean save) {
        return Bukkit.server.unloadWorld(world, save);
    }

    public static World getWorld(String name) {
        return Bukkit.server.getWorld(name);
    }

    public static World getWorld(UUID uid) {
        return Bukkit.server.getWorld(uid);
    }

    /** @deprecated */
    @Deprecated
    public static MapView getMap(short id) {
        return Bukkit.server.getMap(id);
    }

    public static MapView createMap(World world) {
        return Bukkit.server.createMap(world);
    }

    public static void reload() {
        Bukkit.server.reload();
        CustomTimingsHandler.reload();
    }

    public static Logger getLogger() {
        return Bukkit.server.getLogger();
    }

    public static PluginCommand getPluginCommand(String name) {
        return Bukkit.server.getPluginCommand(name);
    }

    public static void savePlayers() {
        Bukkit.server.savePlayers();
    }

    public static boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException {
        return Bukkit.server.dispatchCommand(sender, commandLine);
    }

    public static void configureDbConfig(ServerConfig config) {
        Bukkit.server.configureDbConfig(config);
    }

    public static boolean addRecipe(Recipe recipe) {
        return Bukkit.server.addRecipe(recipe);
    }

    public static List getRecipesFor(ItemStack result) {
        return Bukkit.server.getRecipesFor(result);
    }

    public static Iterator recipeIterator() {
        return Bukkit.server.recipeIterator();
    }

    public static void clearRecipes() {
        Bukkit.server.clearRecipes();
    }

    public static void resetRecipes() {
        Bukkit.server.resetRecipes();
    }

    public static Map getCommandAliases() {
        return Bukkit.server.getCommandAliases();
    }

    public static int getSpawnRadius() {
        return Bukkit.server.getSpawnRadius();
    }

    public static void setSpawnRadius(int value) {
        Bukkit.server.setSpawnRadius(value);
    }

    public static boolean getOnlineMode() {
        return Bukkit.server.getOnlineMode();
    }

    public static boolean getAllowFlight() {
        return Bukkit.server.getAllowFlight();
    }

    public static boolean isHardcore() {
        return Bukkit.server.isHardcore();
    }

    /** @deprecated */
    @Deprecated
    public static boolean useExactLoginLocation() {
        return Bukkit.server.useExactLoginLocation();
    }

    public static void shutdown() {
        Bukkit.server.shutdown();
    }

    public static int broadcast(String message, String permission) {
        return Bukkit.server.broadcast(message, permission);
    }

    /** @deprecated */
    @Deprecated
    public static OfflinePlayer getOfflinePlayer(String name) {
        return Bukkit.server.getOfflinePlayer(name);
    }

    public static OfflinePlayer getOfflinePlayer(UUID id) {
        return Bukkit.server.getOfflinePlayer(id);
    }

    public static Set getIPBans() {
        return Bukkit.server.getIPBans();
    }

    public static void banIP(String address) {
        Bukkit.server.banIP(address);
    }

    public static void unbanIP(String address) {
        Bukkit.server.unbanIP(address);
    }

    public static Set getBannedPlayers() {
        return Bukkit.server.getBannedPlayers();
    }

    public static BanList getBanList(BanList.Type type) {
        return Bukkit.server.getBanList(type);
    }

    public static Set getOperators() {
        return Bukkit.server.getOperators();
    }

    public static GameMode getDefaultGameMode() {
        return Bukkit.server.getDefaultGameMode();
    }

    public static void setDefaultGameMode(GameMode mode) {
        Bukkit.server.setDefaultGameMode(mode);
    }

    public static ConsoleCommandSender getConsoleSender() {
        return Bukkit.server.getConsoleSender();
    }

    public static File getWorldContainer() {
        return Bukkit.server.getWorldContainer();
    }

    public static OfflinePlayer[] getOfflinePlayers() {
        return Bukkit.server.getOfflinePlayers();
    }

    public static Messenger getMessenger() {
        return Bukkit.server.getMessenger();
    }

    public static HelpMap getHelpMap() {
        return Bukkit.server.getHelpMap();
    }

    public static Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return Bukkit.server.createInventory(owner, type);
    }

    public static Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        return Bukkit.server.createInventory(owner, type, title);
    }

    public static Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        return Bukkit.server.createInventory(owner, size);
    }

    public static Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        return Bukkit.server.createInventory(owner, size, title);
    }

    public static int getMonsterSpawnLimit() {
        return Bukkit.server.getMonsterSpawnLimit();
    }

    public static int getAnimalSpawnLimit() {
        return Bukkit.server.getAnimalSpawnLimit();
    }

    public static int getWaterAnimalSpawnLimit() {
        return Bukkit.server.getWaterAnimalSpawnLimit();
    }

    public static int getAmbientSpawnLimit() {
        return Bukkit.server.getAmbientSpawnLimit();
    }

    public static boolean isPrimaryThread() {
        return Bukkit.server.isPrimaryThread();
    }

    public static String getMotd() {
        return Bukkit.server.getMotd();
    }

    public static String getShutdownMessage() {
        return Bukkit.server.getShutdownMessage();
    }

    public static Warning.WarningState getWarningState() {
        return Bukkit.server.getWarningState();
    }

    public static ItemFactory getItemFactory() {
        return Bukkit.server.getItemFactory();
    }

    public static ScoreboardManager getScoreboardManager() {
        return Bukkit.server.getScoreboardManager();
    }

    public static CachedServerIcon getServerIcon() {
        return Bukkit.server.getServerIcon();
    }

    public static CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception {
        return Bukkit.server.loadServerIcon(file);
    }

    public static CachedServerIcon loadServerIcon(BufferedImage image) throws IllegalArgumentException, Exception {
        return Bukkit.server.loadServerIcon(image);
    }

    public static void setIdleTimeout(int threshold) {
        Bukkit.server.setIdleTimeout(threshold);
    }

    public static int getIdleTimeout() {
        return Bukkit.server.getIdleTimeout();
    }

    public static ChunkGenerator.ChunkData createChunkData(World world) {
        return Bukkit.server.createChunkData(world);
    }

    /** @deprecated */
    @Deprecated
    public static UnsafeValues getUnsafe() {
        return Bukkit.server.getUnsafe();
    }

    public static Server.Spigot spigot() {
        return Bukkit.server.spigot();
    }
}
