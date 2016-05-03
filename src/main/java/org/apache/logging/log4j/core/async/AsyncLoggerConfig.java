package org.apache.logging.log4j.core.async;

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(
    name = "asyncLogger",
    category = "Core",
    printObject = true
)
public class AsyncLoggerConfig extends LoggerConfig {

    private AsyncLoggerConfigHelper helper;

    public AsyncLoggerConfig() {}

    public AsyncLoggerConfig(String s, Level level, boolean flag) {
        super(s, level, flag);
    }

    protected AsyncLoggerConfig(String s, List list, Filter filter, Level level, boolean flag, Property[] aproperty, Configuration configuration, boolean flag1) {
        super(s, list, filter, level, flag, aproperty, configuration, flag1);
    }

    protected void callAppenders(LogEvent logevent) {
        logevent.getSource();
        logevent.getThreadName();
        this.helper.callAppendersFromAnotherThread(logevent);
    }

    void asyncCallAppenders(LogEvent logevent) {
        super.callAppenders(logevent);
    }

    public void startFilter() {
        if (this.helper == null) {
            this.helper = new AsyncLoggerConfigHelper(this);
        } else {
            AsyncLoggerConfigHelper.claim();
        }

        super.startFilter();
    }

    public void stopFilter() {
        AsyncLoggerConfigHelper.release();
        super.stopFilter();
    }

    @PluginFactory
    public static LoggerConfig createLogger(@PluginAttribute("additivity") String s, @PluginAttribute("level") String s1, @PluginAttribute("name") String s2, @PluginAttribute("includeLocation") String s3, @PluginElement("AppenderRef") AppenderRef[] aappenderref, @PluginElement("Properties") Property[] aproperty, @PluginConfiguration Configuration configuration, @PluginElement("Filters") Filter filter) {
        if (s2 == null) {
            AsyncLoggerConfig.LOGGER.error("Loggers cannot be configured without a name");
            return null;
        } else {
            List list = Arrays.asList(aappenderref);

            Level level;

            try {
                level = Level.toLevel(s1, Level.ERROR);
            } catch (Exception exception) {
                AsyncLoggerConfig.LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", new Object[] { s1});
                level = Level.ERROR;
            }

            String s4 = s2.equals("root") ? "" : s2;
            boolean flag = Booleans.parseBoolean(s, true);

            return new AsyncLoggerConfig(s4, list, filter, level, flag, aproperty, configuration, includeLocation(s3));
        }
    }

    protected static boolean includeLocation(String s) {
        return Boolean.parseBoolean(s);
    }

    @Plugin(
        name = "asyncRoot",
        category = "Core",
        printObject = true
    )
    public static class RootLogger extends LoggerConfig {

        @PluginFactory
        public static LoggerConfig createLogger(@PluginAttribute("additivity") String s, @PluginAttribute("level") String s1, @PluginAttribute("includeLocation") String s2, @PluginElement("AppenderRef") AppenderRef[] aappenderref, @PluginElement("Properties") Property[] aproperty, @PluginConfiguration Configuration configuration, @PluginElement("Filters") Filter filter) {
            List list = Arrays.asList(aappenderref);

            Level level;

            try {
                level = Level.toLevel(s1, Level.ERROR);
            } catch (Exception exception) {
                AsyncLoggerConfig.RootLogger.LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", new Object[] { s1});
                level = Level.ERROR;
            }

            boolean flag = Booleans.parseBoolean(s, true);

            return new AsyncLoggerConfig("", list, filter, level, flag, aproperty, configuration, includeLocation(s2));
        }
    }
}
