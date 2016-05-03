package org.bukkit.configuration;

import java.util.Map;

public interface Configuration extends ConfigurationSection {

    void addDefault(String s, Object object);

    void addDefaults(Map map);

    void addDefaults(Configuration configuration);

    void setDefaults(Configuration configuration);

    Configuration getDefaults();

    ConfigurationOptions options();
}
