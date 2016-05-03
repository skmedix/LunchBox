package org.apache.logging.log4j.core;

public interface ErrorHandler {

    void error(String s);

    void error(String s, Throwable throwable);

    void error(String s, LogEvent logevent, Throwable throwable);
}
