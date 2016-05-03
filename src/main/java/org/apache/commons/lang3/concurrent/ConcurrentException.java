package org.apache.commons.lang3.concurrent;

public class ConcurrentException extends Exception {

    private static final long serialVersionUID = 6622707671812226130L;

    protected ConcurrentException() {}

    public ConcurrentException(Throwable throwable) {
        super(ConcurrentUtils.checkedException(throwable));
    }

    public ConcurrentException(String s, Throwable throwable) {
        super(s, ConcurrentUtils.checkedException(throwable));
    }
}
