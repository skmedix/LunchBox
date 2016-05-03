package org.apache.logging.log4j.core.appender.routing;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "Routes",
    category = "Core",
    printObject = true
)
public final class Routes {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private final String pattern;
    private final Route[] routes;

    private Routes(String s, Route... aroute) {
        this.pattern = s;
        this.routes = aroute;
    }

    public String getPattern() {
        return this.pattern;
    }

    public Route[] getRoutes() {
        return this.routes;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("{");
        boolean flag = true;
        Route[] aroute = this.routes;
        int i = aroute.length;

        for (int j = 0; j < i; ++j) {
            Route route = aroute[j];

            if (!flag) {
                stringbuilder.append(",");
            }

            flag = false;
            stringbuilder.append(route.toString());
        }

        stringbuilder.append("}");
        return stringbuilder.toString();
    }

    @PluginFactory
    public static Routes createRoutes(@PluginAttribute("pattern") String s, @PluginElement("Routes") Route... aroute) {
        if (s == null) {
            Routes.LOGGER.error("A pattern is required");
            return null;
        } else if (aroute != null && aroute.length != 0) {
            return new Routes(s, aroute);
        } else {
            Routes.LOGGER.error("No routes configured");
            return null;
        }
    }
}
