package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.status.StatusLogger;

public class JNDIContextSelector implements NamedContextSelector {

    private static final LoggerContext CONTEXT = new LoggerContext("Default");
    private static final ConcurrentMap CONTEXT_MAP = new ConcurrentHashMap();
    private static final StatusLogger LOGGER = StatusLogger.getLogger();

    public LoggerContext getContext(String s, ClassLoader classloader, boolean flag) {
        return this.getContext(s, classloader, flag, (URI) null);
    }

    public LoggerContext getContext(String s, ClassLoader classloader, boolean flag, URI uri) {
        LoggerContext loggercontext = (LoggerContext) ContextAnchor.THREAD_CONTEXT.get();

        if (loggercontext != null) {
            return loggercontext;
        } else {
            String s1 = null;

            try {
                InitialContext initialcontext = new InitialContext();

                s1 = (String) lookup(initialcontext, "java:comp/env/log4j/context-name");
            } catch (NamingException namingexception) {
                JNDIContextSelector.LOGGER.error("Unable to lookup java:comp/env/log4j/context-name", (Throwable) namingexception);
            }

            return s1 == null ? JNDIContextSelector.CONTEXT : this.locateContext(s1, (Object) null, uri);
        }
    }

    public LoggerContext locateContext(String s, Object object, URI uri) {
        if (s == null) {
            JNDIContextSelector.LOGGER.error("A context name is required to locate a LoggerContext");
            return null;
        } else {
            if (!JNDIContextSelector.CONTEXT_MAP.containsKey(s)) {
                LoggerContext loggercontext = new LoggerContext(s, object, uri);

                JNDIContextSelector.CONTEXT_MAP.putIfAbsent(s, loggercontext);
            }

            return (LoggerContext) JNDIContextSelector.CONTEXT_MAP.get(s);
        }
    }

    public void removeContext(LoggerContext loggercontext) {
        Iterator iterator = JNDIContextSelector.CONTEXT_MAP.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((LoggerContext) entry.getValue()).equals(loggercontext)) {
                JNDIContextSelector.CONTEXT_MAP.remove(entry.getKey());
            }
        }

    }

    public LoggerContext removeContext(String s) {
        return (LoggerContext) JNDIContextSelector.CONTEXT_MAP.remove(s);
    }

    public List getLoggerContexts() {
        ArrayList arraylist = new ArrayList(JNDIContextSelector.CONTEXT_MAP.values());

        return Collections.unmodifiableList(arraylist);
    }

    protected static Object lookup(Context context, String s) throws NamingException {
        if (context == null) {
            return null;
        } else {
            try {
                return context.lookup(s);
            } catch (NameNotFoundException namenotfoundexception) {
                JNDIContextSelector.LOGGER.error("Could not find name [" + s + "].");
                throw namenotfoundexception;
            }
        }
    }
}
