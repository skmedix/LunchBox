package org.apache.logging.log4j.core.appender.routing;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(
    name = "Routing",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class RoutingAppender extends AbstractAppender {

    private static final String DEFAULT_KEY = "ROUTING_APPENDER_DEFAULT";
    private final Routes routes;
    private final Route defaultRoute;
    private final Configuration config;
    private final ConcurrentMap appenders = new ConcurrentHashMap();
    private final RewritePolicy rewritePolicy;

    private RoutingAppender(String s, Filter filter, boolean flag, Routes routes, RewritePolicy rewritepolicy, Configuration configuration) {
        super(s, filter, (Layout) null, flag);
        this.routes = routes;
        this.config = configuration;
        this.rewritePolicy = rewritepolicy;
        Route route = null;
        Route[] aroute = routes.getRoutes();
        int i = aroute.length;

        for (int j = 0; j < i; ++j) {
            Route route1 = aroute[j];

            if (route1.getKey() == null) {
                if (route == null) {
                    route = route1;
                } else {
                    this.error("Multiple default routes. Route " + route1.toString() + " will be ignored");
                }
            }
        }

        this.defaultRoute = route;
    }

    public void start() {
        Map map = this.config.getAppenders();
        Route[] aroute = this.routes.getRoutes();
        int i = aroute.length;

        for (int j = 0; j < i; ++j) {
            Route route = aroute[j];

            if (route.getAppenderRef() != null) {
                Appender appender = (Appender) map.get(route.getAppenderRef());

                if (appender != null) {
                    String s = route == this.defaultRoute ? "ROUTING_APPENDER_DEFAULT" : route.getKey();

                    this.appenders.put(s, new AppenderControl(appender, (Level) null, (Filter) null));
                } else {
                    RoutingAppender.LOGGER.error("Appender " + route.getAppenderRef() + " cannot be located. Route ignored");
                }
            }
        }

        super.start();
    }

    public void stop() {
        super.stop();
        Map map = this.config.getAppenders();
        Iterator iterator = this.appenders.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            String s = ((AppenderControl) entry.getValue()).getAppender().getName();

            if (!map.containsKey(s)) {
                ((AppenderControl) entry.getValue()).getAppender().stop();
            }
        }

    }

    public void append(LogEvent logevent) {
        if (this.rewritePolicy != null) {
            logevent = this.rewritePolicy.rewrite(logevent);
        }

        String s = this.config.getStrSubstitutor().replace(logevent, this.routes.getPattern());
        AppenderControl appendercontrol = this.getControl(s, logevent);

        if (appendercontrol != null) {
            appendercontrol.callAppender(logevent);
        }

    }

    private synchronized AppenderControl getControl(String s, LogEvent logevent) {
        AppenderControl appendercontrol = (AppenderControl) this.appenders.get(s);

        if (appendercontrol != null) {
            return appendercontrol;
        } else {
            Route route = null;
            Route[] aroute = this.routes.getRoutes();
            int i = aroute.length;

            for (int j = 0; j < i; ++j) {
                Route route1 = aroute[j];

                if (route1.getAppenderRef() == null && s.equals(route1.getKey())) {
                    route = route1;
                    break;
                }
            }

            if (route == null) {
                route = this.defaultRoute;
                appendercontrol = (AppenderControl) this.appenders.get("ROUTING_APPENDER_DEFAULT");
                if (appendercontrol != null) {
                    return appendercontrol;
                }
            }

            if (route != null) {
                Appender appender = this.createAppender(route, logevent);

                if (appender == null) {
                    return null;
                }

                appendercontrol = new AppenderControl(appender, (Level) null, (Filter) null);
                this.appenders.put(s, appendercontrol);
            }

            return appendercontrol;
        }
    }

    private Appender createAppender(Route route, LogEvent logevent) {
        Node node = route.getNode();
        Iterator iterator = node.getChildren().iterator();

        Node node1;

        do {
            if (!iterator.hasNext()) {
                RoutingAppender.LOGGER.error("No Appender was configured for route " + route.getKey());
                return null;
            }

            node1 = (Node) iterator.next();
        } while (!node1.getType().getElementName().equals("appender"));

        Node node2 = new Node(node1);

        this.config.createConfiguration(node2, logevent);
        if (node2.getObject() instanceof Appender) {
            Appender appender = (Appender) node2.getObject();

            appender.start();
            return appender;
        } else {
            RoutingAppender.LOGGER.error("Unable to create Appender of type " + node1.getName());
            return null;
        }
    }

    @PluginFactory
    public static RoutingAppender createAppender(@PluginAttribute("name") String s, @PluginAttribute("ignoreExceptions") String s1, @PluginElement("Routes") Routes routes, @PluginConfiguration Configuration configuration, @PluginElement("RewritePolicy") RewritePolicy rewritepolicy, @PluginElement("Filters") Filter filter) {
        boolean flag = Booleans.parseBoolean(s1, true);

        if (s == null) {
            RoutingAppender.LOGGER.error("No name provided for RoutingAppender");
            return null;
        } else if (routes == null) {
            RoutingAppender.LOGGER.error("No routes defined for RoutingAppender");
            return null;
        } else {
            return new RoutingAppender(s, filter, flag, routes, rewritepolicy, configuration);
        }
    }
}
