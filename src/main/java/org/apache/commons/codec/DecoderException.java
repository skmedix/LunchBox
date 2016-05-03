package org.apache.commons.codec;

public class DecoderException extends Exception {

    private static final long serialVersionUID = 1L;

    public DecoderException() {}

    public DecoderException(String s) {
        super(s);
    }

    public DecoderException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DecoderException(Throwable throwable) {
        super(throwable);
    }
}
