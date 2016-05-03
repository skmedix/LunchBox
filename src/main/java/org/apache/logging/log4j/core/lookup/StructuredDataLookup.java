package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.StructuredDataMessage;

@Plugin(
    name = "sd",
    category = "Lookup"
)
public class StructuredDataLookup implements StrLookup {

    public String lookup(String s) {
        return null;
    }

    public String lookup(LogEvent logevent, String s) {
        if (logevent != null && logevent.getMessage() instanceof StructuredDataMessage) {
            StructuredDataMessage structureddatamessage = (StructuredDataMessage) logevent.getMessage();

            return s.equalsIgnoreCase("id") ? structureddatamessage.getId().getName() : (s.equalsIgnoreCase("type") ? structureddatamessage.getType() : structureddatamessage.get(s));
        } else {
            return null;
        }
    }
}
