package org.apache.logging.log4j.core.appender.routing;

import java.util.Iterator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginNode;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "Route",
    category = "Core",
    printObject = true,
    deferChildren = true
)
public final class Route {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private final Node node;
    private final String appenderRef;
    private final String key;

    private Route(Node node, String s, String s1) {
        this.node = node;
        this.appenderRef = s;
        this.key = s1;
    }

    public Node getNode() {
        return this.node;
    }

    public String getAppenderRef() {
        return this.appenderRef;
    }

    public String getKey() {
        return this.key;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("Route(");

        stringbuilder.append("type=");
        if (this.appenderRef != null) {
            stringbuilder.append("static Reference=").append(this.appenderRef);
        } else if (this.node != null) {
            stringbuilder.append("dynamic - type=").append(this.node.getName());
        } else {
            stringbuilder.append("invalid Route");
        }

        if (this.key != null) {
            stringbuilder.append(" key=\'").append(this.key).append("\'");
        } else {
            stringbuilder.append(" default");
        }

        stringbuilder.append(")");
        return stringbuilder.toString();
    }

    @PluginFactory
    public static Route createRoute(@PluginAttribute("ref") String s, @PluginAttribute("key") String s1, @PluginNode Node node) {
        if (node != null && node.hasChildren()) {
            Node node1;

            for (Iterator iterator = node.getChildren().iterator(); iterator.hasNext(); node1 = (Node) iterator.next()) {
                ;
            }

            if (s != null) {
                Route.LOGGER.error("A route cannot be configured with an appender reference and an appender definition");
                return null;
            }
        } else if (s == null) {
            Route.LOGGER.error("A route must specify an appender reference or an appender definition");
            return null;
        }

        return new Route(node, s, s1);
    }
}
