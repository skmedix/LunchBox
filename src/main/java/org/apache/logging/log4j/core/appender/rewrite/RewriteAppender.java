package org.apache.logging.log4j.core.appender.rewrite;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(
    name = "Rewrite",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class RewriteAppender extends AbstractAppender {

    private final Configuration config;
    private final ConcurrentMap appenders = new ConcurrentHashMap();
    private final RewritePolicy rewritePolicy;
    private final AppenderRef[] appenderRefs;

    private RewriteAppender(String s, Filter filter, boolean flag, AppenderRef[] aappenderref, RewritePolicy rewritepolicy, Configuration configuration) {
        super(s, filter, (Layout) null, flag);
        this.config = configuration;
        this.rewritePolicy = rewritepolicy;
        this.appenderRefs = aappenderref;
    }

    public void start() {
        Map map = this.config.getAppenders();
        AppenderRef[] aappenderref = this.appenderRefs;
        int i = aappenderref.length;

        for (int j = 0; j < i; ++j) {
            AppenderRef appenderref = aappenderref[j];
            String s = appenderref.getRef();
            Appender appender = (Appender) map.get(s);

            if (appender != null) {
                Filter filter = appender instanceof AbstractAppender ? ((AbstractAppender) appender).getFilter() : null;

                this.appenders.put(s, new AppenderControl(appender, appenderref.getLevel(), filter));
            } else {
                RewriteAppender.LOGGER.error("Appender " + appenderref + " cannot be located. Reference ignored");
            }
        }

        super.start();
    }

    public void stop() {
        super.stop();
    }

    public void append(LogEvent logevent) {
        if (this.rewritePolicy != null) {
            logevent = this.rewritePolicy.rewrite(logevent);
        }

        Iterator iterator = this.appenders.values().iterator();

        while (iterator.hasNext()) {
            AppenderControl appendercontrol = (AppenderControl) iterator.next();

            appendercontrol.callAppender(logevent);
        }

    }

    @PluginFactory
    public static RewriteAppender createAppender(@PluginAttribute("name") String s, @PluginAttribute("ignoreExceptions") String s1, @PluginElement("AppenderRef") AppenderRef[] aappenderref, @PluginConfiguration Configuration configuration, @PluginElement("RewritePolicy") RewritePolicy rewritepolicy, @PluginElement("Filter") Filter filter) {
        boolean flag = Booleans.parseBoolean(s1, true);

        if (s == null) {
            RewriteAppender.LOGGER.error("No name provided for RewriteAppender");
            return null;
        } else if (aappenderref == null) {
            RewriteAppender.LOGGER.error("No appender references defined for RewriteAppender");
            return null;
        } else {
            return new RewriteAppender(s, filter, flag, aappenderref, rewritepolicy, configuration);
        }
    }
}
