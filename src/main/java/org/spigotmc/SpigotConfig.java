package org.spigotmc;

import com.google.common.base.Throwables;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import net.minecraft.server.v1_8_R3.AttributeRanged;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpigotConfig {

    private static File CONFIG_FILE;
    private static final String HEADER = "This is the main configuration file for Spigot.\nAs you can see, there\'s tons to configure. Some options may impact gameplay, so use\nwith caution, and make sure you know what each option does before configuring.\nFor a reference for any variable inside this file, check out the Spigot wiki at\nhttp://www.spigotmc.org/wiki/spigot-configuration/\n\nIf you need help with the configuration or have any questions related to Spigot,\njoin us at the IRC or drop by our forums and leave a post.\n\nIRC: #spigot @ irc.spi.gt ( http://www.spigotmc.org/pages/irc/ )\nForums: http://www.spigotmc.org/\n";
    public static YamlConfiguration config;
    static int version;
    static Map commands;
    private static Metrics metrics;
    public static boolean logCommands;
    public static int tabComplete;
    public static String whitelistMessage;
    public static String unknownCommandMessage;
    public static String serverFullMessage;
    public static String outdatedClientMessage = "Outdated client! Please use {0}";
    public static String outdatedServerMessage = "Outdated server! I\'m still on {0}";
    public static int timeoutTime = 60;
    public static boolean restartOnCrash = true;
    public static String restartScript = "./start.sh";
    public static String restartMessage;
    public static boolean bungee;
    public static boolean lateBind;
    public static boolean disableStatSaving;
    public static TObjectIntHashMap forcedStats = new TObjectIntHashMap();
    public static int playerSample;
    public static int playerShuffle;
    public static List spamExclusions;
    public static boolean silentCommandBlocks;
    public static boolean filterCreativeItems;
    public static Set replaceCommands;
    public static int userCacheCap;
    public static boolean saveUserCacheOnStopOnly;
    public static int intCacheLimit;
    public static double movedWronglyThreshold;
    public static double movedTooQuicklyThreshold;
    public static double maxHealth = 2048.0D;
    public static double movementSpeed = 2048.0D;
    public static double attackDamage = 2048.0D;
    public static boolean debug;

    public static void init(File configFile) {
        SpigotConfig.CONFIG_FILE = configFile;
        SpigotConfig.config = new YamlConfiguration();

        try {
            SpigotConfig.config.load(SpigotConfig.CONFIG_FILE);
        } catch (IOException ioexception) {
            ;
        } catch (InvalidConfigurationException invalidconfigurationexception) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load spigot.yml, please correct your syntax errors", invalidconfigurationexception);
            throw Throwables.propagate(invalidconfigurationexception);
        }

        SpigotConfig.config.options().header("This is the main configuration file for Spigot.\nAs you can see, there\'s tons to configure. Some options may impact gameplay, so use\nwith caution, and make sure you know what each option does before configuring.\nFor a reference for any variable inside this file, check out the Spigot wiki at\nhttp://www.spigotmc.org/wiki/spigot-configuration/\n\nIf you need help with the configuration or have any questions related to Spigot,\njoin us at the IRC or drop by our forums and leave a post.\n\nIRC: #spigot @ irc.spi.gt ( http://www.spigotmc.org/pages/irc/ )\nForums: http://www.spigotmc.org/\n");
        SpigotConfig.config.options().copyDefaults(true);
        SpigotConfig.commands = new HashMap();
        SpigotConfig.version = getInt("config-version", 8);
        set("config-version", Integer.valueOf(8));
        readConfig(SpigotConfig.class, (Object) null);
    }

    public static void registerCommands() {
        Iterator iterator = SpigotConfig.commands.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry ex = (Entry) iterator.next();

            MinecraftServer.getServer().server.getCommandMap().register((String) ex.getKey(), "Spigot", (Command) ex.getValue());
        }

        if (SpigotConfig.metrics == null) {
            try {
                SpigotConfig.metrics = new Metrics();
                SpigotConfig.metrics.start();
            } catch (IOException ioexception) {
                Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not start metrics service", ioexception);
            }
        }

    }

    static void readConfig(Class clazz, Object instance) {
        Method[] amethod;
        int i = (amethod = clazz.getDeclaredMethods()).length;

        for (int j = 0; j < i; ++j) {
            Method ex = amethod[j];

            if (Modifier.isPrivate(ex.getModifiers()) && ex.getParameterTypes().length == 0 && ex.getReturnType() == Void.TYPE) {
                try {
                    ex.setAccessible(true);
                    ex.invoke(instance, new Object[0]);
                } catch (InvocationTargetException invocationtargetexception) {
                    throw Throwables.propagate(invocationtargetexception.getCause());
                } catch (Exception exception) {
                    Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + ex, exception);
                }
            }
        }

        try {
            SpigotConfig.config.save(SpigotConfig.CONFIG_FILE);
        } catch (IOException ioexception) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + SpigotConfig.CONFIG_FILE, ioexception);
        }

    }

    private static void set(String path, Object val) {
        SpigotConfig.config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        SpigotConfig.config.addDefault(path, Boolean.valueOf(def));
        return SpigotConfig.config.getBoolean(path, SpigotConfig.config.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        SpigotConfig.config.addDefault(path, Integer.valueOf(def));
        return SpigotConfig.config.getInt(path, SpigotConfig.config.getInt(path));
    }

    private static List getList(String path, Object def) {
        SpigotConfig.config.addDefault(path, def);
        return SpigotConfig.config.getList(path, SpigotConfig.config.getList(path));
    }

    private static String getString(String path, String def) {
        SpigotConfig.config.addDefault(path, def);
        return SpigotConfig.config.getString(path, SpigotConfig.config.getString(path));
    }

    private static double getDouble(String path, double def) {
        SpigotConfig.config.addDefault(path, Double.valueOf(def));
        return SpigotConfig.config.getDouble(path, SpigotConfig.config.getDouble(path));
    }

    private static void logCommands() {
        SpigotConfig.logCommands = getBoolean("commands.log", true);
    }

    private static void tabComplete() {
        if (SpigotConfig.version < 6) {
            boolean oldValue = getBoolean("commands.tab-complete", true);

            if (oldValue) {
                set("commands.tab-complete", Integer.valueOf(0));
            } else {
                set("commands.tab-complete", Integer.valueOf(-1));
            }
        }

        SpigotConfig.tabComplete = getInt("commands.tab-complete", 0);
    }

    private static String transform(String s) {
        return ChatColor.translateAlternateColorCodes('&', s).replaceAll("\\n", "\n");
    }

    private static void messages() {
        if (SpigotConfig.version < 8) {
            set("messages.outdated-client", SpigotConfig.outdatedClientMessage);
            set("messages.outdated-server", SpigotConfig.outdatedServerMessage);
        }

        SpigotConfig.whitelistMessage = transform(getString("messages.whitelist", "You are not whitelisted on this server!"));
        SpigotConfig.unknownCommandMessage = transform(getString("messages.unknown-command", "Unknown command. Type \"/help\" for help."));
        SpigotConfig.serverFullMessage = transform(getString("messages.server-full", "The server is full!"));
        SpigotConfig.outdatedClientMessage = transform(getString("messages.outdated-client", SpigotConfig.outdatedClientMessage));
        SpigotConfig.outdatedServerMessage = transform(getString("messages.outdated-server", SpigotConfig.outdatedServerMessage));
    }

    private static void watchdog() {
        SpigotConfig.timeoutTime = getInt("settings.timeout-time", SpigotConfig.timeoutTime);
        SpigotConfig.restartOnCrash = getBoolean("settings.restart-on-crash", SpigotConfig.restartOnCrash);
        SpigotConfig.restartScript = getString("settings.restart-script", SpigotConfig.restartScript);
        SpigotConfig.restartMessage = transform(getString("messages.restart", "Server is restarting"));
        SpigotConfig.commands.put("restart", new RestartCommand("restart"));
        WatchdogThread.doStart(SpigotConfig.timeoutTime, SpigotConfig.restartOnCrash);
    }

    private static void bungee() {
        if (SpigotConfig.version < 4) {
            set("settings.bungeecord", Boolean.valueOf(false));
            System.out.println("Oudated config, disabling BungeeCord support!");
        }

        SpigotConfig.bungee = getBoolean("settings.bungeecord", false);
    }

    private static void nettyThreads() {
        int count = getInt("settings.netty-threads", 4);

        System.setProperty("io.netty.eventLoopThreads", Integer.toString(count));
        Bukkit.getLogger().log(Level.INFO, "Using {0} threads for Netty based IO", Integer.valueOf(count));
    }

    private static void lateBind() {
        SpigotConfig.lateBind = getBoolean("settings.late-bind", false);
    }

    private static void stats() {
        SpigotConfig.disableStatSaving = getBoolean("stats.disable-saving", false);
        if (!SpigotConfig.config.contains("stats.forced-stats")) {
            SpigotConfig.config.createSection("stats.forced-stats");
        }

        ConfigurationSection section = SpigotConfig.config.getConfigurationSection("stats.forced-stats");
        Iterator iterator = section.getKeys(true).iterator();

        while (iterator.hasNext()) {
            String name = (String) iterator.next();

            if (section.isInt(name)) {
                SpigotConfig.forcedStats.put(name, section.getInt(name));
            }
        }

        if (SpigotConfig.disableStatSaving && section.getInt("achievement.openInventory", 0) < 1) {
            Bukkit.getLogger().warning("*** WARNING *** stats.disable-saving is true but stats.forced-stats.achievement.openInventory isn\'t set to 1. Disabling stat saving without forcing the achievement may cause it to get stuck on the player\'s screen.");
        }

    }

    private static void tpsCommand() {
        SpigotConfig.commands.put("tps", new TicksPerSecondCommand("tps"));
    }

    private static void playerSample() {
        SpigotConfig.playerSample = getInt("settings.sample-count", 12);
        System.out.println("Server Ping Player Sample Count: " + SpigotConfig.playerSample);
    }

    private static void playerShuffle() {
        SpigotConfig.playerShuffle = getInt("settings.player-shuffle", 0);
    }

    private static void spamExclusions() {
        SpigotConfig.spamExclusions = getList("commands.spam-exclusions", Arrays.asList(new String[] { "/skill"}));
    }

    private static void silentCommandBlocks() {
        SpigotConfig.silentCommandBlocks = getBoolean("commands.silent-commandblock-console", false);
    }

    private static void filterCreativeItems() {
        SpigotConfig.filterCreativeItems = getBoolean("settings.filter-creative-items", true);
    }

    private static void replaceCommands() {
        if (SpigotConfig.config.contains("replace-commands")) {
            set("commands.replace-commands", SpigotConfig.config.getStringList("replace-commands"));
            SpigotConfig.config.set("replace-commands", (Object) null);
        }

        SpigotConfig.replaceCommands = new HashSet(getList("commands.replace-commands", Arrays.asList(new String[] { "setblock", "summon", "testforblock", "tellraw"})));
    }

    private static void userCacheCap() {
        SpigotConfig.userCacheCap = getInt("settings.user-cache-size", 1000);
    }

    private static void saveUserCacheOnStopOnly() {
        SpigotConfig.saveUserCacheOnStopOnly = getBoolean("settings.save-user-cache-on-stop-only", false);
    }

    private static void intCacheLimit() {
        SpigotConfig.intCacheLimit = getInt("settings.int-cache-limit", 1024);
    }

    private static void movedWronglyThreshold() {
        SpigotConfig.movedWronglyThreshold = getDouble("settings.moved-wrongly-threshold", 0.0625D);
    }

    private static void movedTooQuicklyThreshold() {
        SpigotConfig.movedTooQuicklyThreshold = getDouble("settings.moved-too-quickly-threshold", 100.0D);
    }

    private static void attributeMaxes() {
        SpigotConfig.maxHealth = getDouble("settings.attribute.maxHealth.max", SpigotConfig.maxHealth);
        ((AttributeRanged) GenericAttributes.maxHealth).b = SpigotConfig.maxHealth;
        SpigotConfig.movementSpeed = getDouble("settings.attribute.movementSpeed.max", SpigotConfig.movementSpeed);
        ((AttributeRanged) GenericAttributes.MOVEMENT_SPEED).b = SpigotConfig.movementSpeed;
        SpigotConfig.attackDamage = getDouble("settings.attribute.attackDamage.max", SpigotConfig.attackDamage);
        ((AttributeRanged) GenericAttributes.ATTACK_DAMAGE).b = SpigotConfig.attackDamage;
    }

    private static void debug() {
        SpigotConfig.debug = getBoolean("settings.debug", false);
        if (SpigotConfig.debug && !LogManager.getRootLogger().isTraceEnabled()) {
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            Configuration conf = ctx.getConfiguration();

            conf.getLoggerConfig("").setLevel(org.apache.logging.log4j.Level.ALL);
            ctx.updateLoggers(conf);
        }

        if (LogManager.getRootLogger().isTraceEnabled()) {
            Bukkit.getLogger().info("Debug logging is enabled");
        } else {
            Bukkit.getLogger().info("Debug logging is disabled");
        }

    }
}
