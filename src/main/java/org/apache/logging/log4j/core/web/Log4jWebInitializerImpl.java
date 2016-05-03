package org.apache.logging.log4j.core.web;

import java.net.URI;
import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.core.selector.NamedContextSelector;
import org.apache.logging.log4j.spi.LoggerContextFactory;

final class Log4jWebInitializerImpl implements Log4jWebInitializer {

    private static final Object MUTEX = new Object();
    private final StrSubstitutor substitutor = new StrSubstitutor(new Interpolator());
    private final ServletContext servletContext;
    private String name;
    private NamedContextSelector selector;
    private LoggerContext loggerContext;
    private boolean initialized = false;
    private boolean deinitialized = false;

    private Log4jWebInitializerImpl(ServletContext servletcontext) {
        this.servletContext = servletcontext;
    }

    public synchronized void initialize() throws UnavailableException {
        if (this.deinitialized) {
            throw new IllegalStateException("Cannot initialize Log4jWebInitializer after it was destroyed.");
        } else {
            if (!this.initialized) {
                this.initialized = true;
                this.name = this.substitutor.replace(this.servletContext.getInitParameter("log4jContextName"));
                String s = this.substitutor.replace(this.servletContext.getInitParameter("log4jConfiguration"));
                boolean flag = "true".equals(this.servletContext.getInitParameter("isLog4jContextSelectorNamed"));

                if (flag) {
                    this.initializeJndi(s);
                } else {
                    this.initializeNonJndi(s);
                }
            }

        }
    }

    private void initializeJndi(String s) throws UnavailableException {
        URI uri = null;

        if (s != null) {
            try {
                uri = new URI(s);
            } catch (Exception exception) {
                this.servletContext.log("Unable to convert configuration location [" + s + "] to a URI!", exception);
            }
        }

        if (this.name == null) {
            throw new UnavailableException("A log4jContextName context parameter is required");
        } else {
            LoggerContextFactory loggercontextfactory = LogManager.getFactory();

            if (loggercontextfactory instanceof Log4jContextFactory) {
                ContextSelector contextselector = ((Log4jContextFactory) loggercontextfactory).getSelector();

                if (contextselector instanceof NamedContextSelector) {
                    this.selector = (NamedContextSelector) contextselector;
                    LoggerContext loggercontext = this.selector.locateContext(this.name, this.servletContext, uri);

                    ContextAnchor.THREAD_CONTEXT.set(loggercontext);
                    if (loggercontext.getStatus() == LoggerContext.Status.INITIALIZED) {
                        loggercontext.start();
                    }

                    ContextAnchor.THREAD_CONTEXT.remove();
                    this.loggerContext = loggercontext;
                    this.servletContext.log("Created logger context for [" + this.name + "] using [" + loggercontext.getClass().getClassLoader() + "].");
                } else {
                    this.servletContext.log("Potential problem: Selector is not an instance of NamedContextSelector.");
                }
            } else {
                this.servletContext.log("Potential problem: Factory is not an instance of Log4jContextFactory.");
            }
        }
    }

    private void initializeNonJndi(String s) {
        if (this.name == null) {
            this.name = this.servletContext.getServletContextName();
        }

        if (this.name == null && s == null) {
            this.servletContext.log("No Log4j context configuration provided. This is very unusual.");
        } else {
            this.loggerContext = Configurator.initialize(this.name, this.getClassLoader(), s, this.servletContext);
        }
    }

    public synchronized void deinitialize() {
        if (!this.initialized) {
            throw new IllegalStateException("Cannot deinitialize Log4jWebInitializer because it has not initialized.");
        } else {
            if (!this.deinitialized) {
                this.deinitialized = true;
                if (this.loggerContext != null) {
                    this.servletContext.log("Removing LoggerContext for [" + this.name + "].");
                    if (this.selector != null) {
                        this.selector.removeContext(this.name);
                    }

                    this.loggerContext.stop();
                    this.loggerContext.setExternalContext((Object) null);
                    this.loggerContext = null;
                }
            }

        }
    }

    public void setLoggerContext() {
        if (this.loggerContext != null) {
            ContextAnchor.THREAD_CONTEXT.set(this.loggerContext);
        }

    }

    public void clearLoggerContext() {
        ContextAnchor.THREAD_CONTEXT.remove();
    }

    private ClassLoader getClassLoader() {
        try {
            return this.servletContext.getClassLoader();
        } catch (Throwable throwable) {
            return Log4jWebInitializerImpl.class.getClassLoader();
        }
    }

    static Log4jWebInitializer getLog4jWebInitializer(ServletContext servletcontext) {
        Object object = Log4jWebInitializerImpl.MUTEX;

        synchronized (Log4jWebInitializerImpl.MUTEX) {
            Object object1 = (Log4jWebInitializer) servletcontext.getAttribute(Log4jWebInitializerImpl.INITIALIZER_ATTRIBUTE);

            if (object1 == null) {
                object1 = new Log4jWebInitializerImpl(servletcontext);
                servletcontext.setAttribute(Log4jWebInitializerImpl.INITIALIZER_ATTRIBUTE, object1);
            }

            return (Log4jWebInitializer) object1;
        }
    }

    static {
        try {
            Class.forName("org.apache.logging.log4j.core.web.JNDIContextFilter");
            throw new IllegalStateException("You are using Log4j 2 in a web application with the old, extinct log4j-web artifact. This is not supported and could cause serious runtime problems. Pleaseremove the log4j-web JAR file from your application.");
        } catch (ClassNotFoundException classnotfoundexception) {
            ;
        }
    }
}
