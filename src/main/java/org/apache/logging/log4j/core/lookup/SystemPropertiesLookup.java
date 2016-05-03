package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "sys",
    category = "Lookup"
)
public class SystemPropertiesLookup implements StrLookup {

    public String lookup(String s) {
        try {
            return System.getProperty(s);
        } catch (Exception exception) {
            return null;
        }
    }

    public String lookup(LogEvent logevent, String s) {
        try {
            return System.getProperty(s);
        } catch (Exception exception) {
            return null;
        }
    }
}
