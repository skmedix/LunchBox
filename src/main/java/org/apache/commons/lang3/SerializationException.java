package org.apache.commons.lang3;

public class SerializationException extends RuntimeException {

    private static final long serialVersionUID = 4029025366392702726L;

    public SerializationException() {}

    public SerializationException(String s) {
        super(s);
    }

    public SerializationException(Throwable throwable) {
        super(throwable);
    }

    public SerializationException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
