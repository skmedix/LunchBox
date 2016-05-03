package org.apache.logging.log4j.core.config.plugins;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.Appender;

@Plugin(
    name = "appenders",
    category = "Core"
)
public final class AppendersPlugin {

    @PluginFactory
    public static ConcurrentMap createAppenders(@PluginElement("Appenders") Appender[] aappender) {
        ConcurrentHashMap concurrenthashmap = new ConcurrentHashMap();
        Appender[] aappender1 = aappender;
        int i = aappender.length;

        for (int j = 0; j < i; ++j) {
            Appender appender = aappender1[j];

            concurrenthashmap.put(appender.getName(), appender);
        }

        return concurrenthashmap;
    }
}
