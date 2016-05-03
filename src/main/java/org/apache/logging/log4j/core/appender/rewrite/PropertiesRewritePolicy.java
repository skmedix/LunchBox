package org.apache.logging.log4j.core.appender.rewrite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "PropertiesRewritePolicy",
    category = "Core",
    elementType = "rewritePolicy",
    printObject = true
)
public final class PropertiesRewritePolicy implements RewritePolicy {

    protected static final Logger LOGGER = StatusLogger.getLogger();
    private final Map properties;
    private final Configuration config;

    private PropertiesRewritePolicy(Configuration configuration, List list) {
        this.config = configuration;
        this.properties = new HashMap(list.size());
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Property property = (Property) iterator.next();
            Boolean obool = Boolean.valueOf(property.getValue().contains("${"));

            this.properties.put(property, obool);
        }

    }

    public LogEvent rewrite(LogEvent logevent) {
        HashMap hashmap = new HashMap(logevent.getContextMap());
        Iterator iterator = this.properties.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            Property property = (Property) entry.getKey();

            hashmap.put(property.getName(), ((Boolean) entry.getValue()).booleanValue() ? this.config.getStrSubstitutor().replace(property.getValue()) : property.getValue());
        }

        return new Log4jLogEvent(logevent.getLoggerName(), logevent.getMarker(), logevent.getFQCN(), logevent.getLevel(), logevent.getMessage(), logevent.getThrown(), hashmap, logevent.getContextStack(), logevent.getThreadName(), logevent.getSource(), logevent.getMillis());
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(" {");
        boolean flag = true;

        for (Iterator iterator = this.properties.entrySet().iterator(); iterator.hasNext(); flag = false) {
            Entry entry = (Entry) iterator.next();

            if (!flag) {
                stringbuilder.append(", ");
            }

            Property property = (Property) entry.getKey();

            stringbuilder.append(property.getName()).append("=").append(property.getValue());
        }

        stringbuilder.append("}");
        return stringbuilder.toString();
    }

    @PluginFactory
    public static PropertiesRewritePolicy createPolicy(@PluginConfiguration Configuration configuration, @PluginElement("Properties") Property[] aproperty) {
        if (aproperty != null && aproperty.length != 0) {
            List list = Arrays.asList(aproperty);

            return new PropertiesRewritePolicy(configuration, list);
        } else {
            PropertiesRewritePolicy.LOGGER.error("Properties must be specified for the PropertiesRewritePolicy");
            return null;
        }
    }
}
