package org.apache.logging.log4j.core;

public interface Appender extends LifeCycle {

    void append(LogEvent logevent);

    String getName();

    Layout getLayout();

    boolean ignoreExceptions();

    ErrorHandler getHandler();

    void setHandler(ErrorHandler errorhandler);
}
