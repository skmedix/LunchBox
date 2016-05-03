package org.apache.logging.log4j.core.config;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.status.StatusLogger;

public final class Configurator {

    protected static final StatusLogger LOGGER = StatusLogger.getLogger();

    public static LoggerContext initialize(String s, ClassLoader classloader, String s1) {
        return initialize(s, classloader, s1, (Object) null);
    }

    public static LoggerContext initialize(String s, ClassLoader classloader, String s1, Object object) {
        try {
            URI uri = s1 == null ? null : new URI(s1);

            return initialize(s, classloader, uri, object);
        } catch (URISyntaxException urisyntaxexception) {
            urisyntaxexception.printStackTrace();
            return null;
        }
    }

    public static LoggerContext initialize(String s, String s1) {
        return initialize(s, (ClassLoader) null, s1);
    }

    public static LoggerContext initialize(String s, ClassLoader classloader, URI uri) {
        return initialize(s, classloader, uri, (Object) null);
    }

    public static LoggerContext initialize(String s, ClassLoader classloader, URI uri, Object object) {
        try {
            org.apache.logging.log4j.spi.LoggerContext org_apache_logging_log4j_spi_loggercontext = LogManager.getContext(classloader, false, uri);

            if (org_apache_logging_log4j_spi_loggercontext instanceof LoggerContext) {
                LoggerContext loggercontext = (LoggerContext) org_apache_logging_log4j_spi_loggercontext;

                ContextAnchor.THREAD_CONTEXT.set(loggercontext);
                if (object != null) {
                    loggercontext.setExternalContext(object);
                }

                Configuration configuration = ConfigurationFactory.getInstance().getConfiguration(s, uri);

                loggercontext.start(configuration);
                ContextAnchor.THREAD_CONTEXT.remove();
                return loggercontext;
            }

            Configurator.LOGGER.error("LogManager returned an instance of {} which does not implement {}. Unable to initialize Log4j", new Object[] { org_apache_logging_log4j_spi_loggercontext.getClass().getName(), LoggerContext.class.getName()});
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static LoggerContext initialize(ClassLoader classloader, ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource) {
        try {
            URI uri = null;

            try {
                uri = configurationfactory_configurationsource.getLocation() == null ? null : new URI(configurationfactory_configurationsource.getLocation());
            } catch (Exception exception) {
                ;
            }

            org.apache.logging.log4j.spi.LoggerContext org_apache_logging_log4j_spi_loggercontext = LogManager.getContext(classloader, false, uri);

            if (org_apache_logging_log4j_spi_loggercontext instanceof LoggerContext) {
                LoggerContext loggercontext = (LoggerContext) org_apache_logging_log4j_spi_loggercontext;

                ContextAnchor.THREAD_CONTEXT.set(loggercontext);
                Configuration configuration = ConfigurationFactory.getInstance().getConfiguration(configurationfactory_configurationsource);

                loggercontext.start(configuration);
                ContextAnchor.THREAD_CONTEXT.remove();
                return loggercontext;
            }

            Configurator.LOGGER.error("LogManager returned an instance of {} which does not implement {}. Unable to initialize Log4j", new Object[] { org_apache_logging_log4j_spi_loggercontext.getClass().getName(), LoggerContext.class.getName()});
        } catch (Exception exception1) {
            exception1.printStackTrace();
        }

        return null;
    }

    public static void shutdown(LoggerContext loggercontext) {
        if (loggercontext != null) {
            loggercontext.stop();
        }

    }
}
