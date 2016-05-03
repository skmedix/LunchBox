package org.apache.commons.codec;

public interface BinaryDecoder extends Decoder {

    byte[] decode(byte[] abyte) throws DecoderException;
}
