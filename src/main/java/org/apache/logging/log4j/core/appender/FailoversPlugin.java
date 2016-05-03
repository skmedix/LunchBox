package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "failovers",
    category = "Core"
)
public final class FailoversPlugin {

    private static final Logger LOGGER = StatusLogger.getLogger();

    @PluginFactory
    public static String[] createFailovers(@PluginElement("AppenderRef") AppenderRef... aappenderref) {
        if (aappenderref == null) {
            FailoversPlugin.LOGGER.error("failovers must contain an appender reference");
            return null;
        } else {
            String[] astring = new String[aappenderref.length];

            for (int i = 0; i < aappenderref.length; ++i) {
                astring[i] = aappenderref[i].getRef();
            }

            return astring;
        }
    }
}
