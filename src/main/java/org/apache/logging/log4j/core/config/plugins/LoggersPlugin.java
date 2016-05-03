package org.apache.logging.log4j.core.config.plugins;

import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Loggers;

@Plugin(
    name = "loggers",
    category = "Core"
)
public final class LoggersPlugin {

    @PluginFactory
    public static Loggers createLoggers(@PluginElement("Loggers") LoggerConfig[] aloggerconfig) {
        ConcurrentHashMap concurrenthashmap = new ConcurrentHashMap();
        LoggerConfig loggerconfig = null;
        LoggerConfig[] aloggerconfig1 = aloggerconfig;
        int i = aloggerconfig.length;

        for (int j = 0; j < i; ++j) {
            LoggerConfig loggerconfig1 = aloggerconfig1[j];

            if (loggerconfig1 != null) {
                if (loggerconfig1.getName().isEmpty()) {
                    loggerconfig = loggerconfig1;
                }

                concurrenthashmap.put(loggerconfig1.getName(), loggerconfig1);
            }
        }

        return new Loggers(concurrenthashmap, loggerconfig);
    }
}
