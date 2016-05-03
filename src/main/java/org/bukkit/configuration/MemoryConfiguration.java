package org.bukkit.configuration;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.Validate;

public class MemoryConfiguration extends MemorySection implements Configuration {

    protected Configuration defaults;
    protected MemoryConfigurationOptions options;

    public MemoryConfiguration() {}

    public MemoryConfiguration(Configuration defaults) {
        this.defaults = defaults;
    }

    public void addDefault(String path, Object value) {
        Validate.notNull(path, "Path may not be null");
        if (this.defaults == null) {
            this.defaults = new MemoryConfiguration();
        }

        this.defaults.set(path, value);
    }

    public void addDefaults(Map defaults) {
        Validate.notNull(defaults, "Defaults may not be null");
        Iterator iterator = defaults.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            this.addDefault((String) entry.getKey(), entry.getValue());
        }

    }

    public void addDefaults(Configuration defaults) {
        Validate.notNull(defaults, "Defaults may not be null");
        this.addDefaults(defaults.getValues(true));
    }

    public void setDefaults(Configuration defaults) {
        Validate.notNull(defaults, "Defaults may not be null");
        this.defaults = defaults;
    }

    public Configuration getDefaults() {
        return this.defaults;
    }

    public ConfigurationSection getParent() {
        return null;
    }

    public MemoryConfigurationOptions options() {
        if (this.options == null) {
            this.options = new MemoryConfigurationOptions(this);
        }

        return this.options;
    }
}
