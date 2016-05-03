package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "property",
    category = "Core",
    printObject = true
)
public final class Property {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private final String name;
    private final String value;

    private Property(String s, String s1) {
        this.name = s;
        this.value = s1;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    @PluginFactory
    public static Property createProperty(@PluginAttribute("name") String s, @PluginValue("value") String s1) {
        if (s == null) {
            Property.LOGGER.error("Property key cannot be null");
        }

        return new Property(s, s1);
    }

    public String toString() {
        return this.name + "=" + this.value;
    }
}
