package org.apache.logging.log4j.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.NullConfiguration;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.helpers.Assert;
import org.apache.logging.log4j.core.helpers.NetUtils;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.status.StatusLogger;

public class LoggerContext implements org.apache.logging.log4j.spi.LoggerContext, ConfigurationListener, LifeCycle {

    public static final String PROPERTY_CONFIG = "config";
    private static final StatusLogger LOGGER = StatusLogger.getLogger();
    private final ConcurrentMap loggers;
    private final CopyOnWriteArrayList propertyChangeListeners;
    private volatile Configuration config;
    private Object externalContext;
    private final String name;
    private URI configLocation;
    private LoggerContext.ShutdownThread shutdownThread;
    private volatile LoggerContext.Status status;
    private final Lock configLock;

    public LoggerContext(String s) {
        this(s, (Object) null, (URI) null);
    }

    public LoggerContext(String s, Object object) {
        this(s, object, (URI) null);
    }

    public LoggerContext(String s, Object object, URI uri) {
        this.loggers = new ConcurrentHashMap();
        this.propertyChangeListeners = new CopyOnWriteArrayList();
        this.config = new DefaultConfiguration();
        this.shutdownThread = null;
        this.status = LoggerContext.Status.INITIALIZED;
        this.configLock = new ReentrantLock();
        this.name = s;
        this.externalContext = object;
        this.configLocation = uri;
    }

    public LoggerContext(String s, Object object, String s1) {
        this.loggers = new ConcurrentHashMap();
        this.propertyChangeListeners = new CopyOnWriteArrayList();
        this.config = new DefaultConfiguration();
        this.shutdownThread = null;
        this.status = LoggerContext.Status.INITIALIZED;
        this.configLock = new ReentrantLock();
        this.name = s;
        this.externalContext = object;
        if (s1 != null) {
            URI uri;

            try {
                uri = (new File(s1)).toURI();
            } catch (Exception exception) {
                uri = null;
            }

            this.configLocation = uri;
        } else {
            this.configLocation = null;
        }

    }

    public void start() {
        if (this.configLock.tryLock()) {
            try {
                if (this.status == LoggerContext.Status.INITIALIZED || this.status == LoggerContext.Status.STOPPED) {
                    this.status = LoggerContext.Status.STARTING;
                    this.reconfigure();
                    if (this.config.isShutdownHookEnabled()) {
                        this.shutdownThread = new LoggerContext.ShutdownThread(this);

                        try {
                            Runtime.getRuntime().addShutdownHook(this.shutdownThread);
                        } catch (IllegalStateException illegalstateexception) {
                            LoggerContext.LOGGER.warn("Unable to register shutdown hook due to JVM state");
                            this.shutdownThread = null;
                        } catch (SecurityException securityexception) {
                            LoggerContext.LOGGER.warn("Unable to register shutdown hook due to security restrictions");
                            this.shutdownThread = null;
                        }
                    }

                    this.status = LoggerContext.Status.STARTED;
                }
            } finally {
                this.configLock.unlock();
            }
        }

    }

    public void start(Configuration configuration) {
        if (this.configLock.tryLock()) {
            try {
                if ((this.status == LoggerContext.Status.INITIALIZED || this.status == LoggerContext.Status.STOPPED) && configuration.isShutdownHookEnabled()) {
                    this.shutdownThread = new LoggerContext.ShutdownThread(this);

                    try {
                        Runtime.getRuntime().addShutdownHook(this.shutdownThread);
                    } catch (IllegalStateException illegalstateexception) {
                        LoggerContext.LOGGER.warn("Unable to register shutdown hook due to JVM state");
                        this.shutdownThread = null;
                    } catch (SecurityException securityexception) {
                        LoggerContext.LOGGER.warn("Unable to register shutdown hook due to security restrictions");
                        this.shutdownThread = null;
                    }

                    this.status = LoggerContext.Status.STARTED;
                }
            } finally {
                this.configLock.unlock();
            }
        }

        this.setConfiguration(configuration);
    }

    public void stop() {
        this.configLock.lock();

        try {
            if (this.status != LoggerContext.Status.STOPPED) {
                this.status = LoggerContext.Status.STOPPING;
                if (this.shutdownThread != null) {
                    Runtime.getRuntime().removeShutdownHook(this.shutdownThread);
                    this.shutdownThread = null;
                }

                Configuration configuration = this.config;

                this.config = new NullConfiguration();
                this.updateLoggers();
                configuration.stop();
                this.externalContext = null;
                LogManager.getFactory().removeContext(this);
                this.status = LoggerContext.Status.STOPPED;
                return;
            }
        } finally {
            this.configLock.unlock();
        }

    }

    public String getName() {
        return this.name;
    }

    public LoggerContext.Status getStatus() {
        return this.status;
    }

    public boolean isStarted() {
        return this.status == LoggerContext.Status.STARTED;
    }

    public void setExternalContext(Object object) {
        this.externalContext = object;
    }

    public Object getExternalContext() {
        return this.externalContext;
    }

    public Logger getLogger(String s) {
        return this.getLogger(s, (MessageFactory) null);
    }

    public Logger getLogger(String s, MessageFactory messagefactory) {
        Logger logger = (Logger) this.loggers.get(s);

        if (logger != null) {
            AbstractLogger.checkMessageFactory(logger, messagefactory);
            return logger;
        } else {
            logger = this.newInstance(this, s, messagefactory);
            Logger logger1 = (Logger) this.loggers.putIfAbsent(s, logger);

            return logger1 == null ? logger : logger1;
        }
    }

    public boolean hasLogger(String s) {
        return this.loggers.containsKey(s);
    }

    public Configuration getConfiguration() {
        return this.config;
    }

    public void addFilter(Filter filter) {
        this.config.addFilter(filter);
    }

    public void removeFilter(Filter filter) {
        this.config.removeFilter(filter);
    }

    private synchronized Configuration setConfiguration(Configuration configuration) {
        if (configuration == null) {
            throw new NullPointerException("No Configuration was provided");
        } else {
            Configuration configuration1 = this.config;

            configuration.addListener(this);
            HashMap hashmap = new HashMap();

            hashmap.put("hostName", NetUtils.getLocalHostname());
            hashmap.put("contextName", this.name);
            configuration.addComponent("ContextProperties", hashmap);
            configuration.start();
            this.config = configuration;
            this.updateLoggers();
            if (configuration1 != null) {
                configuration1.removeListener(this);
                configuration1.stop();
            }

            PropertyChangeEvent propertychangeevent = new PropertyChangeEvent(this, "config", configuration1, configuration);
            Iterator iterator = this.propertyChangeListeners.iterator();

            while (iterator.hasNext()) {
                PropertyChangeListener propertychangelistener = (PropertyChangeListener) iterator.next();

                propertychangelistener.propertyChange(propertychangeevent);
            }

            return configuration1;
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener propertychangelistener) {
        this.propertyChangeListeners.add(Assert.isNotNull(propertychangelistener, "listener"));
    }

    public void removePropertyChangeListener(PropertyChangeListener propertychangelistener) {
        this.propertyChangeListeners.remove(propertychangelistener);
    }

    public synchronized URI getConfigLocation() {
        return this.configLocation;
    }

    public synchronized void setConfigLocation(URI uri) {
        this.configLocation = uri;
        this.reconfigure();
    }

    public synchronized void reconfigure() {
        LoggerContext.LOGGER.debug("Reconfiguration started for context " + this.name);
        Configuration configuration = ConfigurationFactory.getInstance().getConfiguration(this.name, this.configLocation);

        this.setConfiguration(configuration);
        LoggerContext.LOGGER.debug("Reconfiguration completed");
    }

    public void updateLoggers() {
        this.updateLoggers(this.config);
    }

    public void updateLoggers(Configuration configuration) {
        Iterator iterator = this.loggers.values().iterator();

        while (iterator.hasNext()) {
            Logger logger = (Logger) iterator.next();

            logger.updateConfiguration(configuration);
        }

    }

    public synchronized void onChange(Reconfigurable reconfigurable) {
        LoggerContext.LOGGER.debug("Reconfiguration started for context " + this.name);
        Configuration configuration = reconfigurable.reconfigure();

        if (configuration != null) {
            this.setConfiguration(configuration);
            LoggerContext.LOGGER.debug("Reconfiguration completed");
        } else {
            LoggerContext.LOGGER.debug("Reconfiguration failed");
        }

    }

    protected Logger newInstance(LoggerContext loggercontext, String s, MessageFactory messagefactory) {
        return new Logger(loggercontext, s, messagefactory);
    }

    private class ShutdownThread extends Thread {

        private final LoggerContext context;

        public ShutdownThread(LoggerContext loggercontext) {
            this.context = loggercontext;
        }

        public void run() {
            this.context.shutdownThread = null;
            this.context.stop();
        }
    }

    public static enum Status {

        INITIALIZED, STARTING, STARTED, STOPPING, STOPPED;
    }
}
