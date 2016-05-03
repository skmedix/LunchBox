package org.apache.logging.log4j.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.AbstractLogger;

public class Logger extends AbstractLogger {

    protected volatile Logger.PrivateConfig config;
    private final LoggerContext context;

    protected Logger(LoggerContext loggercontext, String s, MessageFactory messagefactory) {
        super(s, messagefactory);
        this.context = loggercontext;
        this.config = new Logger.PrivateConfig(loggercontext.getConfiguration(), this);
    }

    public Logger getParent() {
        LoggerConfig loggerconfig = this.config.loggerConfig.getName().equals(this.getName()) ? this.config.loggerConfig.getParent() : this.config.loggerConfig;

        return loggerconfig == null ? null : (this.context.hasLogger(loggerconfig.getName()) ? this.context.getLogger(loggerconfig.getName(), this.getMessageFactory()) : new Logger(this.context, loggerconfig.getName(), this.getMessageFactory()));
    }

    public LoggerContext getContext() {
        return this.context;
    }

    public synchronized void setLevel(Level level) {
        if (level != null) {
            this.config = new Logger.PrivateConfig(this.config, level);
        }

    }

    public Level getLevel() {
        return this.config.level;
    }

    public void log(Marker marker, String s, Level level, Message message, Throwable throwable) {
        if (message == null) {
            message = new SimpleMessage("");
        }

        this.config.config.getConfigurationMonitor().checkConfiguration();
        this.config.loggerConfig.log(this.getName(), marker, s, level, (Message) message, throwable);
    }

    public boolean isEnabled(Level level, Marker marker, String s) {
        return this.config.filter(level, marker, s);
    }

    public boolean isEnabled(Level level, Marker marker, String s, Throwable throwable) {
        return this.config.filter(level, marker, s, throwable);
    }

    public boolean isEnabled(Level level, Marker marker, String s, Object... aobject) {
        return this.config.filter(level, marker, s, aobject);
    }

    public boolean isEnabled(Level level, Marker marker, Object object, Throwable throwable) {
        return this.config.filter(level, marker, object, throwable);
    }

    public boolean isEnabled(Level level, Marker marker, Message message, Throwable throwable) {
        return this.config.filter(level, marker, message, throwable);
    }

    public void addAppender(Appender appender) {
        this.config.config.addLoggerAppender(this, appender);
    }

    public void removeAppender(Appender appender) {
        this.config.loggerConfig.removeAppender(appender.getName());
    }

    public Map getAppenders() {
        return this.config.loggerConfig.getAppenders();
    }

    public Iterator getFilters() {
        Filter filter = this.config.loggerConfig.getFilter();

        if (filter == null) {
            return (new ArrayList()).iterator();
        } else if (filter instanceof CompositeFilter) {
            return ((CompositeFilter) filter).iterator();
        } else {
            ArrayList arraylist = new ArrayList();

            arraylist.add(filter);
            return arraylist.iterator();
        }
    }

    public int filterCount() {
        Filter filter = this.config.loggerConfig.getFilter();

        return filter == null ? 0 : (filter instanceof CompositeFilter ? ((CompositeFilter) filter).size() : 1);
    }

    public void addFilter(Filter filter) {
        this.config.config.addLoggerFilter(this, filter);
    }

    public boolean isAdditive() {
        return this.config.loggerConfig.isAdditive();
    }

    public void setAdditive(boolean flag) {
        this.config.config.setLoggerAdditive(this, flag);
    }

    void updateConfiguration(Configuration configuration) {
        this.config = new Logger.PrivateConfig(configuration, this);
    }

    public String toString() {
        String s = "" + this.getName() + ":" + this.getLevel();

        if (this.context == null) {
            return s;
        } else {
            String s1 = this.context.getName();

            return s1 == null ? s : s + " in " + s1;
        }
    }

    protected class PrivateConfig {

        public final LoggerConfig loggerConfig;
        public final Configuration config;
        private final Level level;
        private final int intLevel;
        private final Logger logger;

        public PrivateConfig(Configuration configuration, Logger logger) {
            this.config = configuration;
            this.loggerConfig = configuration.getLoggerConfig(Logger.this.getName());
            this.level = this.loggerConfig.getLevel();
            this.intLevel = this.level.intLevel();
            this.logger = logger;
        }

        public PrivateConfig(Logger.PrivateConfig logger_privateconfig, Level level) {
            this.config = logger_privateconfig.config;
            this.loggerConfig = logger_privateconfig.loggerConfig;
            this.level = level;
            this.intLevel = this.level.intLevel();
            this.logger = logger_privateconfig.logger;
        }

        public PrivateConfig(Logger.PrivateConfig logger_privateconfig, LoggerConfig loggerconfig) {
            this.config = logger_privateconfig.config;
            this.loggerConfig = loggerconfig;
            this.level = loggerconfig.getLevel();
            this.intLevel = this.level.intLevel();
            this.logger = logger_privateconfig.logger;
        }

        public void logEvent(LogEvent logevent) {
            this.config.getConfigurationMonitor().checkConfiguration();
            this.loggerConfig.log(logevent);
        }

        boolean filter(Level level, Marker marker, String s) {
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter filter = this.config.getFilter();

            if (filter != null) {
                Filter.Result filter_result = filter.filter(this.logger, level, marker, s, new Object[0]);

                if (filter_result != Filter.Result.NEUTRAL) {
                    return filter_result == Filter.Result.ACCEPT;
                }
            }

            return this.intLevel >= level.intLevel();
        }

        boolean filter(Level level, Marker marker, String s, Throwable throwable) {
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter filter = this.config.getFilter();

            if (filter != null) {
                Filter.Result filter_result = filter.filter(this.logger, level, marker, (Object) s, throwable);

                if (filter_result != Filter.Result.NEUTRAL) {
                    return filter_result == Filter.Result.ACCEPT;
                }
            }

            return this.intLevel >= level.intLevel();
        }

        boolean filter(Level level, Marker marker, String s, Object... aobject) {
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter filter = this.config.getFilter();

            if (filter != null) {
                Filter.Result filter_result = filter.filter(this.logger, level, marker, s, aobject);

                if (filter_result != Filter.Result.NEUTRAL) {
                    return filter_result == Filter.Result.ACCEPT;
                }
            }

            return this.intLevel >= level.intLevel();
        }

        boolean filter(Level level, Marker marker, Object object, Throwable throwable) {
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter filter = this.config.getFilter();

            if (filter != null) {
                Filter.Result filter_result = filter.filter(this.logger, level, marker, object, throwable);

                if (filter_result != Filter.Result.NEUTRAL) {
                    return filter_result == Filter.Result.ACCEPT;
                }
            }

            return this.intLevel >= level.intLevel();
        }

        boolean filter(Level level, Marker marker, Message message, Throwable throwable) {
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter filter = this.config.getFilter();

            if (filter != null) {
                Filter.Result filter_result = filter.filter(this.logger, level, marker, message, throwable);

                if (filter_result != Filter.Result.NEUTRAL) {
                    return filter_result == Filter.Result.ACCEPT;
                }
            }

            return this.intLevel >= level.intLevel();
        }
    }
}
