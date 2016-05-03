package org.apache.commons.lang3.exception;

public class CloneFailedException extends RuntimeException {

    private static final long serialVersionUID = 20091223L;

    public CloneFailedException(String s) {
        super(s);
    }

    public CloneFailedException(Throwable throwable) {
        super(throwable);
    }

    public CloneFailedException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
