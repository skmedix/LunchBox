package org.bukkit.plugin;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.Validate;
import org.bukkit.Server;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.TimingsCommand;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.FileUtil;

public final class SimplePluginManager implements PluginManager {

    private final Server server;
    private final Map fileAssociations = new HashMap();
    private final List plugins = new ArrayList();
    private final Map lookupNames = new HashMap();
    private static File updateDirectory = null;
    private final SimpleCommandMap commandMap;
    private final Map permissions = new HashMap();
    private final Map defaultPerms = new LinkedHashMap();
    private final Map permSubs = new HashMap();
    private final Map defSubs = new HashMap();
    private boolean useTimings = false;

    public SimplePluginManager(Server instance, SimpleCommandMap commandMap) {
        this.server = instance;
        this.commandMap = commandMap;
        this.defaultPerms.put(Boolean.valueOf(true), new HashSet());
        this.defaultPerms.put(Boolean.valueOf(false), new HashSet());
    }

    public void registerInterface(Class loader) throws IllegalArgumentException {
        if (PluginLoader.class.isAssignableFrom(loader)) {
            PluginLoader instance;

            try {
                Constructor patterns = loader.getConstructor(new Class[] { Server.class});

                instance = (PluginLoader) patterns.newInstance(new Object[] { this.server});
            } catch (NoSuchMethodException nosuchmethodexception) {
                String pattern = loader.getName();

                throw new IllegalArgumentException(String.format("Class %s does not have a public %s(Server) constructor", new Object[] { pattern, pattern}), nosuchmethodexception);
            } catch (Exception exception) {
                throw new IllegalArgumentException(String.format("Unexpected exception %s while attempting to construct a new instance of %s", new Object[] { exception.getClass().getName(), loader.getName()}), exception);
            }

            Pattern[] apattern = instance.getPluginFileFilters();

            synchronized (this) {
                Pattern[] apattern1 = apattern;
                int i = apattern.length;

                for (int j = 0; j < i; ++j) {
                    Pattern pattern = apattern1[j];

                    this.fileAssociations.put(pattern, instance);
                }

            }
        } else {
            throw new IllegalArgumentException(String.format("Class %s does not implement interface PluginLoader", new Object[] { loader.getName()}));
        }
    }

    public Plugin[] loadPlugins(File directory) {
        Validate.notNull(directory, "Directory cannot be null");
        Validate.isTrue(directory.isDirectory(), "Directory must be a directory");
        ArrayList result = new ArrayList();
        Set filters = this.fileAssociations.keySet();

        if (!this.server.getUpdateFolder().equals("")) {
            SimplePluginManager.updateDirectory = new File(directory, this.server.getUpdateFolder());
        }

        HashMap plugins = new HashMap();
        HashSet loadedPlugins = new HashSet();
        HashMap dependencies = new HashMap();
        HashMap softDependencies = new HashMap();
        File[] file;
        int failedPluginIterator = (file = directory.listFiles()).length;

        for (int pluginIterator = 0; pluginIterator < failedPluginIterator; ++pluginIterator) {
            File missingDependency = file[pluginIterator];
            PluginLoader ex = null;
            Iterator replacedFile = filters.iterator();

            Pattern file1;

            while (replacedFile.hasNext()) {
                file1 = (Pattern) replacedFile.next();
                Matcher softDependencySet = file1.matcher(missingDependency.getName());

                if (softDependencySet.find()) {
                    ex = (PluginLoader) this.fileAssociations.get(file1);
                }
            }

            if (ex != null) {
                file1 = null;

                PluginDescriptionFile plugindescriptionfile;

                try {
                    plugindescriptionfile = ex.getPluginDescription(missingDependency);
                    String s = plugindescriptionfile.getName();

                    if (s.equalsIgnoreCase("bukkit") || s.equalsIgnoreCase("minecraft") || s.equalsIgnoreCase("mojang")) {
                        this.server.getLogger().log(Level.SEVERE, "Could not load \'" + missingDependency.getPath() + "\' in folder \'" + directory.getPath() + "\': Restricted Name");
                        continue;
                    }

                    if (plugindescriptionfile.rawName.indexOf(32) != -1) {
                        this.server.getLogger().warning(String.format("Plugin `%s\' uses the space-character (0x20) in its name `%s\' - this is discouraged", new Object[] { plugindescriptionfile.getFullName(), plugindescriptionfile.rawName}));
                    }
                } catch (InvalidDescriptionException invaliddescriptionexception) {
                    this.server.getLogger().log(Level.SEVERE, "Could not load \'" + missingDependency.getPath() + "\' in folder \'" + directory.getPath() + "\'", invaliddescriptionexception);
                    continue;
                }

                File file = (File) plugins.put(plugindescriptionfile.getName(), missingDependency);

                if (file != null) {
                    this.server.getLogger().severe(String.format("Ambiguous plugin name `%s\' for files `%s\' and `%s\' in `%s\'", new Object[] { plugindescriptionfile.getName(), missingDependency.getPath(), file.getPath(), directory.getPath()}));
                }

                List list = plugindescriptionfile.getSoftDepend();

                if (list != null && !list.isEmpty()) {
                    if (softDependencies.containsKey(plugindescriptionfile.getName())) {
                        ((Collection) softDependencies.get(plugindescriptionfile.getName())).addAll(list);
                    } else {
                        softDependencies.put(plugindescriptionfile.getName(), new LinkedList(list));
                    }
                }

                List dependencySet = plugindescriptionfile.getDepend();

                if (dependencySet != null && !dependencySet.isEmpty()) {
                    dependencies.put(plugindescriptionfile.getName(), new LinkedList(dependencySet));
                }

                List loadBeforeSet = plugindescriptionfile.getLoadBefore();

                if (loadBeforeSet != null && !loadBeforeSet.isEmpty()) {
                    Iterator iterator = loadBeforeSet.iterator();

                    while (iterator.hasNext()) {
                        String loadBeforeTarget = (String) iterator.next();

                        if (softDependencies.containsKey(loadBeforeTarget)) {
                            ((Collection) softDependencies.get(loadBeforeTarget)).add(plugindescriptionfile.getName());
                        } else {
                            LinkedList shortSoftDependency = new LinkedList();

                            shortSoftDependency.add(plugindescriptionfile.getName());
                            softDependencies.put(loadBeforeTarget, shortSoftDependency);
                        }
                    }
                }
            }
        }

        while (!plugins.isEmpty()) {
            boolean flag = true;
            Iterator iterator1 = plugins.keySet().iterator();

            File file1;
            String s1;

            while (iterator1.hasNext()) {
                s1 = (String) iterator1.next();
                Iterator iterator2;
                String s2;

                if (dependencies.containsKey(s1)) {
                    iterator2 = ((Collection) dependencies.get(s1)).iterator();

                    while (iterator2.hasNext()) {
                        s2 = (String) iterator2.next();
                        if (loadedPlugins.contains(s2)) {
                            iterator2.remove();
                        } else if (!plugins.containsKey(s2)) {
                            flag = false;
                            File file2 = (File) plugins.get(s1);

                            iterator1.remove();
                            softDependencies.remove(s1);
                            dependencies.remove(s1);
                            this.server.getLogger().log(Level.SEVERE, "Could not load \'" + file2.getPath() + "\' in folder \'" + directory.getPath() + "\'", new UnknownDependencyException(s2));
                            break;
                        }
                    }

                    if (dependencies.containsKey(s1) && ((Collection) dependencies.get(s1)).isEmpty()) {
                        dependencies.remove(s1);
                    }
                }

                if (softDependencies.containsKey(s1)) {
                    iterator2 = ((Collection) softDependencies.get(s1)).iterator();

                    while (iterator2.hasNext()) {
                        s2 = (String) iterator2.next();
                        if (!plugins.containsKey(s2)) {
                            iterator2.remove();
                        }
                    }

                    if (((Collection) softDependencies.get(s1)).isEmpty()) {
                        softDependencies.remove(s1);
                    }
                }

                if (!dependencies.containsKey(s1) && !softDependencies.containsKey(s1) && plugins.containsKey(s1)) {
                    file1 = (File) plugins.get(s1);
                    iterator1.remove();
                    flag = false;

                    try {
                        result.add(this.loadPlugin(file1));
                        loadedPlugins.add(s1);
                    } catch (InvalidPluginException invalidpluginexception) {
                        this.server.getLogger().log(Level.SEVERE, "Could not load \'" + file1.getPath() + "\' in folder \'" + directory.getPath() + "\'", invalidpluginexception);
                    }
                }
            }

            if (flag) {
                iterator1 = plugins.keySet().iterator();

                while (true) {
                    if (iterator1.hasNext()) {
                        s1 = (String) iterator1.next();
                        if (dependencies.containsKey(s1)) {
                            continue;
                        }

                        softDependencies.remove(s1);
                        flag = false;
                        file1 = (File) plugins.get(s1);
                        iterator1.remove();

                        try {
                            result.add(this.loadPlugin(file1));
                            loadedPlugins.add(s1);
                        } catch (InvalidPluginException invalidpluginexception1) {
                            this.server.getLogger().log(Level.SEVERE, "Could not load \'" + file1.getPath() + "\' in folder \'" + directory.getPath() + "\'", invalidpluginexception1);
                            continue;
                        }
                    }

                    if (flag) {
                        softDependencies.clear();
                        dependencies.clear();
                        Iterator iterator3 = plugins.values().iterator();

                        while (iterator3.hasNext()) {
                            file1 = (File) iterator3.next();
                            iterator3.remove();
                            this.server.getLogger().log(Level.SEVERE, "Could not load \'" + file1.getPath() + "\' in folder \'" + directory.getPath() + "\': circular dependency detected");
                        }
                    }
                    break;
                }
            }
        }

        TimingsCommand.timingStart = System.nanoTime();
        return (Plugin[]) result.toArray(new Plugin[result.size()]);
    }

    public synchronized Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException {
        Validate.notNull(file, "File cannot be null");
        this.checkUpdate(file);
        Set filters = this.fileAssociations.keySet();
        Plugin result = null;
        Iterator iterator = filters.iterator();

        while (iterator.hasNext()) {
            Pattern filter = (Pattern) iterator.next();
            String name = file.getName();
            Matcher match = filter.matcher(name);

            if (match.find()) {
                PluginLoader loader = (PluginLoader) this.fileAssociations.get(filter);

                result = loader.loadPlugin(file);
            }
        }

        if (result != null) {
            this.plugins.add(result);
            this.lookupNames.put(result.getDescription().getName(), result);
        }

        return result;
    }

    private void checkUpdate(File file) {
        if (SimplePluginManager.updateDirectory != null && SimplePluginManager.updateDirectory.isDirectory()) {
            File updateFile = new File(SimplePluginManager.updateDirectory, file.getName());

            if (updateFile.isFile() && FileUtil.copy(updateFile, file)) {
                updateFile.delete();
            }

        }
    }

    public synchronized Plugin getPlugin(String name) {
        return (Plugin) this.lookupNames.get(name.replace(' ', '_'));
    }

    public synchronized Plugin[] getPlugins() {
        return (Plugin[]) this.plugins.toArray(new Plugin[0]);
    }

    public boolean isPluginEnabled(String name) {
        Plugin plugin = this.getPlugin(name);

        return this.isPluginEnabled(plugin);
    }

    public boolean isPluginEnabled(Plugin plugin) {
        return plugin != null && this.plugins.contains(plugin) ? plugin.isEnabled() : false;
    }

    public void enablePlugin(Plugin plugin) {
        if (!plugin.isEnabled()) {
            List pluginCommands = PluginCommandYamlParser.parse(plugin);

            if (!pluginCommands.isEmpty()) {
                this.commandMap.registerAll(plugin.getDescription().getName(), pluginCommands);
            }

            try {
                plugin.getPluginLoader().enablePlugin(plugin);
            } catch (Throwable throwable) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while enabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", throwable);
            }

            HandlerList.bakeAll();
        }

    }

    public void disablePlugins() {
        Plugin[] plugins = this.getPlugins();

        for (int i = plugins.length - 1; i >= 0; --i) {
            this.disablePlugin(plugins[i]);
        }

    }

    public void disablePlugin(Plugin plugin) {
        if (plugin.isEnabled()) {
            try {
                plugin.getPluginLoader().disablePlugin(plugin);
            } catch (Throwable throwable) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while disabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", throwable);
            }

            try {
                this.server.getScheduler().cancelTasks(plugin);
            } catch (Throwable throwable1) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while cancelling tasks for " + plugin.getDescription().getFullName() + " (Is it up to date?)", throwable1);
            }

            try {
                this.server.getServicesManager().unregisterAll(plugin);
            } catch (Throwable throwable2) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while unregistering services for " + plugin.getDescription().getFullName() + " (Is it up to date?)", throwable2);
            }

            try {
                HandlerList.unregisterAll(plugin);
            } catch (Throwable throwable3) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while unregistering events for " + plugin.getDescription().getFullName() + " (Is it up to date?)", throwable3);
            }

            try {
                this.server.getMessenger().unregisterIncomingPluginChannel(plugin);
                this.server.getMessenger().unregisterOutgoingPluginChannel(plugin);
            } catch (Throwable throwable4) {
                this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while unregistering plugin channels for " + plugin.getDescription().getFullName() + " (Is it up to date?)", throwable4);
            }
        }

    }

    public void clearPlugins() {
        synchronized (this) {
            this.disablePlugins();
            this.plugins.clear();
            this.lookupNames.clear();
            HandlerList.unregisterAll();
            this.fileAssociations.clear();
            this.permissions.clear();
            ((Set) this.defaultPerms.get(Boolean.valueOf(true))).clear();
            ((Set) this.defaultPerms.get(Boolean.valueOf(false))).clear();
        }
    }

    public void callEvent(Event event) {
        if (event.isAsynchronous()) {
            if (Thread.holdsLock(this)) {
                throw new IllegalStateException(event.getEventName() + " cannot be triggered asynchronously from inside synchronized code.");
            }

            if (this.server.isPrimaryThread()) {
                throw new IllegalStateException(event.getEventName() + " cannot be triggered asynchronously from primary server thread.");
            }

            this.fireEvent(event);
        } else {
            synchronized (this) {
                this.fireEvent(event);
            }
        }

    }

    private void fireEvent(Event event) {
        HandlerList handlers = event.getHandlers();
        RegisteredListener[] listeners = handlers.getRegisteredListeners();
        RegisteredListener[] aregisteredlistener = listeners;
        int i = listeners.length;

        for (int j = 0; j < i; ++j) {
            RegisteredListener registration = aregisteredlistener[j];

            if (registration.getPlugin().isEnabled()) {
                try {
                    registration.callEvent(event);
                } catch (AuthorNagException authornagexception) {
                    Plugin plugin = registration.getPlugin();

                    if (plugin.isNaggable()) {
                        plugin.setNaggable(false);
                        this.server.getLogger().log(Level.SEVERE, String.format("Nag author(s): \'%s\' of \'%s\' about the following: %s", new Object[] { plugin.getDescription().getAuthors(), plugin.getDescription().getFullName(), authornagexception.getMessage()}));
                    }
                } catch (Throwable throwable) {
                    this.server.getLogger().log(Level.SEVERE, "Could not pass event " + event.getEventName() + " to " + registration.getPlugin().getDescription().getFullName(), throwable);
                }
            }
        }

    }

    public void registerEvents(Listener listener, Plugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + listener + " while not enabled");
        } else {
            Iterator iterator = plugin.getPluginLoader().createRegisteredListeners(listener, plugin).entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                this.getEventListeners(this.getRegistrationClass((Class) entry.getKey())).registerAll((Collection) entry.getValue());
            }

        }
    }

    public void registerEvent(Class event, Listener listener, EventPriority priority, EventExecutor executor, Plugin plugin) {
        this.registerEvent(event, listener, priority, executor, plugin, false);
    }

    public void registerEvent(Class event, Listener listener, EventPriority priority, EventExecutor executor, Plugin plugin, boolean ignoreCancelled) {
        Validate.notNull(listener, "Listener cannot be null");
        Validate.notNull(priority, "Priority cannot be null");
        Validate.notNull(executor, "Executor cannot be null");
        Validate.notNull(plugin, "Plugin cannot be null");
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + event + " while not enabled");
        } else {
            if (this.useTimings) {
                this.getEventListeners(event).register(new TimedRegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
            } else {
                this.getEventListeners(event).register(new RegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
            }

        }
    }

    private HandlerList getEventListeners(Class type) {
        try {
            Method e = this.getRegistrationClass(type).getDeclaredMethod("getHandlerList", new Class[0]);

            e.setAccessible(true);
            return (HandlerList) e.invoke((Object) null, new Object[0]);
        } catch (Exception exception) {
            throw new IllegalPluginAccessException(exception.toString());
        }
    }

    private Class getRegistrationClass(Class clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList", new Class[0]);
            return clazz;
        } catch (NoSuchMethodException nosuchmethodexception) {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class) && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return this.getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
            }
        }
    }

    public Permission getPermission(String name) {
        return (Permission) this.permissions.get(name.toLowerCase());
    }

    public void addPermission(Permission perm) {
        String name = perm.getName().toLowerCase();

        if (this.permissions.containsKey(name)) {
            throw new IllegalArgumentException("The permission " + name + " is already defined!");
        } else {
            this.permissions.put(name, perm);
            this.calculatePermissionDefault(perm);
        }
    }

    public Set getDefaultPermissions(boolean op) {
        return ImmutableSet.copyOf((Collection) this.defaultPerms.get(Boolean.valueOf(op)));
    }

    public void removePermission(Permission perm) {
        this.removePermission(perm.getName());
    }

    public void removePermission(String name) {
        this.permissions.remove(name.toLowerCase());
    }

    public void recalculatePermissionDefaults(Permission perm) {
        if (perm != null && this.permissions.containsKey(perm.getName().toLowerCase())) {
            ((Set) this.defaultPerms.get(Boolean.valueOf(true))).remove(perm);
            ((Set) this.defaultPerms.get(Boolean.valueOf(false))).remove(perm);
            this.calculatePermissionDefault(perm);
        }

    }

    private void calculatePermissionDefault(Permission perm) {
        if (perm.getDefault() == PermissionDefault.OP || perm.getDefault() == PermissionDefault.TRUE) {
            ((Set) this.defaultPerms.get(Boolean.valueOf(true))).add(perm);
            this.dirtyPermissibles(true);
        }

        if (perm.getDefault() == PermissionDefault.NOT_OP || perm.getDefault() == PermissionDefault.TRUE) {
            ((Set) this.defaultPerms.get(Boolean.valueOf(false))).add(perm);
            this.dirtyPermissibles(false);
        }

    }

    private void dirtyPermissibles(boolean op) {
        Set permissibles = this.getDefaultPermSubscriptions(op);
        Iterator iterator = permissibles.iterator();

        while (iterator.hasNext()) {
            Permissible p = (Permissible) iterator.next();

            p.recalculatePermissions();
        }

    }

    public void subscribeToPermission(String permission, Permissible permissible) {
        String name = permission.toLowerCase();
        Object map = (Map) this.permSubs.get(name);

        if (map == null) {
            map = new WeakHashMap();
            this.permSubs.put(name, map);
        }

        ((Map) map).put(permissible, Boolean.valueOf(true));
    }

    public void unsubscribeFromPermission(String permission, Permissible permissible) {
        String name = permission.toLowerCase();
        Map map = (Map) this.permSubs.get(name);

        if (map != null) {
            map.remove(permissible);
            if (map.isEmpty()) {
                this.permSubs.remove(name);
            }
        }

    }

    public Set getPermissionSubscriptions(String permission) {
        String name = permission.toLowerCase();
        Map map = (Map) this.permSubs.get(name);

        return map == null ? ImmutableSet.of() : ImmutableSet.copyOf((Collection) map.keySet());
    }

    public void subscribeToDefaultPerms(boolean op, Permissible permissible) {
        Object map = (Map) this.defSubs.get(Boolean.valueOf(op));

        if (map == null) {
            map = new WeakHashMap();
            this.defSubs.put(Boolean.valueOf(op), map);
        }

        ((Map) map).put(permissible, Boolean.valueOf(true));
    }

    public void unsubscribeFromDefaultPerms(boolean op, Permissible permissible) {
        Map map = (Map) this.defSubs.get(Boolean.valueOf(op));

        if (map != null) {
            map.remove(permissible);
            if (map.isEmpty()) {
                this.defSubs.remove(Boolean.valueOf(op));
            }
        }

    }

    public Set getDefaultPermSubscriptions(boolean op) {
        Map map = (Map) this.defSubs.get(Boolean.valueOf(op));

        return map == null ? ImmutableSet.of() : ImmutableSet.copyOf((Collection) map.keySet());
    }

    public Set getPermissions() {
        return new HashSet(this.permissions.values());
    }

    public boolean useTimings() {
        return this.useTimings;
    }

    public void useTimings(boolean use) {
        this.useTimings = use;
    }
}
