package org.apache.logging.log4j.core.appender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(
    name = "Failover",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class FailoverAppender extends AbstractAppender {

    private static final int DEFAULT_INTERVAL_SECONDS = 60;
    private final String primaryRef;
    private final String[] failovers;
    private final Configuration config;
    private AppenderControl primary;
    private final List failoverAppenders = new ArrayList();
    private final long intervalMillis;
    private long nextCheckMillis = 0L;
    private volatile boolean failure = false;

    private FailoverAppender(String s, Filter filter, String s1, String[] astring, int i, Configuration configuration, boolean flag) {
        super(s, filter, (Layout) null, flag);
        this.primaryRef = s1;
        this.failovers = astring;
        this.config = configuration;
        this.intervalMillis = (long) i;
    }

    public void start() {
        Map map = this.config.getAppenders();
        int i = 0;

        if (map.containsKey(this.primaryRef)) {
            this.primary = new AppenderControl((Appender) map.get(this.primaryRef), (Level) null, (Filter) null);
        } else {
            FailoverAppender.LOGGER.error("Unable to locate primary Appender " + this.primaryRef);
            ++i;
        }

        String[] astring = this.failovers;
        int j = astring.length;

        for (int k = 0; k < j; ++k) {
            String s = astring[k];

            if (map.containsKey(s)) {
                this.failoverAppenders.add(new AppenderControl((Appender) map.get(s), (Level) null, (Filter) null));
            } else {
                FailoverAppender.LOGGER.error("Failover appender " + s + " is not configured");
            }
        }

        if (this.failoverAppenders.size() == 0) {
            FailoverAppender.LOGGER.error("No failover appenders are available");
            ++i;
        }

        if (i == 0) {
            super.start();
        }

    }

    public void append(LogEvent logevent) {
        if (!this.isStarted()) {
            this.error("FailoverAppender " + this.getName() + " did not start successfully");
        } else {
            if (!this.failure) {
                this.callAppender(logevent);
            } else {
                long i = System.currentTimeMillis();

                if (i >= this.nextCheckMillis) {
                    this.callAppender(logevent);
                } else {
                    this.failover(logevent, (Exception) null);
                }
            }

        }
    }

    private void callAppender(LogEvent logevent) {
        try {
            this.primary.callAppender(logevent);
        } catch (Exception exception) {
            this.nextCheckMillis = System.currentTimeMillis() + this.intervalMillis;
            this.failure = true;
            this.failover(logevent, exception);
        }

    }

    private void failover(LogEvent logevent, Exception exception) {
        LoggingException loggingexception = exception != null ? (exception instanceof LoggingException ? (LoggingException) exception : new LoggingException(exception)) : null;
        boolean flag = false;
        Exception exception1 = null;
        Iterator iterator = this.failoverAppenders.iterator();

        while (iterator.hasNext()) {
            AppenderControl appendercontrol = (AppenderControl) iterator.next();

            try {
                appendercontrol.callAppender(logevent);
                flag = true;
                break;
            } catch (Exception exception2) {
                if (exception1 == null) {
                    exception1 = exception2;
                }
            }
        }

        if (!flag && !this.ignoreExceptions()) {
            if (loggingexception != null) {
                throw loggingexception;
            } else {
                throw new LoggingException("Unable to write to failover appenders", exception1);
            }
        }
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder(this.getName());

        stringbuilder.append(" primary=").append(this.primary).append(", failover={");
        boolean flag = true;
        String[] astring = this.failovers;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s = astring[j];

            if (!flag) {
                stringbuilder.append(", ");
            }

            stringbuilder.append(s);
            flag = false;
        }

        stringbuilder.append("}");
        return stringbuilder.toString();
    }

    @PluginFactory
    public static FailoverAppender createAppender(@PluginAttribute("name") String s, @PluginAttribute("primary") String s1, @PluginElement("Failovers") String[] astring, @PluginAttribute("retryInterval") String s2, @PluginConfiguration Configuration configuration, @PluginElement("Filters") Filter filter, @PluginAttribute("ignoreExceptions") String s3) {
        if (s == null) {
            FailoverAppender.LOGGER.error("A name for the Appender must be specified");
            return null;
        } else if (s1 == null) {
            FailoverAppender.LOGGER.error("A primary Appender must be specified");
            return null;
        } else if (astring != null && astring.length != 0) {
            int i = parseInt(s2, 60);
            int j;

            if (i >= 0) {
                j = i * 1000;
            } else {
                FailoverAppender.LOGGER.warn("Interval " + s2 + " is less than zero. Using default");
                j = '\uea60';
            }

            boolean flag = Booleans.parseBoolean(s3, true);

            return new FailoverAppender(s, filter, s1, astring, j, configuration, flag);
        } else {
            FailoverAppender.LOGGER.error("At least one failover Appender must be specified");
            return null;
        }
    }
}
