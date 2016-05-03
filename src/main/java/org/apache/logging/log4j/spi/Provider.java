package org.apache.logging.log4j.spi;

import java.net.URL;
import java.util.Properties;

public class Provider {

    private static final Integer DEFAULT_PRIORITY = Integer.valueOf(-1);
    private static final String FACTORY_PRIORITY = "FactoryPriority";
    private static final String THREAD_CONTEXT_MAP = "ThreadContextMap";
    private static final String LOGGER_CONTEXT_FACTORY = "LoggerContextFactory";
    private final Integer priority;
    private final String className;
    private final String threadContextMap;
    private final URL url;

    public Provider(Properties properties, URL url) {
        this.url = url;
        String s = properties.getProperty("FactoryPriority");

        this.priority = s == null ? Provider.DEFAULT_PRIORITY : Integer.valueOf(s);
        this.className = properties.getProperty("LoggerContextFactory");
        this.threadContextMap = properties.getProperty("ThreadContextMap");
    }

    public Integer getPriority() {
        return this.priority;
    }

    public String getClassName() {
        return this.className;
    }

    public String getThreadContextMap() {
        return this.threadContextMap;
    }

    public URL getURL() {
        return this.url;
    }
}
