package org.apache.commons.codec;

public interface Decoder {

    Object decode(Object object) throws DecoderException;
}
