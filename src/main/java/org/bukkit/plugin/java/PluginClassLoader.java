package org.bukkit.plugin.java;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;

final class PluginClassLoader extends URLClassLoader {

    private final JavaPluginLoader loader;
    private final Map classes = new ConcurrentHashMap();
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    final JavaPlugin plugin;
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;

    static {
        try {
            Method ex = ClassLoader.class.getDeclaredMethod("registerAsParallelCapable", new Class[0]);

            if (ex != null) {
                boolean oldAccessible = ex.isAccessible();

                ex.setAccessible(true);
                ex.invoke((Object) null, new Object[0]);
                ex.setAccessible(oldAccessible);
                Bukkit.getLogger().log(Level.INFO, "Set PluginClassLoader as parallel capable");
            }
        } catch (NoSuchMethodException nosuchmethodexception) {
            ;
        } catch (Exception exception) {
            Bukkit.getLogger().log(Level.WARNING, "Error setting PluginClassLoader as parallel capable", exception);
        }

    }

    PluginClassLoader(JavaPluginLoader loader, ClassLoader parent, PluginDescriptionFile description, File dataFolder, File file) throws InvalidPluginException, MalformedURLException {
        super(new URL[] { file.toURI().toURL()}, parent);
        Validate.notNull(loader, "Loader cannot be null");
        this.loader = loader;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;

        try {
            Class ex;

            try {
                ex = Class.forName(description.getMain(), true, this);
            } catch (ClassNotFoundException classnotfoundexception) {
                throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "\'", classnotfoundexception);
            }

            Class pluginClass;

            try {
                pluginClass = ex.asSubclass(JavaPlugin.class);
            } catch (ClassCastException classcastexception) {
                throw new InvalidPluginException("main class `" + description.getMain() + "\' does not extend JavaPlugin", classcastexception);
            }

            this.plugin = (JavaPlugin) pluginClass.newInstance();
        } catch (IllegalAccessException illegalaccessexception) {
            throw new InvalidPluginException("No public constructor", illegalaccessexception);
        } catch (InstantiationException instantiationexception) {
            throw new InvalidPluginException("Abnormal plugin type", instantiationexception);
        }
    }

    protected Class findClass(String name) throws ClassNotFoundException {
        return this.findClass(name, true);
    }

    Class findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (!name.startsWith("org.bukkit.") && !name.startsWith("net.minecraft.")) {
            Class result = (Class) this.classes.get(name);

            if (result == null) {
                if (checkGlobal) {
                    result = this.loader.getClassByName(name);
                }

                if (result == null) {
                    result = super.findClass(name);
                    if (result != null) {
                        this.loader.setClass(name, result);
                    }
                }

                this.classes.put(name, result);
            }

            return result;
        } else {
            throw new ClassNotFoundException(name);
        }
    }

    Set getClasses() {
        return this.classes.keySet();
    }

    synchronized void initialize(JavaPlugin javaPlugin) {
        Validate.notNull(javaPlugin, "Initializing plugin cannot be null");
        Validate.isTrue(javaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin == null && this.pluginInit == null) {
            this.pluginState = new IllegalStateException("Initial initialization");
            this.pluginInit = javaPlugin;
            javaPlugin.init(this.loader, this.loader.server, this.description, this.dataFolder, this.file, this);
        } else {
            throw new IllegalArgumentException("Plugin already initialized!", this.pluginState);
        }
    }
}
