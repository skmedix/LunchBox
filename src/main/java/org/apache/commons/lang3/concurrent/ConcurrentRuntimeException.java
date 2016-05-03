package org.apache.commons.lang3.concurrent;

public class ConcurrentRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -6582182735562919670L;

    protected ConcurrentRuntimeException() {}

    public ConcurrentRuntimeException(Throwable throwable) {
        super(ConcurrentUtils.checkedException(throwable));
    }

    public ConcurrentRuntimeException(String s, Throwable throwable) {
        super(s, ConcurrentUtils.checkedException(throwable));
    }
}
