package org.apache.logging.log4j.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

@Plugin(
    name = "logger",
    category = "Core",
    printObject = true
)
public class LoggerConfig extends AbstractFilterable {

    protected static final Logger LOGGER = StatusLogger.getLogger();
    private static final int MAX_RETRIES = 3;
    private static final long WAIT_TIME = 1000L;
    private static LogEventFactory LOG_EVENT_FACTORY = null;
    private List appenderRefs = new ArrayList();
    private final Map appenders = new ConcurrentHashMap();
    private final String name;
    private LogEventFactory logEventFactory;
    private Level level;
    private boolean additive = true;
    private boolean includeLocation = true;
    private LoggerConfig parent;
    private final AtomicInteger counter = new AtomicInteger();
    private boolean shutdown = false;
    private final Map properties;
    private final Configuration config;

    public LoggerConfig() {
        this.logEventFactory = LoggerConfig.LOG_EVENT_FACTORY;
        this.level = Level.ERROR;
        this.name = "";
        this.properties = null;
        this.config = null;
    }

    public LoggerConfig(String s, Level level, boolean flag) {
        this.logEventFactory = LoggerConfig.LOG_EVENT_FACTORY;
        this.name = s;
        this.level = level;
        this.additive = flag;
        this.properties = null;
        this.config = null;
    }

    protected LoggerConfig(String s, List list, Filter filter, Level level, boolean flag, Property[] aproperty, Configuration configuration, boolean flag1) {
        super(filter);
        this.logEventFactory = LoggerConfig.LOG_EVENT_FACTORY;
        this.name = s;
        this.appenderRefs = list;
        this.level = level;
        this.additive = flag;
        this.includeLocation = flag1;
        this.config = configuration;
        if (aproperty != null && aproperty.length > 0) {
            this.properties = new HashMap(aproperty.length);
            Property[] aproperty1 = aproperty;
            int i = aproperty.length;

            for (int j = 0; j < i; ++j) {
                Property property = aproperty1[j];
                boolean flag2 = property.getValue().contains("${");

                this.properties.put(property, Boolean.valueOf(flag2));
            }
        } else {
            this.properties = null;
        }

    }

    public Filter getFilter() {
        return super.getFilter();
    }

    public String getName() {
        return this.name;
    }

    public void setParent(LoggerConfig loggerconfig) {
        this.parent = loggerconfig;
    }

    public LoggerConfig getParent() {
        return this.parent;
    }

    public void addAppender(Appender appender, Level level, Filter filter) {
        this.appenders.put(appender.getName(), new AppenderControl(appender, level, filter));
    }

    public void removeAppender(String s) {
        AppenderControl appendercontrol = (AppenderControl) this.appenders.remove(s);

        if (appendercontrol != null) {
            this.cleanupFilter(appendercontrol);
        }

    }

    public Map getAppenders() {
        HashMap hashmap = new HashMap();
        Iterator iterator = this.appenders.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            hashmap.put(entry.getKey(), ((AppenderControl) entry.getValue()).getAppender());
        }

        return hashmap;
    }

    protected void clearAppenders() {
        this.waitForCompletion();
        Collection collection = this.appenders.values();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            AppenderControl appendercontrol = (AppenderControl) iterator.next();

            iterator.remove();
            this.cleanupFilter(appendercontrol);
        }

    }

    private void cleanupFilter(AppenderControl appendercontrol) {
        Filter filter = appendercontrol.getFilter();

        if (filter != null) {
            appendercontrol.removeFilter(filter);
            if (filter instanceof LifeCycle) {
                ((LifeCycle) filter).stop();
            }
        }

    }

    public List getAppenderRefs() {
        return this.appenderRefs;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return this.level;
    }

    public LogEventFactory getLogEventFactory() {
        return this.logEventFactory;
    }

    public void setLogEventFactory(LogEventFactory logeventfactory) {
        this.logEventFactory = logeventfactory;
    }

    public boolean isAdditive() {
        return this.additive;
    }

    public void setAdditive(boolean flag) {
        this.additive = flag;
    }

    public boolean isIncludeLocation() {
        return this.includeLocation;
    }

    public Map getProperties() {
        return this.properties == null ? null : Collections.unmodifiableMap(this.properties);
    }

    public void log(String s, Marker marker, String s1, Level level, Message message, Throwable throwable) {
        ArrayList arraylist = null;

        if (this.properties != null) {
            arraylist = new ArrayList(this.properties.size());
            Iterator iterator = this.properties.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Property property = (Property) entry.getKey();
                String s2 = ((Boolean) entry.getValue()).booleanValue() ? this.config.getStrSubstitutor().replace(property.getValue()) : property.getValue();

                arraylist.add(Property.createProperty(property.getName(), s2));
            }
        }

        LogEvent logevent = this.logEventFactory.createEvent(s, marker, s1, level, message, arraylist, throwable);

        this.log(logevent);
    }

    private synchronized void waitForCompletion() {
        if (!this.shutdown) {
            this.shutdown = true;
            int i = 0;

            while (this.counter.get() > 0) {
                try {
                    this.wait(1000L * (long) (i + 1));
                } catch (InterruptedException interruptedexception) {
                    ++i;
                    if (i > 3) {
                        break;
                    }
                }
            }

        }
    }

    public void log(LogEvent logevent) {
        this.counter.incrementAndGet();
        boolean flag = false;

        label148: {
            try {
                flag = true;
                if (!this.isFiltered(logevent)) {
                    logevent.setIncludeLocation(this.isIncludeLocation());
                    this.callAppenders(logevent);
                    if (this.additive) {
                        if (this.parent != null) {
                            this.parent.log(logevent);
                            flag = false;
                        } else {
                            flag = false;
                        }
                    } else {
                        flag = false;
                    }
                    break label148;
                }

                flag = false;
            } finally {
                if(flag) {
                    if (this.counter.decrementAndGet() == 0) {
                        synchronized (this) {
                            if (this.shutdown) {
                                this.notifyAll();
                            }
                        }
                    }

                }
            }

            if (this.counter.decrementAndGet() == 0) {
                synchronized (this) {
                    if (this.shutdown) {
                        this.notifyAll();
                    }
                }
            }

            return;
        }

        if (this.counter.decrementAndGet() == 0) {
            synchronized (this) {
                if (this.shutdown) {
                    this.notifyAll();
                }
            }
        }

    }

    protected void callAppenders(LogEvent logevent) {
        Iterator iterator = this.appenders.values().iterator();

        while (iterator.hasNext()) {
            AppenderControl appendercontrol = (AppenderControl) iterator.next();

            appendercontrol.callAppender(logevent);
        }

    }

    public String toString() {
        return Strings.isEmpty(this.name) ? "root" : this.name;
    }

    @PluginFactory
    public static LoggerConfig createLogger(@PluginAttribute("additivity") String s, @PluginAttribute("level") String s1, @PluginAttribute("name") String s2, @PluginAttribute("includeLocation") String s3, @PluginElement("AppenderRef") AppenderRef[] aappenderref, @PluginElement("Properties") Property[] aproperty, @PluginConfiguration Configuration configuration, @PluginElement("Filters") Filter filter) {
        if (s2 == null) {
            LoggerConfig.LOGGER.error("Loggers cannot be configured without a name");
            return null;
        } else {
            List list = Arrays.asList(aappenderref);

            Level level;

            try {
                level = Level.toLevel(s1, Level.ERROR);
            } catch (Exception exception) {
                LoggerConfig.LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", new Object[] { s1});
                level = Level.ERROR;
            }

            String s4 = s2.equals("root") ? "" : s2;
            boolean flag = Booleans.parseBoolean(s, true);

            return new LoggerConfig(s4, list, filter, level, flag, aproperty, configuration, includeLocation(s3));
        }
    }

    protected static boolean includeLocation(String s) {
        if (s == null) {
            boolean flag = !AsyncLoggerContextSelector.class.getName().equals(System.getProperty("Log4jContextSelector"));

            return flag;
        } else {
            return Boolean.parseBoolean(s);
        }
    }

    static {
        String s = PropertiesUtil.getProperties().getStringProperty("Log4jLogEventFactory");

        if (s != null) {
            try {
                Class oclass = Loader.loadClass(s);

                if (oclass != null && LogEventFactory.class.isAssignableFrom(oclass)) {
                    LoggerConfig.LOG_EVENT_FACTORY = (LogEventFactory) oclass.newInstance();
                }
            } catch (Exception exception) {
                LoggerConfig.LOGGER.error("Unable to create LogEventFactory " + s, (Throwable) exception);
            }
        }

        if (LoggerConfig.LOG_EVENT_FACTORY == null) {
            LoggerConfig.LOG_EVENT_FACTORY = new DefaultLogEventFactory();
        }

    }

    @Plugin(
        name = "root",
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
                LoggerConfig.RootLogger.LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", new Object[] { s1});
                level = Level.ERROR;
            }

            boolean flag = Booleans.parseBoolean(s, true);

            return new LoggerConfig("", list, filter, level, flag, aproperty, configuration, includeLocation(s2));
        }
    }
}
