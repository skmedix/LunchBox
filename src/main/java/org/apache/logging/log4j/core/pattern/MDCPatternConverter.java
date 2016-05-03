package org.apache.logging.log4j.core.pattern;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "MDCPatternConverter",
    category = "Converter"
)
@ConverterKeys({ "X", "mdc", "MDC"})
public final class MDCPatternConverter extends LogEventPatternConverter {

    private final String key;

    private MDCPatternConverter(String[] astring) {
        super(astring != null && astring.length > 0 ? "MDC{" + astring[0] + "}" : "MDC", "mdc");
        this.key = astring != null && astring.length > 0 ? astring[0] : null;
    }

    public static MDCPatternConverter newInstance(String[] astring) {
        return new MDCPatternConverter(astring);
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        Map map = logevent.getContextMap();

        if (this.key == null) {
            if (map == null || map.size() == 0) {
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
        } else if (map != null) {
            Object object = map.get(this.key);

            if (object != null) {
                stringbuilder.append(object);
            }
        }

    }
}
