package org.bukkit.plugin.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.apache.commons.lang3.Validate;
import org.bukkit.Server;
import org.bukkit.Warning;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;
import org.spigotmc.CustomTimingsHandler;
import org.yaml.snakeyaml.error.YAMLException;

public final class JavaPluginLoader implements PluginLoader {

    final Server server;
    private final Pattern[] fileFilters = new Pattern[] { Pattern.compile("\\.jar$")};
    private final Map classes = new ConcurrentHashMap();
    private final Map loaders = new LinkedHashMap();
    public static final CustomTimingsHandler pluginParentTimer = new CustomTimingsHandler("** Plugins");

    /** @deprecated */
    @Deprecated
    public JavaPluginLoader(Server instance) {
        Validate.notNull(instance, "Server cannot be null");
        this.server = instance;
    }

    public Plugin loadPlugin(File file) throws InvalidPluginException {
        Validate.notNull(file, "File cannot be null");
        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));
        } else {
            PluginDescriptionFile description;

            try {
                description = this.getPluginDescription(file);
            } catch (InvalidDescriptionException invaliddescriptionexception) {
                throw new InvalidPluginException(invaliddescriptionexception);
            }

            File parentFile = file.getParentFile();
            File dataFolder = new File(parentFile, description.getName());
            File oldDataFolder = new File(parentFile, description.getRawName());

            if (!dataFolder.equals(oldDataFolder)) {
                if (dataFolder.isDirectory() && oldDataFolder.isDirectory()) {
                    this.server.getLogger().warning(String.format("While loading %s (%s) found old-data folder: `%s\' next to the new one `%s\'", new Object[] { description.getFullName(), file, oldDataFolder, dataFolder}));
                } else if (oldDataFolder.isDirectory() && !dataFolder.exists()) {
                    if (!oldDataFolder.renameTo(dataFolder)) {
                        throw new InvalidPluginException("Unable to rename old data folder: `" + oldDataFolder + "\' to: `" + dataFolder + "\'");
                    }

                    this.server.getLogger().log(Level.INFO, String.format("While loading %s (%s) renamed data folder: `%s\' to `%s\'", new Object[] { description.getFullName(), file, oldDataFolder, dataFolder}));
                }
            }

            if (dataFolder.exists() && !dataFolder.isDirectory()) {
                throw new InvalidPluginException(String.format("Projected datafolder: `%s\' for %s (%s) exists and is not a directory", new Object[] { dataFolder, description.getFullName(), file}));
            } else {
                Iterator ex = description.getDepend().iterator();

                while (ex.hasNext()) {
                    String loader = (String) ex.next();

                    if (this.loaders == null) {
                        throw new UnknownDependencyException(loader);
                    }

                    PluginClassLoader current = (PluginClassLoader) this.loaders.get(loader);

                    if (current == null) {
                        throw new UnknownDependencyException(loader);
                    }
                }

                PluginClassLoader loader1;

                try {
                    loader1 = new PluginClassLoader(this, this.getClass().getClassLoader(), description, dataFolder, file);
                } catch (InvalidPluginException invalidpluginexception) {
                    throw invalidpluginexception;
                } catch (Throwable throwable) {
                    throw new InvalidPluginException(throwable);
                }

                this.loaders.put(description.getName(), loader1);
                return loader1.plugin;
            }
        }
    }

    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException {
        Validate.notNull(file, "File cannot be null");
        JarFile jar = null;
        InputStream stream = null;

        PluginDescriptionFile plugindescriptionfile;

        try {
            jar = new JarFile(file);
            JarEntry ex = jar.getJarEntry("plugin.yml");

            if (ex == null) {
                throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
            }

            stream = jar.getInputStream(ex);
            plugindescriptionfile = new PluginDescriptionFile(stream);
        } catch (IOException ioexception) {
            throw new InvalidDescriptionException(ioexception);
        } catch (YAMLException yamlexception) {
            throw new InvalidDescriptionException(yamlexception);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException ioexception1) {
                    ;
                }
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ioexception2) {
                    ;
                }
            }

        }

        return plugindescriptionfile;
    }

    public Pattern[] getPluginFileFilters() {
        return (Pattern[]) this.fileFilters.clone();
    }

    Class getClassByName(String name) {
        Class cachedClass = (Class) this.classes.get(name);

        if (cachedClass != null) {
            return cachedClass;
        } else {
            Iterator iterator = this.loaders.keySet().iterator();

            while (iterator.hasNext()) {
                String current = (String) iterator.next();
                PluginClassLoader loader = (PluginClassLoader) this.loaders.get(current);

                try {
                    cachedClass = loader.findClass(name, false);
                } catch (ClassNotFoundException classnotfoundexception) {
                    ;
                }

                if (cachedClass != null) {
                    return cachedClass;
                }
            }

            return null;
        }
    }

    void setClass(String name, Class clazz) {
        if (!this.classes.containsKey(name)) {
            this.classes.put(name, clazz);
            if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                Class serializable = clazz.asSubclass(ConfigurationSerializable.class);

                ConfigurationSerialization.registerClass(serializable);
            }
        }

    }

    private void removeClass(String name) {
        Class clazz = (Class) this.classes.remove(name);

        try {
            if (clazz != null && ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                Class serializable = clazz.asSubclass(ConfigurationSerializable.class);

                ConfigurationSerialization.unregisterClass(serializable);
            }
        } catch (NullPointerException nullpointerexception) {
            ;
        }

    }

    public Map createRegisteredListeners(Listener listener, Plugin plugin) {
        Validate.notNull(plugin, "Plugin can not be null");
        Validate.notNull(listener, "Listener can not be null");
        this.server.getPluginManager().useTimings();
        HashMap ret = new HashMap();

        HashSet methods;

        try {
            Method[] method = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();

            methods = new HashSet(method.length + privateMethods.length, 1.0F);
            Method[] eventSet = method;
            int eventClass = method.length;
            int checkClass = 0;

            label79:
            while (true) {
                Method eh;

                if (checkClass >= eventClass) {
                    eventSet = privateMethods;
                    eventClass = privateMethods.length;
                    checkClass = 0;

                    while (true) {
                        if (checkClass >= eventClass) {
                            break label79;
                        }

                        eh = eventSet[checkClass];
                        methods.add(eh);
                        ++checkClass;
                    }
                }

                eh = eventSet[checkClass];
                methods.add(eh);
                ++checkClass;
            }
        } catch (NoClassDefFoundError noclassdeffounderror) {
            plugin.getLogger().severe("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + listener.getClass() + " because " + noclassdeffounderror.getMessage() + " does not exist.");
            return ret;
        }

        Iterator iterator = methods.iterator();

        while (iterator.hasNext()) {
            final Method method = (Method) iterator.next();
            EventHandler eventhandler = (EventHandler) method.getAnnotation(EventHandler.class);

            if (eventhandler != null && !method.isBridge() && !method.isSynthetic()) {
                final Class oclass;

                if (method.getParameterTypes().length == 1 && Event.class.isAssignableFrom(oclass = method.getParameterTypes()[0])) {
                    final Class oclass1 = oclass.asSubclass(Event.class);

                    method.setAccessible(true);
                    Object object = (Set) ret.get(oclass1);

                    if (object == null) {
                        object = new HashSet();
                        ret.put(oclass1, object);
                    }

                    Class timings = oclass1;

                    while (true) {
                        if (Event.class.isAssignableFrom(timings)) {
                            if (timings.getAnnotation(Deprecated.class) == null) {
                                timings = timings.getSuperclass();
                                continue;
                            }

                            Warning executor = (Warning) timings.getAnnotation(Warning.class);
                            Warning.WarningState warningState = this.server.getWarningState();

                            if (warningState.printFor(executor)) {
                                plugin.getLogger().log(Level.WARNING, String.format("\"%s\" has registered a listener for %s on method \"%s\", but the event is Deprecated. \"%s\"; please notify the authors %s.", new Object[] { plugin.getDescription().getFullName(), timings.getName(), method.toGenericString(), executor != null && executor.reason().length() != 0 ? executor.reason() : "Server performance will be affected", Arrays.toString(plugin.getDescription().getAuthors().toArray())}), warningState == Warning.WarningState.ON ? new AuthorNagException((String) null) : null);
                            }
                        }

                        final CustomTimingsHandler customtimingshandler = new CustomTimingsHandler("Plugin: " + plugin.getDescription().getFullName() + " Event: " + listener.getClass().getName() + "::" + method.getName() + "(" + oclass1.getSimpleName() + ")", JavaPluginLoader.pluginParentTimer);
                        EventExecutor eventexecutor = new EventExecutor() {
                            public void execute(Listener listener, Event event) throws EventException {
                                try {
                                    if (oclass.isAssignableFrom(event.getClass())) {
                                        boolean t = event.isAsynchronous();

                                        if (!t) {
                                            customtimingshandler.startTiming();
                                        }

                                        method.invoke(listener, new Object[] { event});
                                        if (!t) {
                                            customtimingshandler.stopTiming();
                                        }

                                    }
                                } catch (InvocationTargetException invocationtargetexception) {
                                    throw new EventException(invocationtargetexception.getCause());
                                } catch (Throwable throwable) {
                                    throw new EventException(throwable);
                                }
                            }
                        };

                        ((Set) object).add(new RegisteredListener(listener, eventexecutor, eventhandler.priority(), plugin, eventhandler.ignoreCancelled()));
                        break;
                    }
                } else {
                    plugin.getLogger().severe(plugin.getDescription().getFullName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                }
            }
        }

        return ret;
    }

    public void enablePlugin(Plugin plugin) {
        Validate.isTrue(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");
        if (!plugin.isEnabled()) {
            plugin.getLogger().info("Enabling " + plugin.getDescription().getFullName());
            JavaPlugin jPlugin = (JavaPlugin) plugin;
            String pluginName = jPlugin.getDescription().getName();

            if (!this.loaders.containsKey(pluginName)) {
                this.loaders.put(pluginName, (PluginClassLoader) jPlugin.getClassLoader());
            }

            try {
                jPlugin.setEnabled(true);
            } catch (Throwable throwable) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred while enabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", throwable);
            }

            this.server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }

    }

    public void disablePlugin(Plugin plugin) {
        Validate.isTrue(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");
        if (plugin.isEnabled()) {
            String message = String.format("Disabling %s", new Object[] { plugin.getDescription().getFullName()});

            plugin.getLogger().info(message);
            this.server.getPluginManager().callEvent(new PluginDisableEvent(plugin));
            JavaPlugin jPlugin = (JavaPlugin) plugin;
            ClassLoader cloader = jPlugin.getClassLoader();

            try {
                jPlugin.setEnabled(false);
            } catch (Throwable throwable) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred while disabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", throwable);
            }

            this.loaders.remove(jPlugin.getDescription().getName());
            if (cloader instanceof PluginClassLoader) {
                PluginClassLoader loader = (PluginClassLoader) cloader;
                Set names = loader.getClasses();
                Iterator iterator = names.iterator();

                while (iterator.hasNext()) {
                    String name = (String) iterator.next();

                    this.removeClass(name);
                }
            }
        }

    }
}
