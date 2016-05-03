package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(
    name = "KeyValuePair",
    category = "Core",
    printObject = true
)
public class KeyValuePair {

    private final String key;
    private final String value;

    public KeyValuePair(String s, String s1) {
        this.key = s;
        this.value = s1;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return this.key + "=" + this.value;
    }

    @PluginFactory
    public static KeyValuePair createPair(@PluginAttribute("key") String s, @PluginAttribute("value") String s1) {
        return new KeyValuePair(s, s1);
    }
}
