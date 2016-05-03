package org.apache.logging.log4j;

public class LoggingException extends RuntimeException {

    private static final long serialVersionUID = 6366395965071580537L;

    public LoggingException(String s) {
        super(s);
    }

    public LoggingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public LoggingException(Throwable throwable) {
        super(throwable);
    }
}
