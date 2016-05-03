package org.apache.commons.codec.binary;

import java.io.OutputStream;

public class Base32OutputStream extends BaseNCodecOutputStream {

    public Base32OutputStream(OutputStream outputstream) {
        this(outputstream, true);
    }

    public Base32OutputStream(OutputStream outputstream, boolean flag) {
        super(outputstream, new Base32(false), flag);
    }

    public Base32OutputStream(OutputStream outputstream, boolean flag, int i, byte[] abyte) {
        super(outputstream, new Base32(i, abyte), flag);
    }
}
