package org.apache.logging.log4j.core.impl;

import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.jmx.Server;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class Log4jContextFactory implements LoggerContextFactory {

    private static final StatusLogger LOGGER = StatusLogger.getLogger();
    private ContextSelector selector;

    public Log4jContextFactory() {
        String s = PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector");

        if (s != null) {
            try {
                Class oclass = Loader.loadClass(s);

                if (oclass != null && ContextSelector.class.isAssignableFrom(oclass)) {
                    this.selector = (ContextSelector) oclass.newInstance();
                }
            } catch (Exception exception) {
                Log4jContextFactory.LOGGER.error("Unable to create context " + s, (Throwable) exception);
            }
        }

        if (this.selector == null) {
            this.selector = new ClassLoaderContextSelector();
        }

        try {
            Server.registerMBeans(this.selector);
        } catch (Exception exception1) {
            Log4jContextFactory.LOGGER.error("Could not start JMX", (Throwable) exception1);
        }

    }

    public ContextSelector getSelector() {
        return this.selector;
    }

    public LoggerContext getContext(String s, ClassLoader classloader, boolean flag) {
        LoggerContext loggercontext = this.selector.getContext(s, classloader, flag);

        if (loggercontext.getStatus() == LoggerContext.Status.INITIALIZED) {
            loggercontext.start();
        }

        return loggercontext;
    }

    public LoggerContext getContext(String s, ClassLoader classloader, boolean flag, URI uri) {
        LoggerContext loggercontext = this.selector.getContext(s, classloader, flag, uri);

        if (loggercontext.getStatus() == LoggerContext.Status.INITIALIZED) {
            loggercontext.start();
        }

        return loggercontext;
    }

    public void removeContext(org.apache.logging.log4j.spi.LoggerContext org_apache_logging_log4j_spi_loggercontext) {
        if (org_apache_logging_log4j_spi_loggercontext instanceof LoggerContext) {
            this.selector.removeContext((LoggerContext) org_apache_logging_log4j_spi_loggercontext);
        }

    }
}
