package org.apache.logging.log4j.core.pattern;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.MapMessage;

@Plugin(
    name = "MapPatternConverter",
    category = "Converter"
)
@ConverterKeys({ "K", "map", "MAP"})
public final class MapPatternConverter extends LogEventPatternConverter {

    private final String key;

    private MapPatternConverter(String[] astring) {
        super(astring != null && astring.length > 0 ? "MAP{" + astring[0] + "}" : "MAP", "map");
        this.key = astring != null && astring.length > 0 ? astring[0] : null;
    }

    public static MapPatternConverter newInstance(String[] astring) {
        return new MapPatternConverter(astring);
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        if (logevent.getMessage() instanceof MapMessage) {
            MapMessage mapmessage = (MapMessage) logevent.getMessage();
            Map map = mapmessage.getData();

            if (this.key == null) {
                if (map.size() == 0) {
                    stringbuilder.append("{}");
                    return;
                }

                StringBuilder stringbuilder1 = new StringBuilder("{");
                TreeSet treeset = new TreeSet(map.keySet());

                String s;

                for (Iterator iterator = treeset.iterator(); iterator.hasNext(); stringbuilder1.append(s).append("=").append((String) map.get(s))) {
                    s = (String) iterator.next();
                    if (stringbuilder1.length() > 1) {
                        stringbuilder1.append(", ");
                    }
                }

                stringbuilder1.append("}");
                stringbuilder.append(stringbuilder1);
            } else {
                String s1 = (String) map.get(this.key);

                if (s1 != null) {
                    stringbuilder.append(s1);
                }
            }

        }
    }
}
