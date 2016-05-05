package org.bukkit.craftbukkit.v1_8_R3;

import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.SQLitePlatform;
import com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.kookykraftmc.lunchbox.LunchBox;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
//import net.md_5.bungee.api.chat.BaseComponent; //LunchBox - remove
import net.minecraft.client.resources.Locale;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.integrated.IntegratedServerCommandManager;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.CommandAbstract;
import net.minecraft.server.v1_8_R3.CommandDispatcher;
import net.minecraft.server.v1_8_R3.CraftingManager;
import net.minecraft.server.v1_8_R3.DedicatedPlayerList;
import net.minecraft.server.v1_8_R3.DedicatedServer;
import net.minecraft.server.v1_8_R3.Enchantment;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityTracker;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.ExceptionWorldConflict;
import net.minecraft.server.v1_8_R3.ICommand;
import net.minecraft.server.v1_8_R3.ICommandListener;
import net.minecraft.server.v1_8_R3.IProgressUpdate;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.JsonListEntry;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.MobEffectList;
import net.minecraft.server.v1_8_R3.PersistentCollection;
import net.minecraft.server.v1_8_R3.PlayerList;
import net.minecraft.server.v1_8_R3.PropertyManager;
import net.minecraft.server.v1_8_R3.RecipesFurnace;
import net.minecraft.server.v1_8_R3.RegionFile;
import net.minecraft.server.v1_8_R3.RegionFileCache;
import net.minecraft.server.v1_8_R3.ServerCommand;
import net.minecraft.server.v1_8_R3.ServerNBTManager;
import net.minecraft.server.v1_8_R3.WorldData;
import net.minecraft.server.v1_8_R3.WorldLoaderServer;
import net.minecraft.server.v1_8_R3.WorldManager;
import net.minecraft.server.v1_8_R3.WorldMap;
import net.minecraft.server.v1_8_R3.WorldNBTStorage;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.WorldSettings;
import net.minecraft.server.v1_8_R3.WorldType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;
import org.bukkit.Warning;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.conversations.Conversable;
import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.v1_8_R3.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.generator.CraftChunkData;
import org.bukkit.craftbukkit.v1_8_R3.help.SimpleHelpMap;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftFurnaceRecipe;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftShapelessRecipe;
import org.bukkit.craftbukkit.v1_8_R3.inventory.RecipeIterator;
import org.bukkit.craftbukkit.v1_8_R3.map.CraftMapView;
import org.bukkit.craftbukkit.v1_8_R3.metadata.EntityMetadataStore;
import org.bukkit.craftbukkit.v1_8_R3.metadata.PlayerMetadataStore;
import org.bukkit.craftbukkit.v1_8_R3.metadata.WorldMetadataStore;
import org.bukkit.craftbukkit.v1_8_R3.potion.CraftPotionBrewer;
import org.bukkit.craftbukkit.v1_8_R3.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftIconCache;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_8_R3.util.DatFileFilter;
import org.bukkit.craftbukkit.v1_8_R3.util.Versioning;
import org.bukkit.craftbukkit.v1_8_R3.util.permissions.CraftDefaultPermissions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitWorker;
import org.bukkit.util.StringUtil;
import org.bukkit.util.permissions.DefaultPermissions;
import org.spigotmc.SpigotConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.MarkedYAMLException;

public final class CraftServer implements Server {

    private static final Player[] EMPTY_PLAYER_ARRAY = new Player[0];
    private final String serverName = "CraftBukkit";
    private final String serverVersion;
    private final String bukkitVersion = Versioning.getBukkitVersion();
    private final Logger logger = Logger.getLogger("Minecraft");
    private final ServicesManager servicesManager = new SimpleServicesManager();
    private final CraftScheduler scheduler = new CraftScheduler();
    private final SimpleCommandMap commandMap = new SimpleCommandMap(this);
    private final SimpleHelpMap helpMap = new SimpleHelpMap(this);
    private final StandardMessenger messenger = new StandardMessenger();
    private final PluginManager pluginManager;
    protected final MinecraftServer console;
    protected final DedicatedPlayerList playerList;
    private final Map worlds;
    private YamlConfiguration configuration;
    private YamlConfiguration commandsConfiguration;
    private final Yaml yaml;
    private final Map offlinePlayers;
    private final EntityMetadataStore entityMetadata;
    private final PlayerMetadataStore playerMetadata;
    private final WorldMetadataStore worldMetadata;
    private int monsterSpawn;
    private int animalSpawn;
    private int waterAnimalSpawn;
    private int ambientSpawn;
    public int chunkGCPeriod;
    public int chunkGCLoadThresh;
    private File container;
    private Warning.WarningState warningState;
    private final CraftServer.BooleanWrapper online;
    public CraftScoreboardManager scoreboardManager;
    public boolean playerCommandState;
    private boolean printSaveWarning;
    private CraftIconCache icon;
    private boolean overrideAllCommandBlockCommands;
    private final Pattern validUserPattern;
    private final UUID invalidUserUUID;
    private final List playerView;
    public int reloadCount;
    //private final Server.Spigot spigot;
    private static int[] $SWITCH_TABLE$org$bukkit$BanList$Type;
    private ICommandManager cmdManager;

    static {
        ConfigurationSerialization.registerClass(CraftOfflinePlayer.class);
        CraftItemFactory.instance();
    }

    public CraftServer(MinecraftServer console, DedicatedPlayerList playerList) {
        this.pluginManager = new SimplePluginManager(this, this.commandMap);
        this.worlds = new LinkedHashMap();
        this.yaml = new Yaml(new SafeConstructor());
        this.offlinePlayers = (new MapMaker()).softValues().makeMap();
        this.entityMetadata = new EntityMetadataStore();
        this.playerMetadata = new PlayerMetadataStore();
        this.worldMetadata = new WorldMetadataStore();
        this.monsterSpawn = -1;
        this.animalSpawn = -1;
        this.waterAnimalSpawn = -1;
        this.ambientSpawn = -1;
        this.chunkGCPeriod = -1;
        this.chunkGCLoadThresh = 0;
        this.warningState = Warning.WarningState.DEFAULT;
        this.online = new CraftServer.BooleanWrapper((CraftServer.BooleanWrapper) null);
        this.overrideAllCommandBlockCommands = false;
        this.validUserPattern = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");
        this.invalidUserUUID = UUID.nameUUIDFromBytes("InvalidUsername".getBytes(Charsets.UTF_8));
        /*this.spigot = new Server.Spigot() {
            public YamlConfiguration getConfig() {
                return SpigotConfig.config;
            }

            public void restart() {
                RestartCommand.restart();
            }

            public void broadcast(BaseComponent component) {
                Iterator iterator = CraftServer.this.getOnlinePlayers().iterator();

                while (iterator.hasNext()) {
                    Player player = (Player) iterator.next();

                    player.spigot().sendMessage(component);
                }

            }

            public void broadcast(BaseComponent... components) {
                Iterator iterator = CraftServer.this.getOnlinePlayers().iterator();

                while (iterator.hasNext()) {
                    Player player = (Player) iterator.next();

                    player.spigot().sendMessage(components);
                }

            }
        };*/
        this.console = console;
        this.playerList = (DedicatedPlayerList) playerList;
        this.playerView = Collections.unmodifiableList(Lists.transform(playerList.getPlayerList(), new Function() {
            public CraftPlayer apply(Entity player) {
                return (CraftPlayer) CraftEntity.getEntity(LunchBox.getServer(), player);
            }
        }));
        this.serverVersion = CraftServer.class.getPackage().getImplementationVersion();
        this.online.value = console.isServerInOnlineMode();
        Bukkit.setServer(this);
        Enchantment.sharpness.getClass();
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
        Potion.setPotionBrewer(new CraftPotionBrewer());
        net.minecraft.potion.Potion.moveSlowdown.getClass();
        PotionEffectType.stopAcceptingRegistrations();
        if (!Main.useConsole) {
            this.getLogger().info("Console input is disabled due to --noconsole command argument");
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.getConfigFile());
        this.configuration.options().copyDefaults(true);
        this.configuration.setDefaults(YamlConfiguration.loadConfiguration((Reader) (new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("configurations/bukkit.yml"), Charsets.UTF_8))));
        ConfigurationSection legacyAlias = null;

        if (!this.configuration.isString("aliases")) {
            legacyAlias = this.configuration.getConfigurationSection("aliases");
            this.configuration.set("aliases", "now-in-commands.yml");
        }

        this.saveConfig();
        if (this.getCommandsConfigFile().isFile()) {
            legacyAlias = null;
        }

        this.commandsConfiguration = YamlConfiguration.loadConfiguration(this.getCommandsConfigFile());
        this.commandsConfiguration.options().copyDefaults(true);
        this.commandsConfiguration.setDefaults(YamlConfiguration.loadConfiguration((Reader) (new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("configurations/commands.yml"), Charsets.UTF_8))));
        this.saveCommandsConfig();
        if (legacyAlias != null) {
            ConfigurationSection aliases = this.commandsConfiguration.createSection("aliases");

            String key;
            ArrayList commands;

            for (Iterator iterator = legacyAlias.getKeys(false).iterator(); iterator.hasNext(); aliases.set(key, commands)) {
                key = (String) iterator.next();
                commands = new ArrayList();
                if (legacyAlias.isList(key)) {
                    Iterator iterator1 = legacyAlias.getStringList(key).iterator();

                    while (iterator1.hasNext()) {
                        String command = (String) iterator1.next();

                        commands.add(command + " $1-");
                    }
                } else {
                    commands.add(legacyAlias.getString(key) + " $1-");
                }
            }
        }

        this.saveCommandsConfig();
        this.cmdManager = MinecraftServer.getServer().getCommandManager();
        this.overrideAllCommandBlockCommands = this.commandsConfiguration.getStringList("command-block-overrides").contains("*");
        ((SimplePluginManager) this.pluginManager).useTimings(this.configuration.getBoolean("settings.plugin-profiling"));
        this.monsterSpawn = this.configuration.getInt("spawn-limits.monsters");
        this.animalSpawn = this.configuration.getInt("spawn-limits.animals");
        this.waterAnimalSpawn = this.configuration.getInt("spawn-limits.water-animals");
        this.ambientSpawn = this.configuration.getInt("spawn-limits.ambient");
        //console.autosavePeriod = this.configuration.getInt("ticks-per.autosave");//LunchBox - remove for now
        this.warningState = Warning.WarningState.value(this.configuration.getString("settings.deprecated-verbose"));
        this.chunkGCPeriod = this.configuration.getInt("chunk-gc.period-in-ticks");
        this.chunkGCLoadThresh = this.configuration.getInt("chunk-gc.load-threshold");
        this.loadIcon();
    }

    public boolean getCommandBlockOverride(String command) {
        return this.overrideAllCommandBlockCommands || this.commandsConfiguration.getStringList("command-block-overrides").contains(command);
    }

    private File getConfigFile() {
        return new File(this.console.getDataDirectory(), "bukkit.yml");
    }

    private File getCommandsConfigFile() {
        return new File(this.console.getDataDirectory(), "commands.yml");
    }

    private void saveConfig() {
        try {
            this.configuration.save(this.getConfigFile());
        } catch (IOException ioexception) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + this.getConfigFile(), ioexception);
        }

    }

    private void saveCommandsConfig() {
        try {
            this.commandsConfiguration.save(this.getCommandsConfigFile());
        } catch (IOException ioexception) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + this.getCommandsConfigFile(), ioexception);
        }

    }

    public void loadPlugins() {
        this.pluginManager.registerInterface(JavaPluginLoader.class);
        File pluginFolder = new File(this.console.getDataDirectory(), "/plugins");

        if (pluginFolder.exists()) {
            Plugin[] plugins = this.pluginManager.loadPlugins(pluginFolder);
            Plugin[] aplugin = plugins;
            int i = plugins.length;

            for (int j = 0; j < i; ++j) {
                Plugin plugin = aplugin[j];

                try {
                    String ex = String.format("Loading %s", new Object[] { plugin.getDescription().getFullName()});

                    plugin.getLogger().info(ex);
                    plugin.onLoad();
                } catch (Throwable throwable) {
                    Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, throwable.getMessage() + " initializing " + plugin.getDescription().getFullName() + " (Is it up to date?)", throwable);
                }
            }
        } else {
            pluginFolder.mkdir();
        }

    }

    public void enablePlugins(PluginLoadOrder type) {
        if (type == PluginLoadOrder.STARTUP) {
            this.helpMap.clear();
            this.helpMap.initializeGeneralTopics();
        }

        Plugin[] plugins = this.pluginManager.getPlugins();
        Plugin[] aplugin = plugins;
        int i = plugins.length;

        for (int j = 0; j < i; ++j) {
            Plugin plugin = aplugin[j];

            if (!plugin.isEnabled() && plugin.getDescription().getLoad() == type) {
                this.loadPlugin(plugin);
            }
        }

        if (type == PluginLoadOrder.POSTWORLD) {
            this.setVanillaCommands(true);
            this.commandMap.setFallbackCommands();
            this.setVanillaCommands(false);
            this.commandMap.registerServerAliases();
            this.loadCustomPermissions();
            DefaultPermissions.registerCorePermissions();
            CraftDefaultPermissions.registerCorePermissions();
            this.helpMap.initializeCommands();
        }

    }

    public void disablePlugins() {
        this.pluginManager.disablePlugins();
    }

    private void setVanillaCommands(boolean first) {
        Map commands = cmdManager.getCommands();
        Iterator iterator = commands.values().iterator();

        while (iterator.hasNext()) {
            ICommand cmd = (ICommand) iterator.next();
            String usage = cmd.getCommandUsage(null);
            VanillaCommandWrapper wrapper = new VanillaCommandWrapper(cmd, usage);
            /* LunchBox - Remove spigot until later.
            if (SpigotConfig.replaceCommands.contains(wrapper.getName())) {
                if (first) {
                    this.commandMap.register("minecraft", wrapper);
                }
            } else if (!first) {
                this.commandMap.register("minecraft", wrapper);
            }*/
        }

    }

    private void loadPlugin(Plugin plugin) {
        try {
            this.pluginManager.enablePlugin(plugin);
            List ex = plugin.getDescription().getPermissions();
            Iterator iterator = ex.iterator();

            while (iterator.hasNext()) {
                Permission perm = (Permission) iterator.next();

                try {
                    this.pluginManager.addPermission(perm);
                } catch (IllegalArgumentException illegalargumentexception) {
                    this.getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName() + " tried to register permission \'" + perm.getName() + "\' but it\'s already registered", illegalargumentexception);
                }
            }
        } catch (Throwable throwable) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, throwable.getMessage() + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)", throwable);
        }

    }

    public String getName() {
        return "CraftBukkit";
    }

    public String getVersion() {
        return this.serverVersion + " (MC: " + this.console.getMinecraftVersion() + ")";
    }

    public String getBukkitVersion() {
        return this.bukkitVersion;
    }

    @Deprecated
    public Player[] getOnlinePlayers() {
        return (Player[]) this.getOnlinePlayers().toArray(CraftServer.EMPTY_PLAYER_ARRAY);
    }

    public List getOnlinePlayers() {
        return this.playerView;
    }

    /** @deprecated */
    @Deprecated
    public Player getPlayer(String name) {
        Validate.notNull(name, "Name cannot be null");
        Player found = this.getPlayerExact(name);

        if (found != null) {
            return found;
        } else {
            String lowerName = name.toLowerCase();
            int delta = Integer.MAX_VALUE;
            Iterator iterator = this.getOnlinePlayers().iterator();

            while (iterator.hasNext()) {
                Player player = (Player) iterator.next();

                if (player.getName().toLowerCase().startsWith(lowerName)) {
                    int curDelta = Math.abs(player.getName().length() - lowerName.length());

                    if (curDelta < delta) {
                        found = player;
                        delta = curDelta;
                    }

                    if (curDelta == 0) {
                        break;
                    }
                }
            }

            return found;
        }
    }

    /** @deprecated */
    @Deprecated
    public Player getPlayerExact(String name) {
        Validate.notNull(name, "Name cannot be null");
        EntityPlayer player = this.playerList.getPlayerByUsername(name);

        return player != null ? (Player) com.kookykraftmc.lunchbox.Player.getBukkitEntity(player) : null;
    }

    public Player getPlayer(UUID id) {
        EntityPlayer player = this.playerList.getPlayerByUUID(id);

        return player != null ? (Player) com.kookykraftmc.lunchbox.Player.getBukkitEntity(player) : null;
    }

    public int broadcastMessage(String message) {
        return this.broadcast(message, "bukkit.broadcast.user");
    }

    public Player getPlayer(EntityPlayer entity) {
        return (Player) com.kookykraftmc.lunchbox.Player.getBukkitEntity(entity);
    }

    /** @deprecated */
    @Deprecated
    public List matchPlayer(String partialName) {
        Validate.notNull(partialName, "PartialName cannot be null");
        ArrayList matchedPlayers = new ArrayList();
        Iterator iterator = this.getOnlinePlayers().iterator();

        while (iterator.hasNext()) {
            Player iterPlayer = (Player) iterator.next();
            String iterPlayerName = iterPlayer.getName();

            if (partialName.equalsIgnoreCase(iterPlayerName)) {
                matchedPlayers.clear();
                matchedPlayers.add(iterPlayer);
                break;
            }

            if (iterPlayerName.toLowerCase().contains(partialName.toLowerCase())) {
                matchedPlayers.add(iterPlayer);
            }
        }

        return matchedPlayers;
    }

    public int getMaxPlayers() {
        return this.playerList.getMaxPlayers();
    }

    public int getPort() {
        return this.getConfigInt("server-port", 25565);
    }

    public int getViewDistance() {
        return this.getConfigInt("view-distance", 10);
    }

    public String getIp() {
        return this.getConfigString("server-ip", "");
    }

    public String getServerName() {
        return this.getConfigString("server-name", "Unknown Server");
    }

    public String getServerId() {
        return this.getConfigString("server-id", "unnamed");
    }

    public String getWorldType() {
        return this.getConfigString("level-type", "DEFAULT");
    }

    public boolean getGenerateStructures() {
        return this.getConfigBoolean("generate-structures", true);
    }

    public boolean getAllowEnd() {
        return this.configuration.getBoolean("settings.allow-end");
    }

    public boolean getAllowNether() {
        return this.getConfigBoolean("allow-nether", true);
    }

    public boolean getWarnOnOverload() {
        return this.configuration.getBoolean("settings.warn-on-overload");
    }

    public boolean getQueryPlugins() {
        return this.configuration.getBoolean("settings.query-plugins");
    }

    public boolean hasWhitelist() {
        return this.getConfigBoolean("white-list", false);
    }

    private String getConfigString(String s, String defaultValue) {
        return this.console.getPropertyManager().getString(s, defaultValue);
    }

    private int getConfigInt(String s, int defaultValue) {
        return this.console.getPropertyManager().getInt(s, defaultValue);
    }

    private boolean getConfigBoolean(String s, boolean defaultValue) {
        return this.console.getPropertyManager().getBoolean(s, defaultValue);
    }

    public String getUpdateFolder() {
        return this.configuration.getString("settings.update-folder", "update");
    }

    public File getUpdateFolderFile() {
        return new File((File) this.console.options.valueOf("plugins"), this.configuration.getString("settings.update-folder", "update"));
    }

    public long getConnectionThrottle() {
        return SpigotConfig.bungee ? -1L : (long) this.configuration.getInt("settings.connection-throttle");
    }

    public int getTicksPerAnimalSpawns() {
        return this.configuration.getInt("ticks-per.animal-spawns");
    }

    public int getTicksPerMonsterSpawns() {
        return this.configuration.getInt("ticks-per.monster-spawns");
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public CraftScheduler getScheduler() {
        return this.scheduler;
    }

    public ServicesManager getServicesManager() {
        return this.servicesManager;
    }

    public List getWorlds() {
        return new ArrayList(this.worlds.values());
    }

    public DedicatedPlayerList getHandle() {
        return this.playerList;
    }

    public boolean dispatchServerCommand(CommandSender sender, ServerCommand serverCommand) {
        if (sender instanceof Conversable) {
            Conversable ex = (Conversable) sender;

            if (ex.isConversing()) {
                ex.acceptConversationInput(serverCommand.command);
                return true;
            }
        }

        try {
            this.playerCommandState = true;
            boolean flag = this.dispatchCommand(sender, serverCommand.command);

            return flag;
        } catch (Exception exception) {
            this.getLogger().log(Level.WARNING, "Unexpected exception while parsing console command \"" + serverCommand.command + '\"', exception);
        } finally {
            this.playerCommandState = false;
        }

        return false;
    }

    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(commandLine, "CommandLine cannot be null");
        if (this.commandMap.dispatch(sender, commandLine)) {
            return true;
        } else {
            sender.sendMessage(SpigotConfig.unknownCommandMessage);
            return false;
        }
    }

    public void reload() {
        ++this.reloadCount;
        this.configuration = YamlConfiguration.loadConfiguration(this.getConfigFile());
        this.commandsConfiguration = YamlConfiguration.loadConfiguration(this.getCommandsConfigFile());
        PropertyManager config = new PropertyManager(this.console.options);

        ((DedicatedServer) this.console).propertyManager = config;
        boolean animals = config.getBoolean("spawn-animals", this.console.getSpawnAnimals());
        boolean monsters = config.getBoolean("spawn-monsters", ((WorldServer) this.console.worlds.get(0)).getDifficulty() != EnumDifficulty.PEACEFUL);
        EnumDifficulty difficulty = EnumDifficulty.getById(config.getInt("difficulty", ((WorldServer) this.console.worlds.get(0)).getDifficulty().ordinal()));

        this.online.value = config.getBoolean("online-mode", this.console.getOnlineMode());
        this.console.setSpawnAnimals(config.getBoolean("spawn-animals", this.console.getSpawnAnimals()));
        this.console.setPVP(config.getBoolean("pvp", this.console.getPVP()));
        this.console.setAllowFlight(config.getBoolean("allow-flight", this.console.getAllowFlight()));
        this.console.setMotd(config.getString("motd", this.console.getMotd()));
        this.monsterSpawn = this.configuration.getInt("spawn-limits.monsters");
        this.animalSpawn = this.configuration.getInt("spawn-limits.animals");
        this.waterAnimalSpawn = this.configuration.getInt("spawn-limits.water-animals");
        this.ambientSpawn = this.configuration.getInt("spawn-limits.ambient");
        this.warningState = Warning.WarningState.value(this.configuration.getString("settings.deprecated-verbose"));
        this.printSaveWarning = false;
        this.console.autosavePeriod = this.configuration.getInt("ticks-per.autosave");
        this.chunkGCPeriod = this.configuration.getInt("chunk-gc.period-in-ticks");
        this.chunkGCLoadThresh = this.configuration.getInt("chunk-gc.load-threshold");
        this.loadIcon();

        try {
            this.playerList.getIPBans().load();
        } catch (IOException ioexception) {
            this.logger.log(Level.WARNING, "Failed to load banned-ips.json, " + ioexception.getMessage());
        }

        try {
            this.playerList.getProfileBans().load();
        } catch (IOException ioexception1) {
            this.logger.log(Level.WARNING, "Failed to load banned-players.json, " + ioexception1.getMessage());
        }

        SpigotConfig.init((File) this.console.options.valueOf("spigot-settings"));

        WorldServer pollCount;

        for (Iterator overdueWorkers = this.console.worlds.iterator(); overdueWorkers.hasNext(); pollCount.spigotConfig.init()) {
            pollCount = (WorldServer) overdueWorkers.next();
            pollCount.worldData.setDifficulty(difficulty);
            pollCount.setSpawnFlags(monsters, animals);
            if (this.getTicksPerAnimalSpawns() < 0) {
                pollCount.ticksPerAnimalSpawns = 400L;
            } else {
                pollCount.ticksPerAnimalSpawns = (long) this.getTicksPerAnimalSpawns();
            }

            if (this.getTicksPerMonsterSpawns() < 0) {
                pollCount.ticksPerMonsterSpawns = 1L;
            } else {
                pollCount.ticksPerMonsterSpawns = (long) this.getTicksPerMonsterSpawns();
            }
        }

        this.pluginManager.clearPlugins();
        this.commandMap.clearCommands();
        this.resetRecipes();
        SpigotConfig.registerCommands();
        this.overrideAllCommandBlockCommands = this.commandsConfiguration.getStringList("command-block-overrides").contains("*");

        for (int i = 0; i < 50 && this.getScheduler().getActiveWorkers().size() > 0; ++i) {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException interruptedexception) {
                ;
            }
        }

        List list = this.getScheduler().getActiveWorkers();

        Plugin plugin;
        String author;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); this.getLogger().log(Level.SEVERE, String.format("Nag author: \'%s\' of \'%s\' about the following: %s", new Object[] { author, plugin.getDescription().getName(), "This plugin is not properly shutting down its async tasks when it is being reloaded.  This may cause conflicts with the newly loaded version of the plugin"}))) {
            BukkitWorker worker = (BukkitWorker) iterator.next();

            plugin = worker.getOwner();
            author = "<NoAuthorGiven>";
            if (plugin.getDescription().getAuthors().size() > 0) {
                author = (String) plugin.getDescription().getAuthors().get(0);
            }
        }

        this.loadPlugins();
        this.enablePlugins(PluginLoadOrder.STARTUP);
        this.enablePlugins(PluginLoadOrder.POSTWORLD);
    }

    private void loadIcon() {
        this.icon = new CraftIconCache((String) null);

        try {
            File ex = new File(new File("."), "server-icon.png");

            if (ex.isFile()) {
                this.icon = loadServerIcon0(ex);
            }
        } catch (Exception exception) {
            this.getLogger().log(Level.WARNING, "Couldn\'t load server icon", exception);
        }

    }

    private void loadCustomPermissions() {
        File file = new File(this.configuration.getString("settings.permissions-file"));

        FileInputStream stream;

        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException filenotfoundexception) {
            try {
                file.createNewFile();
            } finally {
                ;
            }

            return;
        }

        Map perms;
        label211: {
            try {
                perms = (Map) this.yaml.load((InputStream) stream);
                break label211;
            } catch (MarkedYAMLException markedyamlexception) {
                this.getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML: " + markedyamlexception.toString());
                return;
            } catch (Throwable throwable) {
                this.getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML.", throwable);
            } finally {
                try {
                    stream.close();
                } catch (IOException ioexception) {
                    ;
                }

            }

            return;
        }

        if (perms == null) {
            this.getLogger().log(Level.INFO, "Server permissions file " + file + " is empty, ignoring it");
        } else {
            List permsList = Permission.loadPermissions(perms, "Permission node \'%s\' in " + file + " is invalid", Permission.DEFAULT_PERMISSION);
            Iterator iterator = permsList.iterator();

            while (iterator.hasNext()) {
                Permission perm = (Permission) iterator.next();

                try {
                    this.pluginManager.addPermission(perm);
                } catch (IllegalArgumentException illegalargumentexception) {
                    this.getLogger().log(Level.SEVERE, "Permission in " + file + " was already defined", illegalargumentexception);
                }
            }

        }
    }

    public String toString() {
        return "CraftServer{serverName=CraftBukkit,serverVersion=" + this.serverVersion + ",minecraftVersion=" + this.console.getVersion() + '}';
    }

    public World createWorld(String name, World.Environment environment) {
        return WorldCreator.name(name).environment(environment).createWorld();
    }

    public World createWorld(String name, World.Environment environment, long seed) {
        return WorldCreator.name(name).environment(environment).seed(seed).createWorld();
    }

    public World createWorld(String name, World.Environment environment, ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).generator(generator).createWorld();
    }

    public World createWorld(String name, World.Environment environment, long seed, ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).seed(seed).generator(generator).createWorld();
    }

    public World createWorld(WorldCreator creator) {
        Validate.notNull(creator, "Creator may not be null");
        String name = creator.name();
        ChunkGenerator generator = creator.generator();
        File folder = new File(this.getWorldContainer(), name);
        World world = this.getWorld(name);
        WorldType type = WorldType.getType(creator.type().getName());
        boolean generateStructures = creator.generateStructures();

        if (world != null) {
            return world;
        } else if (folder.exists() && !folder.isDirectory()) {
            throw new IllegalArgumentException("File exists with the name \'" + name + "\' and isn\'t a folder");
        } else {
            if (generator == null) {
                generator = this.getGenerator(name);
            }

            WorldLoaderServer converter = new WorldLoaderServer(this.getWorldContainer());

            if (converter.isConvertable(name)) {
                this.getLogger().info("Converting world \'" + name + "\'");
                converter.convert(name, new IProgressUpdate() {
                    private long b = System.currentTimeMillis();

                    public void a(String s) {}

                    public void a(int i) {
                        if (System.currentTimeMillis() - this.b >= 1000L) {
                            this.b = System.currentTimeMillis();
                            MinecraftServer.LOGGER.info("Converting... " + i + "%");
                        }

                    }

                    public void c(String s) {}
                });
            }

            int dimension = 10 + this.console.worlds.size();
            boolean used = false;

            while (true) {
                Iterator sdm = this.console.worlds.iterator();

                while (true) {
                    if (sdm.hasNext()) {
                        WorldServer hardcore = (WorldServer) sdm.next();

                        used = hardcore.dimension == dimension;
                        if (!used) {
                            continue;
                        }

                        ++dimension;
                    }

                    if (!used) {
                        boolean flag = false;
                        ServerNBTManager servernbtmanager = new ServerNBTManager(this.getWorldContainer(), name, true);
                        WorldData worlddata = servernbtmanager.getWorldData();

                        if (worlddata == null) {
                            WorldSettings internal = new WorldSettings(creator.seed(), WorldSettings.EnumGamemode.getById(this.getDefaultGameMode().getValue()), generateStructures, flag, type);

                            internal.setGeneratorSettings(creator.generatorSettings());
                            worlddata = new WorldData(internal, name);
                        }

                        worlddata.checkName(name);
                        WorldServer worldserver = (WorldServer) (new WorldServer(this.console, servernbtmanager, worlddata, dimension, this.console.methodProfiler, creator.environment(), generator)).b();

                        if (!this.worlds.containsKey(name.toLowerCase())) {
                            return null;
                        }

                        worldserver.scoreboard = this.getScoreboardManager().getMainScoreboard().getHandle();
                        worldserver.tracker = new EntityTracker(worldserver);
                        worldserver.addIWorldAccess(new WorldManager(this.console, worldserver));
                        worldserver.worldData.setDifficulty(EnumDifficulty.EASY);
                        worldserver.setSpawnFlags(true, true);
                        this.console.worlds.add(worldserver);
                        if (generator != null) {
                            worldserver.getWorld().getPopulators().addAll(generator.getDefaultPopulators(worldserver.getWorld()));
                        }

                        this.pluginManager.callEvent(new WorldInitEvent(worldserver.getWorld()));
                        System.out.print("Preparing start region for level " + (this.console.worlds.size() - 1) + " (Seed: " + worldserver.getSeed() + ")");
                        if (worldserver.getWorld().getKeepSpawnInMemory()) {
                            short short1 = 196;
                            long i = System.currentTimeMillis();

                            for (int j = -short1; j <= short1; j += 16) {
                                for (int k = -short1; k <= short1; k += 16) {
                                    long l = System.currentTimeMillis();

                                    if (l < i) {
                                        i = l;
                                    }

                                    if (l > i + 1000L) {
                                        int chunkcoordinates = (short1 * 2 + 1) * (short1 * 2 + 1);
                                        int j1 = (j + short1) * (short1 * 2 + 1) + k + 1;

                                        System.out.println("Preparing spawn area for " + name + ", " + j1 * 100 / chunkcoordinates + "%");
                                        i = l;
                                    }

                                    BlockPosition blockposition = worldserver.getSpawn();

                                    worldserver.chunkProviderServer.getChunkAt(blockposition.getX() + j >> 4, blockposition.getZ() + k >> 4);
                                }
                            }
                        }

                        this.pluginManager.callEvent(new WorldLoadEvent(worldserver.getWorld()));
                        return worldserver.getWorld();
                    }
                    break;
                }
            }
        }
    }

    public boolean unloadWorld(String name, boolean save) {
        return this.unloadWorld(this.getWorld(name), save);
    }

    public boolean unloadWorld(World world, boolean save) {
        if (world == null) {
            return false;
        } else {
            WorldServer handle = ((CraftWorld) world).getHandle();

            if (!this.console.worlds.contains(handle)) {
                return false;
            } else if (handle.dimension <= 1) {
                return false;
            } else if (handle.players.size() > 0) {
                return false;
            } else {
                WorldUnloadEvent e = new WorldUnloadEvent(handle.getWorld());

                this.pluginManager.callEvent(e);
                if (e.isCancelled()) {
                    return false;
                } else {
                    if (save) {
                        try {
                            handle.save(true, (IProgressUpdate) null);
                            handle.saveLevel();
                        } catch (ExceptionWorldConflict exceptionworldconflict) {
                            this.getLogger().log(Level.SEVERE, (String) null, exceptionworldconflict);
                        }
                    }

                    this.worlds.remove(world.getName().toLowerCase());
                    this.console.worlds.remove(this.console.worlds.indexOf(handle));
                    File parentFolder = world.getWorldFolder().getAbsoluteFile();
                    Class oclass = RegionFileCache.class;

                    synchronized (RegionFileCache.class) {
                        Iterator i = RegionFileCache.a.entrySet().iterator();

                        while (i.hasNext()) {
                            Entry entry = (Entry) i.next();

                            for (File child = ((File) entry.getKey()).getAbsoluteFile(); child != null; child = child.getParentFile()) {
                                if (child.equals(parentFolder)) {
                                    i.remove();

                                    try {
                                        ((RegionFile) entry.getValue()).c();
                                    } catch (IOException ioexception) {
                                        this.getLogger().log(Level.SEVERE, (String) null, ioexception);
                                    }
                                    break;
                                }
                            }
                        }

                        return true;
                    }
                }
            }
        }
    }

    public MinecraftServer getServer() {
        return this.console;
    }

    public World getWorld(String name) {
        Validate.notNull(name, "Name cannot be null");
        return (World) this.worlds.get(name.toLowerCase());
    }

    public World getWorld(UUID uid) {
        Iterator iterator = this.worlds.values().iterator();

        while (iterator.hasNext()) {
            World world = (World) iterator.next();

            if (world.getUID().equals(uid)) {
                return world;
            }
        }

        return null;
    }

    public void addWorld(World world) {
        if (this.getWorld(world.getUID()) != null) {
            System.out.println("World " + world.getName() + " is a duplicate of another world and has been prevented from loading. Please delete the uid.dat file from " + world.getName() + "\'s world directory if you want to be able to load the duplicate world.");
        } else {
            this.worlds.put(world.getName().toLowerCase(), world);
        }
    }

    public Logger getLogger() {
        return this.logger;
    }

    public ConsoleReader getReader() {
        return this.console.reader;
    }

    public PluginCommand getPluginCommand(String name) {
        Command command = this.commandMap.getCommand(name);

        return command instanceof PluginCommand ? (PluginCommand) command : null;
    }

    public void savePlayers() {
        this.checkSaveState();
        this.playerList.savePlayers();
    }

    public void configureDbConfig(ServerConfig config) {
        Validate.notNull(config, "Config cannot be null");
        DataSourceConfig ds = new DataSourceConfig();

        ds.setDriver(this.configuration.getString("database.driver"));
        ds.setUrl(this.configuration.getString("database.url"));
        ds.setUsername(this.configuration.getString("database.username"));
        ds.setPassword(this.configuration.getString("database.password"));
        ds.setIsolationLevel(TransactionIsolation.getLevel(this.configuration.getString("database.isolation")));
        if (ds.getDriver().contains("sqlite")) {
            config.setDatabasePlatform(new SQLitePlatform());
            config.getDatabasePlatform().getDbDdlSyntax().setIdentity("");
        }

        config.setDataSourceConfig(ds);
    }

    public boolean addRecipe(Recipe recipe) {
        Object toAdd;

        if (recipe instanceof CraftRecipe) {
            toAdd = (CraftRecipe) recipe;
        } else if (recipe instanceof ShapedRecipe) {
            toAdd = CraftShapedRecipe.fromBukkitRecipe((ShapedRecipe) recipe);
        } else if (recipe instanceof ShapelessRecipe) {
            toAdd = CraftShapelessRecipe.fromBukkitRecipe((ShapelessRecipe) recipe);
        } else {
            if (!(recipe instanceof FurnaceRecipe)) {
                return false;
            }

            toAdd = CraftFurnaceRecipe.fromBukkitRecipe((FurnaceRecipe) recipe);
        }

        ((CraftRecipe) toAdd).addToCraftingManager();
        CraftingManager.getInstance().sort();
        return true;
    }

    public List getRecipesFor(ItemStack result) {
        Validate.notNull(result, "Result cannot be null");
        ArrayList results = new ArrayList();
        Iterator iter = this.recipeIterator();

        while (iter.hasNext()) {
            Recipe recipe = (Recipe) iter.next();
            ItemStack stack = recipe.getResult();

            if (stack.getType() == result.getType() && (result.getDurability() == -1 || result.getDurability() == stack.getDurability())) {
                results.add(recipe);
            }
        }

        return results;
    }

    public Iterator recipeIterator() {
        return new RecipeIterator();
    }

    public void clearRecipes() {
        CraftingManager.getInstance().recipes.clear();
        RecipesFurnace.getInstance().recipes.clear();
        RecipesFurnace.getInstance().customRecipes.clear();
    }

    public void resetRecipes() {
        CraftingManager.getInstance().recipes = (new CraftingManager()).recipes;
        RecipesFurnace.getInstance().recipes = (new RecipesFurnace()).recipes;
        RecipesFurnace.getInstance().customRecipes.clear();
    }

    public Map getCommandAliases() {
        ConfigurationSection section = this.commandsConfiguration.getConfigurationSection("aliases");
        LinkedHashMap result = new LinkedHashMap();
        String key;
        Object commands;

        if (section != null) {
            for (Iterator iterator = section.getKeys(false).iterator(); iterator.hasNext(); result.put(key, (String[]) ((List) commands).toArray(new String[((List) commands).size()]))) {
                key = (String) iterator.next();
                if (section.isList(key)) {
                    commands = section.getStringList(key);
                } else {
                    commands = ImmutableList.of(section.getString(key));
                }
            }
        }

        return result;
    }

    public void removeBukkitSpawnRadius() {
        this.configuration.set("settings.spawn-radius", (Object) null);
        this.saveConfig();
    }

    public int getBukkitSpawnRadius() {
        return this.configuration.getInt("settings.spawn-radius", -1);
    }

    public String getShutdownMessage() {
        return this.configuration.getString("settings.shutdown-message");
    }

    public int getSpawnRadius() {
        return ((DedicatedServer) this.console).propertyManager.getInt("spawn-protection", 16);
    }

    public void setSpawnRadius(int value) {
        this.configuration.set("settings.spawn-radius", Integer.valueOf(value));
        this.saveConfig();
    }

    public boolean getOnlineMode() {
        return this.online.value;
    }

    public boolean getAllowFlight() {
        return this.console.getAllowFlight();
    }

    public boolean isHardcore() {
        return this.console.isHardcore();
    }

    public boolean useExactLoginLocation() {
        return this.configuration.getBoolean("settings.use-exact-login-location");
    }

    public ChunkGenerator getGenerator(String world) {
        ConfigurationSection section = this.configuration.getConfigurationSection("worlds");
        ChunkGenerator result = null;

        if (section != null) {
            section = section.getConfigurationSection(world);
            if (section != null) {
                String name = section.getString("generator");

                if (name != null && !name.equals("")) {
                    String[] split = name.split(":", 2);
                    String id = split.length > 1 ? split[1] : null;
                    Plugin plugin = this.pluginManager.getPlugin(split[0]);

                    if (plugin == null) {
                        this.getLogger().severe("Could not set generator for default world \'" + world + "\': Plugin \'" + split[0] + "\' does not exist");
                    } else if (!plugin.isEnabled()) {
                        this.getLogger().severe("Could not set generator for default world \'" + world + "\': Plugin \'" + plugin.getDescription().getFullName() + "\' is not enabled yet (is it load:STARTUP?)");
                    } else {
                        try {
                            result = plugin.getDefaultWorldGenerator(world, id);
                            if (result == null) {
                                this.getLogger().severe("Could not set generator for default world \'" + world + "\': Plugin \'" + plugin.getDescription().getFullName() + "\' lacks a default world generator");
                            }
                        } catch (Throwable throwable) {
                            plugin.getLogger().log(Level.SEVERE, "Could not set generator for default world \'" + world + "\': Plugin \'" + plugin.getDescription().getFullName(), throwable);
                        }
                    }
                }
            }
        }

        return result;
    }

    /** @deprecated */
    @Deprecated
    public CraftMapView getMap(short id) {
        PersistentCollection collection = ((WorldServer) this.console.worlds.get(0)).worldMaps;
        WorldMap worldmap = (WorldMap) collection.get(WorldMap.class, "map_" + id);

        return worldmap == null ? null : worldmap.mapView;
    }

    public CraftMapView createMap(World world) {
        Validate.notNull(world, "World cannot be null");
        net.minecraft.server.v1_8_R3.ItemStack stack = new net.minecraft.server.v1_8_R3.ItemStack(Items.MAP, 1, -1);
        WorldMap worldmap = Items.FILLED_MAP.getSavedMap(stack, ((CraftWorld) world).getHandle());

        return worldmap.mapView;
    }

    public void shutdown() {
        this.console.safeShutdown();
    }

    public int broadcast(String message, String permission) {
        int count = 0;
        Set permissibles = this.getPluginManager().getPermissionSubscriptions(permission);
        Iterator iterator = permissibles.iterator();

        while (iterator.hasNext()) {
            Permissible permissible = (Permissible) iterator.next();

            if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                CommandSender user = (CommandSender) permissible;

                user.sendMessage(message);
                ++count;
            }
        }

        return count;
    }

    /** @deprecated */
    @Deprecated
    public OfflinePlayer getOfflinePlayer(String name) {
        Validate.notNull(name, "Name cannot be null");
        Preconditions.checkArgument(!StringUtils.isBlank(name), "Name cannot be blank");
        Object result = this.getPlayerExact(name);

        if (result == null) {
            GameProfile profile = null;

            if (MinecraftServer.getServer().getOnlineMode() || SpigotConfig.bungee) {
                profile = MinecraftServer.getServer().getUserCache().getProfile(name);
            }

            if (profile == null) {
                result = this.getOfflinePlayer(new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)), name));
            } else {
                result = this.getOfflinePlayer(profile);
            }
        } else {
            this.offlinePlayers.remove(((OfflinePlayer) result).getUniqueId());
        }

        return (OfflinePlayer) result;
    }

    public OfflinePlayer getOfflinePlayer(UUID id) {
        Validate.notNull(id, "UUID cannot be null");
        Object result = this.getPlayer(id);

        if (result == null) {
            result = (OfflinePlayer) this.offlinePlayers.get(id);
            if (result == null) {
                result = new CraftOfflinePlayer(this, new GameProfile(id, (String) null));
                this.offlinePlayers.put(id, result);
            }
        } else {
            this.offlinePlayers.remove(id);
        }

        return (OfflinePlayer) result;
    }

    public OfflinePlayer getOfflinePlayer(GameProfile profile) {
        CraftOfflinePlayer player = new CraftOfflinePlayer(this, profile);

        this.offlinePlayers.put(profile.getId(), player);
        return player;
    }

    public Set getIPBans() {
        return new HashSet(Arrays.asList(this.playerList.getIPBans().getEntries()));
    }

    public void banIP(String address) {
        Validate.notNull(address, "Address cannot be null.");
        this.getBanList(BanList.Type.IP).addBan(address, (String) null, (Date) null, (String) null);
    }

    public void unbanIP(String address) {
        Validate.notNull(address, "Address cannot be null.");
        this.getBanList(BanList.Type.IP).pardon(address);
    }

    public Set getBannedPlayers() {
        HashSet result = new HashSet();
        Iterator iterator = this.playerList.getProfileBans().getValues().iterator();

        while (iterator.hasNext()) {
            JsonListEntry entry = (JsonListEntry) iterator.next();

            result.add(this.getOfflinePlayer((GameProfile) entry.getKey()));
        }

        return result;
    }

    public BanList getBanList(BanList.Type type) {
        Validate.notNull(type, "Type cannot be null");
        switch ($SWITCH_TABLE$org$bukkit$BanList$Type()[type.ordinal()]) {
        case 1:
        default:
            return new CraftProfileBanList(this.playerList.getProfileBans());

        case 2:
            return new CraftIpBanList(this.playerList.getIPBans());
        }
    }

    public void setWhitelist(boolean value) {
        this.playerList.setHasWhitelist(value);
        this.console.getPropertyManager().setProperty("white-list", Boolean.valueOf(value));
    }

    public Set getWhitelistedPlayers() {
        LinkedHashSet result = new LinkedHashSet();
        Iterator iterator = this.playerList.getWhitelist().getValues().iterator();

        while (iterator.hasNext()) {
            JsonListEntry entry = (JsonListEntry) iterator.next();

            result.add(this.getOfflinePlayer((GameProfile) entry.getKey()));
        }

        return result;
    }

    public Set getOperators() {
        HashSet result = new HashSet();
        Iterator iterator = this.playerList.getOPs().getValues().iterator();

        while (iterator.hasNext()) {
            JsonListEntry entry = (JsonListEntry) iterator.next();

            result.add(this.getOfflinePlayer((GameProfile) entry.getKey()));
        }

        return result;
    }

    public void reloadWhitelist() {
        this.playerList.reloadWhitelist();
    }

    public GameMode getDefaultGameMode() {
        return GameMode.getByValue(((WorldServer) this.console.worlds.get(0)).getWorldData().getGameType().getId());
    }

    public void setDefaultGameMode(GameMode mode) {
        Validate.notNull(mode, "Mode cannot be null");
        Iterator iterator = this.getWorlds().iterator();

        while (iterator.hasNext()) {
            World world = (World) iterator.next();

            ((CraftWorld) world).getHandle().worldData.setGameType(WorldSettings.EnumGamemode.getById(mode.getValue()));
        }

    }

    public ConsoleCommandSender getConsoleSender() {
        return this.console.console;
    }

    public EntityMetadataStore getEntityMetadata() {
        return this.entityMetadata;
    }

    public PlayerMetadataStore getPlayerMetadata() {
        return this.playerMetadata;
    }

    public WorldMetadataStore getWorldMetadata() {
        return this.worldMetadata;
    }

    public File getWorldContainer() {
        if (this.getServer().universe != null) {
            return this.getServer().universe;
        } else {
            if (this.container == null) {
                this.container = new File(this.configuration.getString("settings.world-container", "."));
            }

            return this.container;
        }
    }

    public OfflinePlayer[] getOfflinePlayers() {
        WorldNBTStorage storage = (WorldNBTStorage) ((WorldServer) this.console.worlds.get(0)).getDataManager();
        String[] files = storage.getPlayerDir().list(new DatFileFilter());
        HashSet players = new HashSet();
        String[] astring = files;
        int i = files.length;

        for (int j = 0; j < i; ++j) {
            String file = astring[j];

            try {
                players.add(this.getOfflinePlayer(UUID.fromString(file.substring(0, file.length() - 4))));
            } catch (IllegalArgumentException illegalargumentexception) {
                ;
            }
        }

        players.addAll(this.getOnlinePlayers());
        return (OfflinePlayer[]) players.toArray(new OfflinePlayer[players.size()]);
    }

    public Messenger getMessenger() {
        return this.messenger;
    }

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(this.getMessenger(), source, channel, message);
        Iterator iterator = this.getOnlinePlayers().iterator();

        while (iterator.hasNext()) {
            Player player = (Player) iterator.next();

            player.sendPluginMessage(source, channel, message);
        }

    }

    public Set getListeningPluginChannels() {
        HashSet result = new HashSet();
        Iterator iterator = this.getOnlinePlayers().iterator();

        while (iterator.hasNext()) {
            Player player = (Player) iterator.next();

            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return new CraftInventoryCustom(owner, type);
    }

    public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        return new CraftInventoryCustom(owner, type, title);
    }

    public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        Validate.isTrue(size % 9 == 0, "Chests must have a size that is a multiple of 9!");
        return new CraftInventoryCustom(owner, size);
    }

    public Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        Validate.isTrue(size % 9 == 0, "Chests must have a size that is a multiple of 9!");
        return new CraftInventoryCustom(owner, size, title);
    }

    public HelpMap getHelpMap() {
        return this.helpMap;
    }

    public SimpleCommandMap getCommandMap() {
        return this.commandMap;
    }

    public int getMonsterSpawnLimit() {
        return this.monsterSpawn;
    }

    public int getAnimalSpawnLimit() {
        return this.animalSpawn;
    }

    public int getWaterAnimalSpawnLimit() {
        return this.waterAnimalSpawn;
    }

    public int getAmbientSpawnLimit() {
        return this.ambientSpawn;
    }

    public boolean isPrimaryThread() {
        return Thread.currentThread().equals(this.console.primaryThread);
    }

    public String getMotd() {
        return this.console.getMotd();
    }

    public Warning.WarningState getWarningState() {
        return this.warningState;
    }

    public List tabComplete(ICommandListener sender, String message) {
        if (!(sender instanceof EntityPlayer)) {
            return ImmutableList.of();
        } else {
            CraftPlayer player = ((EntityPlayer) sender).getBukkitEntity();

            return message.startsWith("/") ? this.tabCompleteCommand(player, message) : this.tabCompleteChat(player, message);
        }
    }

    public List tabCompleteCommand(Player player, String message) {
        if ((SpigotConfig.tabComplete < 0 || message.length() <= SpigotConfig.tabComplete) && !message.contains(" ")) {
            return ImmutableList.of();
        } else {
            List completions = null;

            try {
                completions = this.getCommandMap().tabComplete(player, message.substring(1));
            } catch (CommandException commandexception) {
                player.sendMessage(ChatColor.RED + "An internal error occurred while attempting to tab-complete this command");
                this.getLogger().log(Level.SEVERE, "Exception when " + player.getName() + " attempted to tab complete " + message, commandexception);
            }

            return (List) (completions == null ? ImmutableList.of() : completions);
        }
    }

    public List tabCompleteChat(Player player, String message) {
        ArrayList completions = new ArrayList();
        PlayerChatTabCompleteEvent event = new PlayerChatTabCompleteEvent(player, message, completions);
        String token = event.getLastToken();
        Iterator current = this.getOnlinePlayers().iterator();

        while (current.hasNext()) {
            Player it = (Player) current.next();

            if (player.canSee(it) && StringUtil.startsWithIgnoreCase(it.getName(), token)) {
                completions.add(it.getName());
            }
        }

        this.pluginManager.callEvent(event);
        Iterator it1 = completions.iterator();

        while (it1.hasNext()) {
            Object current1 = it1.next();

            if (!(current1 instanceof String)) {
                it1.remove();
            }
        }

        Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
        return completions;
    }

    public CraftItemFactory getItemFactory() {
        return CraftItemFactory.instance();
    }

    public CraftScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    public void checkSaveState() {
        if (!this.playerCommandState && !this.printSaveWarning && this.console.autosavePeriod > 0) {
            this.printSaveWarning = true;
            this.getLogger().log(Level.WARNING, "A manual (plugin-induced) save has been detected while server is configured to auto-save. This may affect performance.", this.warningState == Warning.WarningState.ON ? new Throwable() : null);
        }
    }

    public CraftIconCache getServerIcon() {
        return this.icon;
    }

    public CraftIconCache loadServerIcon(File file) throws Exception {
        Validate.notNull(file, "File cannot be null");
        if (!file.isFile()) {
            throw new IllegalArgumentException(file + " is not a file");
        } else {
            return loadServerIcon0(file);
        }
    }

    static CraftIconCache loadServerIcon0(File file) throws Exception {
        return loadServerIcon0(ImageIO.read(file));
    }

    public CraftIconCache loadServerIcon(BufferedImage image) throws Exception {
        Validate.notNull(image, "Image cannot be null");
        return loadServerIcon0(image);
    }

    static CraftIconCache loadServerIcon0(BufferedImage image) throws Exception {
        ByteBuf bytebuf = Unpooled.buffer();

        Validate.isTrue(image.getWidth() == 64, "Must be 64 pixels wide");
        Validate.isTrue(image.getHeight() == 64, "Must be 64 pixels high");
        ImageIO.write(image, "PNG", new ByteBufOutputStream(bytebuf));
        ByteBuf bytebuf1 = Base64.encode(bytebuf);

        return new CraftIconCache("data:image/png;base64," + bytebuf1.toString(Charsets.UTF_8));
    }

    public void setIdleTimeout(int threshold) {
        this.console.setIdleTimeout(threshold);
    }

    public int getIdleTimeout() {
        return this.console.getIdleTimeout();
    }

    public ChunkGenerator.ChunkData createChunkData(World world) {
        return new CraftChunkData(world);
    }

    /** @deprecated */
    @Deprecated
    public UnsafeValues getUnsafe() {
        return CraftMagicNumbers.INSTANCE;
    }

    public Server.Spigot spigot() {
        return this.spigot;
    }

    static int[] $SWITCH_TABLE$org$bukkit$BanList$Type() {
        int[] aint = CraftServer.$SWITCH_TABLE$org$bukkit$BanList$Type;

        if (CraftServer.$SWITCH_TABLE$org$bukkit$BanList$Type != null) {
            return aint;
        } else {
            int[] aint1 = new int[BanList.Type.values().length];

            try {
                aint1[BanList.Type.IP.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[BanList.Type.NAME.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            CraftServer.$SWITCH_TABLE$org$bukkit$BanList$Type = aint1;
            return aint1;
        }
    }

    private final class BooleanWrapper {

        private boolean value;

        private BooleanWrapper() {
            this.value = true;
        }

        BooleanWrapper(CraftServer.BooleanWrapper craftserver_booleanwrapper) {
            this();
        }
    }
}
