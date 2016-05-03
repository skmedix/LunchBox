package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "env",
    category = "Lookup"
)
public class EnvironmentLookup implements StrLookup {

    public String lookup(String s) {
        return System.getenv(s);
    }

    public String lookup(LogEvent logevent, String s) {
        return System.getenv(s);
    }
}
