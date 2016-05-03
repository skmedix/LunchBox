package org.bukkit.plugin;

import java.io.File;
import java.util.Set;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;

public interface PluginManager {

    void registerInterface(Class oclass) throws IllegalArgumentException;

    Plugin getPlugin(String s);

    Plugin[] getPlugins();

    boolean isPluginEnabled(String s);

    boolean isPluginEnabled(Plugin plugin);

    Plugin loadPlugin(File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException;

    Plugin[] loadPlugins(File file);

    void disablePlugins();

    void clearPlugins();

    void callEvent(Event event) throws IllegalStateException;

    void registerEvents(Listener listener, Plugin plugin);

    void registerEvent(Class oclass, Listener listener, EventPriority eventpriority, EventExecutor eventexecutor, Plugin plugin);

    void registerEvent(Class oclass, Listener listener, EventPriority eventpriority, EventExecutor eventexecutor, Plugin plugin, boolean flag);

    void enablePlugin(Plugin plugin);

    void disablePlugin(Plugin plugin);

    Permission getPermission(String s);

    void addPermission(Permission permission);

    void removePermission(Permission permission);

    void removePermission(String s);

    Set getDefaultPermissions(boolean flag);

    void recalculatePermissionDefaults(Permission permission);

    void subscribeToPermission(String s, Permissible permissible);

    void unsubscribeFromPermission(String s, Permissible permissible);

    Set getPermissionSubscriptions(String s);

    void subscribeToDefaultPerms(boolean flag, Permissible permissible);

    void unsubscribeFromDefaultPerms(boolean flag, Permissible permissible);

    Set getDefaultPermSubscriptions(boolean flag);

    Set getPermissions();

    boolean useTimings();
}
