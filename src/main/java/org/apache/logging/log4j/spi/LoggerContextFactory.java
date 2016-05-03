package org.apache.logging.log4j.spi;

import java.net.URI;

public interface LoggerContextFactory {

    LoggerContext getContext(String s, ClassLoader classloader, boolean flag);

    LoggerContext getContext(String s, ClassLoader classloader, boolean flag, URI uri);

    void removeContext(LoggerContext loggercontext);
}
