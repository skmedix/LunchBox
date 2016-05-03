package org.apache.commons.codec.binary;

import java.io.InputStream;

public class Base64InputStream extends BaseNCodecInputStream {

    public Base64InputStream(InputStream inputstream) {
        this(inputstream, false);
    }

    public Base64InputStream(InputStream inputstream, boolean flag) {
        super(inputstream, new Base64(false), flag);
    }

    public Base64InputStream(InputStream inputstream, boolean flag, int i, byte[] abyte) {
        super(inputstream, new Base64(i, abyte), flag);
    }
}
