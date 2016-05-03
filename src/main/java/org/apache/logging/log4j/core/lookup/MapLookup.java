package org.apache.logging.log4j.core.lookup;

import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.MapMessage;

@Plugin(
    name = "map",
    category = "Lookup"
)
public class MapLookup implements StrLookup {

    private final Map map;

    public MapLookup(Map map) {
        this.map = map;
    }

    public MapLookup() {
        this.map = null;
    }

    public String lookup(String s) {
        if (this.map == null) {
            return null;
        } else {
            String s1 = (String) this.map.get(s);

            return s1 == null ? null : s1;
        }
    }

    public String lookup(LogEvent logevent, String s) {
        if (this.map == null && !(logevent.getMessage() instanceof MapMessage)) {
            return null;
        } else {
            if (this.map != null && this.map.containsKey(s)) {
                String s1 = (String) this.map.get(s);

                if (s1 != null) {
                    return s1;
                }
            }

            return logevent.getMessage() instanceof MapMessage ? ((MapMessage) logevent.getMessage()).get(s) : null;
        }
    }
}
