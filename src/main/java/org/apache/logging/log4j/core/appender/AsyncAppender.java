package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;

@Plugin(
    name = "Async",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class AsyncAppender extends AbstractAppender {

    private static final int DEFAULT_QUEUE_SIZE = 128;
    private static final String SHUTDOWN = "Shutdown";
    private final BlockingQueue queue;
    private final boolean blocking;
    private final Configuration config;
    private final AppenderRef[] appenderRefs;
    private final String errorRef;
    private final boolean includeLocation;
    private AppenderControl errorAppender;
    private AsyncAppender.AsyncThread thread;
    private static final AtomicLong threadSequence = new AtomicLong(1L);

    private AsyncAppender(String s, Filter filter, AppenderRef[] aappenderref, String s1, int i, boolean flag, boolean flag1, Configuration configuration, boolean flag2) {
        super(s, filter, (Layout) null, flag1);
        this.queue = new ArrayBlockingQueue(i);
        this.blocking = flag;
        this.config = configuration;
        this.appenderRefs = aappenderref;
        this.errorRef = s1;
        this.includeLocation = flag2;
    }

    public void start() {
        Map map = this.config.getAppenders();
        ArrayList arraylist = new ArrayList();
        AppenderRef[] aappenderref = this.appenderRefs;
        int i = aappenderref.length;

        for (int j = 0; j < i; ++j) {
            AppenderRef appenderref = aappenderref[j];

            if (map.containsKey(appenderref.getRef())) {
                arraylist.add(new AppenderControl((Appender) map.get(appenderref.getRef()), appenderref.getLevel(), appenderref.getFilter()));
            } else {
                AsyncAppender.LOGGER.error("No appender named {} was configured", new Object[] { appenderref});
            }
        }

        if (this.errorRef != null) {
            if (map.containsKey(this.errorRef)) {
                this.errorAppender = new AppenderControl((Appender) map.get(this.errorRef), (Level) null, (Filter) null);
            } else {
                AsyncAppender.LOGGER.error("Unable to set up error Appender. No appender named {} was configured", new Object[] { this.errorRef});
            }
        }

        if (arraylist.size() > 0) {
            this.thread = new AsyncAppender.AsyncThread(arraylist, this.queue);
            this.thread.setName("AsyncAppender-" + this.getName());
        } else if (this.errorRef == null) {
            throw new ConfigurationException("No appenders are available for AsyncAppender " + this.getName());
        }

        this.thread.start();
        super.start();
    }

    public void stop() {
        super.stop();
        this.thread.shutdown();

        try {
            this.thread.join();
        } catch (InterruptedException interruptedexception) {
            AsyncAppender.LOGGER.warn("Interrupted while stopping AsyncAppender {}", new Object[] { this.getName()});
        }

    }

    public void append(LogEvent logevent) {
        if (!this.isStarted()) {
            throw new IllegalStateException("AsyncAppender " + this.getName() + " is not active");
        } else {
            if (logevent instanceof Log4jLogEvent) {
                boolean flag = false;

                if (this.blocking) {
                    try {
                        this.queue.put(Log4jLogEvent.serialize((Log4jLogEvent) logevent, this.includeLocation));
                        flag = true;
                    } catch (InterruptedException interruptedexception) {
                        AsyncAppender.LOGGER.warn("Interrupted while waiting for a free slot in the AsyncAppender LogEvent-queue {}", new Object[] { this.getName()});
                    }
                } else {
                    flag = this.queue.offer(Log4jLogEvent.serialize((Log4jLogEvent) logevent, this.includeLocation));
                    if (!flag) {
                        this.error("Appender " + this.getName() + " is unable to write primary appenders. queue is full");
                    }
                }

                if (!flag && this.errorAppender != null) {
                    this.errorAppender.callAppender(logevent);
                }
            }

        }
    }

    @PluginFactory
    public static AsyncAppender createAppender(@PluginElement("AppenderRef") AppenderRef[] aappenderref, @PluginAttribute("errorRef") @PluginAliases({ "error-ref"}) String s, @PluginAttribute("blocking") String s1, @PluginAttribute("bufferSize") String s2, @PluginAttribute("name") String s3, @PluginAttribute("includeLocation") String s4, @PluginElement("Filter") Filter filter, @PluginConfiguration Configuration configuration, @PluginAttribute("ignoreExceptions") String s5) {
        if (s3 == null) {
            AsyncAppender.LOGGER.error("No name provided for AsyncAppender");
            return null;
        } else {
            if (aappenderref == null) {
                AsyncAppender.LOGGER.error("No appender references provided to AsyncAppender {}", new Object[] { s3});
            }

            boolean flag = Booleans.parseBoolean(s1, true);
            int i = AbstractAppender.parseInt(s2, 128);
            boolean flag1 = Boolean.parseBoolean(s4);
            boolean flag2 = Booleans.parseBoolean(s5, true);

            return new AsyncAppender(s3, filter, aappenderref, s, i, flag, flag2, configuration, flag1);
        }
    }

    private class AsyncThread extends Thread {

        private volatile boolean shutdown = false;
        private final List appenders;
        private final BlockingQueue queue;

        public AsyncThread(List list, BlockingQueue blockingqueue) {
            this.appenders = list;
            this.queue = blockingqueue;
            this.setDaemon(true);
            this.setName("AsyncAppenderThread" + AsyncAppender.threadSequence.getAndIncrement());
        }

        public void run() {
            Serializable serializable;
            Log4jLogEvent log4jlogevent;

            while (!this.shutdown) {
                try {
                    serializable = (Serializable) this.queue.take();
                    if (serializable != null && serializable instanceof String && "Shutdown".equals(serializable.toString())) {
                        this.shutdown = true;
                        continue;
                    }
                } catch (InterruptedException interruptedexception) {
                    continue;
                }

                log4jlogevent = Log4jLogEvent.deserialize(serializable);
                log4jlogevent.setEndOfBatch(this.queue.isEmpty());
                boolean flag = false;
                Iterator iterator = this.appenders.iterator();

                while (iterator.hasNext()) {
                    AppenderControl appendercontrol = (AppenderControl) iterator.next();

                    try {
                        appendercontrol.callAppender(log4jlogevent);
                        flag = true;
                    } catch (Exception exception) {
                        ;
                    }
                }

                if (!flag && AsyncAppender.this.errorAppender != null) {
                    try {
                        AsyncAppender.this.errorAppender.callAppender(log4jlogevent);
                    } catch (Exception exception1) {
                        ;
                    }
                }
            }

            while (!this.queue.isEmpty()) {
                try {
                    serializable = (Serializable) this.queue.take();
                    if (serializable instanceof Log4jLogEvent) {
                        log4jlogevent = Log4jLogEvent.deserialize(serializable);
                        log4jlogevent.setEndOfBatch(this.queue.isEmpty());
                        Iterator iterator1 = this.appenders.iterator();

                        while (iterator1.hasNext()) {
                            AppenderControl appendercontrol1 = (AppenderControl) iterator1.next();

                            appendercontrol1.callAppender(log4jlogevent);
                        }
                    }
                } catch (InterruptedException interruptedexception1) {
                    ;
                }
            }

        }

        public void shutdown() {
            this.shutdown = true;
            if (this.queue.isEmpty()) {
                this.queue.offer("Shutdown");
            }

        }
    }
}
