package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.LoggingException;

public class AppenderLoggingException extends LoggingException {

    private static final long serialVersionUID = 6545990597472958303L;

    public AppenderLoggingException(String s) {
        super(s);
    }

    public AppenderLoggingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AppenderLoggingException(Throwable throwable) {
        super(throwable);
    }
}
