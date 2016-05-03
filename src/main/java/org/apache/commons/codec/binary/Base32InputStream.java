package org.apache.commons.codec.binary;

import java.io.InputStream;

public class Base32InputStream extends BaseNCodecInputStream {

    public Base32InputStream(InputStream inputstream) {
        this(inputstream, false);
    }

    public Base32InputStream(InputStream inputstream, boolean flag) {
        super(inputstream, new Base32(false), flag);
    }

    public Base32InputStream(InputStream inputstream, boolean flag, int i, byte[] abyte) {
        super(inputstream, new Base32(i, abyte), flag);
    }
}
