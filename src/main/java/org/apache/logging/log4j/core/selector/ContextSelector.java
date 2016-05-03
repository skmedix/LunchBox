package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.List;
import org.apache.logging.log4j.core.LoggerContext;

public interface ContextSelector {

    LoggerContext getContext(String s, ClassLoader classloader, boolean flag);

    LoggerContext getContext(String s, ClassLoader classloader, boolean flag, URI uri);

    List getLoggerContexts();

    void removeContext(LoggerContext loggercontext);
}
