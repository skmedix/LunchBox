package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;

class Utils {

    static int digit16(byte b0) throws DecoderException {
        int i = Character.digit((char) b0, 16);

        if (i == -1) {
            throw new DecoderException("Invalid URL encoding: not a valid digit (radix 16): " + b0);
        } else {
            return i;
        }
    }
}
