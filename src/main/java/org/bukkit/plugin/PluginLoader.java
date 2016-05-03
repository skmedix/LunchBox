package org.bukkit.plugin;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.event.Listener;

public interface PluginLoader {

    Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException;

    PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException;

    Pattern[] getPluginFileFilters();

    Map createRegisteredListeners(Listener listener, Plugin plugin);

    void enablePlugin(Plugin plugin);

    void disablePlugin(Plugin plugin);
}
