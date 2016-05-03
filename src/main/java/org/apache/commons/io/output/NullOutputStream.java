package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {

    public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();

    public void write(byte[] abyte, int i, int j) {}

    public void write(int i) {}

    public void write(byte[] abyte) throws IOException {}
}
