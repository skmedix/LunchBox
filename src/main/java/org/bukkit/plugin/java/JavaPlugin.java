package org.bukkit.plugin.java;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.Warning;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.PluginAwareness;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;

public abstract class JavaPlugin extends PluginBase {

    private boolean isEnabled = false;
    private PluginLoader loader = null;
    private Server server = null;
    private File file = null;
    private PluginDescriptionFile description = null;
    private File dataFolder = null;
    private ClassLoader classLoader = null;
    private boolean naggable = true;
    private EbeanServer ebean = null;
    private FileConfiguration newConfig = null;
    private File configFile = null;
    private PluginLogger logger = null;

    public JavaPlugin() {
        ClassLoader classLoader = this.getClass().getClassLoader();

        if (!(classLoader instanceof PluginClassLoader)) {
            throw new IllegalStateException("JavaPlugin requires " + PluginClassLoader.class.getName());
        } else {
            ((PluginClassLoader) classLoader).initialize(this);
        }
    }

    /** @deprecated */
    @Deprecated
    protected JavaPlugin(PluginLoader loader, Server server, PluginDescriptionFile description, File dataFolder, File file) {
        ClassLoader classLoader = this.getClass().getClassLoader();

        if (classLoader instanceof PluginClassLoader) {
            throw new IllegalStateException("Cannot use initialization constructor at runtime");
        } else {
            this.init(loader, server, description, dataFolder, file, classLoader);
        }
    }

    protected JavaPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        ClassLoader classLoader = this.getClass().getClassLoader();

        if (classLoader instanceof PluginClassLoader) {
            throw new IllegalStateException("Cannot use initialization constructor at runtime");
        } else {
            this.init(loader, loader.server, description, dataFolder, file, classLoader);
        }
    }

    public final File getDataFolder() {
        return this.dataFolder;
    }

    public final PluginLoader getPluginLoader() {
        return this.loader;
    }

    public final Server getServer() {
        return this.server;
    }

    public final boolean isEnabled() {
        return this.isEnabled;
    }

    protected File getFile() {
        return this.file;
    }

    public final PluginDescriptionFile getDescription() {
        return this.description;
    }

    public FileConfiguration getConfig() {
        if (this.newConfig == null) {
            this.reloadConfig();
        }

        return this.newConfig;
    }

    protected final Reader getTextResource(String file) {
        InputStream in = this.getResource(file);

        return in == null ? null : new InputStreamReader(in, !this.isStrictlyUTF8() && !FileConfiguration.UTF8_OVERRIDE ? Charset.defaultCharset() : Charsets.UTF_8);
    }

    public void reloadConfig() {
        this.newConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defConfigStream = this.getResource("config.yml");

        if (defConfigStream != null) {
            YamlConfiguration defConfig;

            if (!this.isStrictlyUTF8() && !FileConfiguration.UTF8_OVERRIDE) {
                defConfig = new YamlConfiguration();

                byte[] contents;

                try {
                    contents = ByteStreams.toByteArray(defConfigStream);
                } catch (IOException ioexception) {
                    this.getLogger().log(Level.SEVERE, "Unexpected failure reading config.yml", ioexception);
                    return;
                }

                String text = new String(contents, Charset.defaultCharset());

                if (!text.equals(new String(contents, Charsets.UTF_8))) {
                    this.getLogger().warning("Default system encoding may have misread config.yml from plugin jar");
                }

                try {
                    defConfig.loadFromString(text);
                } catch (InvalidConfigurationException invalidconfigurationexception) {
                    this.getLogger().log(Level.SEVERE, "Cannot load configuration from jar", invalidconfigurationexception);
                }
            } else {
                defConfig = YamlConfiguration.loadConfiguration((Reader) (new InputStreamReader(defConfigStream, Charsets.UTF_8)));
            }

            this.newConfig.setDefaults(defConfig);
        }
    }

    private boolean isStrictlyUTF8() {
        return this.getDescription().getAwareness().contains(PluginAwareness.Flags.UTF8);
    }

    public void saveConfig() {
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException ioexception) {
            this.logger.log(Level.SEVERE, "Could not save config to " + this.configFile, ioexception);
        }

    }

    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.saveResource("config.yml", false);
        }

    }

    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = this.getResource(resourcePath);

            if (in == null) {
                throw new IllegalArgumentException("The embedded resource \'" + resourcePath + "\' cannot be found in " + this.file);
            } else {
                File outFile = new File(this.dataFolder, resourcePath);
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(this.dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

                if (!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if (outFile.exists() && !replace) {
                        this.logger.log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        FileOutputStream ex = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];

                        int len;

                        while ((len = in.read(buf)) > 0) {
                            ex.write(buf, 0, len);
                        }

                        ex.close();
                        in.close();
                    }
                } catch (IOException ioexception) {
                    this.logger.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ioexception);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

    public InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        } else {
            try {
                URL url = this.getClassLoader().getResource(filename);

                if (url == null) {
                    return null;
                } else {
                    URLConnection connection = url.openConnection();

                    connection.setUseCaches(false);
                    return connection.getInputStream();
                }
            } catch (IOException ioexception) {
                return null;
            }
        }
    }

    protected final ClassLoader getClassLoader() {
        return this.classLoader;
    }

    protected final void setEnabled(boolean enabled) {
        if (this.isEnabled != enabled) {
            this.isEnabled = enabled;
            if (this.isEnabled) {
                this.onEnable();
            } else {
                this.onDisable();
            }
        }

    }

    /** @deprecated */
    @Deprecated
    protected final void initialize(PluginLoader loader, Server server, PluginDescriptionFile description, File dataFolder, File file, ClassLoader classLoader) {
        if (server.getWarningState() != Warning.WarningState.OFF) {
            this.getLogger().log(Level.WARNING, this.getClass().getName() + " is already initialized", server.getWarningState() == Warning.WarningState.DEFAULT ? null : new AuthorNagException("Explicit initialization"));
        }
    }

    final void init(PluginLoader loader, Server server, PluginDescriptionFile description, File dataFolder, File file, ClassLoader classLoader) {
        this.loader = loader;
        this.server = server;
        this.file = file;
        this.description = description;
        this.dataFolder = dataFolder;
        this.classLoader = classLoader;
        this.configFile = new File(dataFolder, "config.yml");
        this.logger = new PluginLogger(this);
        if (description.isDatabaseEnabled()) {
            ServerConfig db = new ServerConfig();

            db.setDefaultServer(false);
            db.setRegister(false);
            db.setClasses(this.getDatabaseClasses());
            db.setName(description.getName());
            server.configureDbConfig(db);
            DataSourceConfig ds = db.getDataSourceConfig();

            ds.setUrl(this.replaceDatabaseString(ds.getUrl()));
            dataFolder.mkdirs();
            ClassLoader previous = Thread.currentThread().getContextClassLoader();

            Thread.currentThread().setContextClassLoader(classLoader);
            this.ebean = EbeanServerFactory.create(db);
            Thread.currentThread().setContextClassLoader(previous);
        }

    }

    public List getDatabaseClasses() {
        return new ArrayList();
    }

    private String replaceDatabaseString(String input) {
        input = input.replaceAll("\\{DIR\\}", this.dataFolder.getPath().replaceAll("\\\\", "/") + "/");
        input = input.replaceAll("\\{NAME\\}", this.description.getName().replaceAll("[^\\w_-]", ""));
        return input;
    }

    /** @deprecated */
    @Deprecated
    public final boolean isInitialized() {
        return true;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    public List onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public PluginCommand getCommand(String name) {
        String alias = name.toLowerCase();
        PluginCommand command = this.getServer().getPluginCommand(alias);

        if (command == null || command.getPlugin() != this) {
            command = this.getServer().getPluginCommand(this.description.getName().toLowerCase() + ":" + alias);
        }

        return command != null && command.getPlugin() == this ? command : null;
    }

    public void onLoad() {}

    public void onDisable() {}

    public void onEnable() {}

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return null;
    }

    public final boolean isNaggable() {
        return this.naggable;
    }

    public final void setNaggable(boolean canNag) {
        this.naggable = canNag;
    }

    public EbeanServer getDatabase() {
        return this.ebean;
    }

    protected void installDDL() {
        SpiEbeanServer serv = (SpiEbeanServer) this.getDatabase();
        DdlGenerator gen = serv.getDdlGenerator();

        gen.runScript(false, gen.generateCreateDdl());
    }

    protected void removeDDL() {
        SpiEbeanServer serv = (SpiEbeanServer) this.getDatabase();
        DdlGenerator gen = serv.getDdlGenerator();

        gen.runScript(true, gen.generateDropDdl());
    }

    public final Logger getLogger() {
        return this.logger;
    }

    public String toString() {
        return this.description.getFullName();
    }

    public static JavaPlugin getPlugin(Class clazz) {
        Validate.notNull(clazz, "Null class cannot have a plugin");
        if (!JavaPlugin.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz + " does not extend " + JavaPlugin.class);
        } else {
            ClassLoader cl = clazz.getClassLoader();

            if (!(cl instanceof PluginClassLoader)) {
                throw new IllegalArgumentException(clazz + " is not initialized by " + PluginClassLoader.class);
            } else {
                JavaPlugin plugin = ((PluginClassLoader) cl).plugin;

                if (plugin == null) {
                    throw new IllegalStateException("Cannot get plugin for " + clazz + " from a static initializer");
                } else {
                    return (JavaPlugin) clazz.cast(plugin);
                }
            }
        }
    }

    public static JavaPlugin getProvidingPlugin(Class clazz) {
        Validate.notNull(clazz, "Null class cannot have a plugin");
        ClassLoader cl = clazz.getClassLoader();

        if (!(cl instanceof PluginClassLoader)) {
            throw new IllegalArgumentException(clazz + " is not provided by " + PluginClassLoader.class);
        } else {
            JavaPlugin plugin = ((PluginClassLoader) cl).plugin;

            if (plugin == null) {
                throw new IllegalStateException("Cannot get plugin for " + clazz + " from a static initializer");
            } else {
                return plugin;
            }
        }
    }
}
