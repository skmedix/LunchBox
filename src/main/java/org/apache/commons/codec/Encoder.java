package org.apache.commons.codec;

public interface Encoder {

    Object encode(Object object) throws EncoderException;
}
