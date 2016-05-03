package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;

public class BasicContextSelector implements ContextSelector {

    private static final LoggerContext CONTEXT = new LoggerContext("Default");

    public LoggerContext getContext(String s, ClassLoader classloader, boolean flag) {
        LoggerContext loggercontext = (LoggerContext) ContextAnchor.THREAD_CONTEXT.get();

        return loggercontext != null ? loggercontext : BasicContextSelector.CONTEXT;
    }

    public LoggerContext getContext(String s, ClassLoader classloader, boolean flag, URI uri) {
        LoggerContext loggercontext = (LoggerContext) ContextAnchor.THREAD_CONTEXT.get();

        return loggercontext != null ? loggercontext : BasicContextSelector.CONTEXT;
    }

    public LoggerContext locateContext(String s, String s1) {
        return BasicContextSelector.CONTEXT;
    }

    public void removeContext(LoggerContext loggercontext) {}

    public List getLoggerContexts() {
        ArrayList arraylist = new ArrayList();

        arraylist.add(BasicContextSelector.CONTEXT);
        return Collections.unmodifiableList(arraylist);
    }
}
