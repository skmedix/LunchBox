package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.status.StatusLogger;

public class DefaultErrorHandler implements ErrorHandler {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final int MAX_EXCEPTIONS = 3;
    private static final int EXCEPTION_INTERVAL = 300000;
    private int exceptionCount = 0;
    private long lastException;
    private final Appender appender;

    public DefaultErrorHandler(Appender appender) {
        this.appender = appender;
    }

    public void error(String s) {
        long i = System.currentTimeMillis();

        if (this.lastException + 300000L < i || this.exceptionCount++ < 3) {
            DefaultErrorHandler.LOGGER.error(s);
        }

        this.lastException = i;
    }

    public void error(String s, Throwable throwable) {
        long i = System.currentTimeMillis();

        if (this.lastException + 300000L < i || this.exceptionCount++ < 3) {
            DefaultErrorHandler.LOGGER.error(s, throwable);
        }

        this.lastException = i;
        if (!this.appender.ignoreExceptions() && throwable != null && !(throwable instanceof AppenderLoggingException)) {
            throw new AppenderLoggingException(s, throwable);
        }
    }

    public void error(String s, LogEvent logevent, Throwable throwable) {
        long i = System.currentTimeMillis();

        if (this.lastException + 300000L < i || this.exceptionCount++ < 3) {
            DefaultErrorHandler.LOGGER.error(s, throwable);
        }

        this.lastException = i;
        if (!this.appender.ignoreExceptions() && throwable != null && !(throwable instanceof AppenderLoggingException)) {
            throw new AppenderLoggingException(s, throwable);
        }
    }
}
