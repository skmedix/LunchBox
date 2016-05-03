package org.apache.commons.codec.binary;

import java.io.OutputStream;

public class Base64OutputStream extends BaseNCodecOutputStream {

    public Base64OutputStream(OutputStream outputstream) {
        this(outputstream, true);
    }

    public Base64OutputStream(OutputStream outputstream, boolean flag) {
        super(outputstream, new Base64(false), flag);
    }

    public Base64OutputStream(OutputStream outputstream, boolean flag, int i, byte[] abyte) {
        super(outputstream, new Base64(i, abyte), flag);
    }
}
