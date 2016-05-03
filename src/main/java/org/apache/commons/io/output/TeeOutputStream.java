package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class TeeOutputStream extends ProxyOutputStream {

    protected OutputStream branch;

    public TeeOutputStream(OutputStream outputstream, OutputStream outputstream1) {
        super(outputstream);
        this.branch = outputstream1;
    }

    public synchronized void write(byte[] abyte) throws IOException {
        super.write(abyte);
        this.branch.write(abyte);
    }

    public synchronized void write(byte[] abyte, int i, int j) throws IOException {
        super.write(abyte, i, j);
        this.branch.write(abyte, i, j);
    }

    public synchronized void write(int i) throws IOException {
        super.write(i);
        this.branch.write(i);
    }

    public void flush() throws IOException {
        super.flush();
        this.branch.flush();
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            this.branch.close();
        }

    }
}
