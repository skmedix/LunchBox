package org.apache.commons.codec;

public interface StringEncoder extends Encoder {

    String encode(String s) throws EncoderException;
}
