package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "ctx",
    category = "Lookup"
)
public class ContextMapLookup implements StrLookup {

    public String lookup(String s) {
        return ThreadContext.get(s);
    }

    public String lookup(LogEvent logevent, String s) {
        return (String) logevent.getContextMap().get(s);
    }
}
