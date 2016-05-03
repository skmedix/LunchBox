package org.apache.commons.codec;

public class EncoderException extends Exception {

    private static final long serialVersionUID = 1L;

    public EncoderException() {}

    public EncoderException(String s) {
        super(s);
    }

    public EncoderException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EncoderException(Throwable throwable) {
        super(throwable);
    }
}
