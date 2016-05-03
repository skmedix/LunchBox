package org.apache.commons.io.input;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class ProxyInputStream extends FilterInputStream {

    public ProxyInputStream(InputStream inputstream) {
        super(inputstream);
    }

    public int read() throws IOException {
        try {
            this.beforeRead(1);
            int i = this.in.read();

            this.afterRead(i != -1 ? 1 : -1);
            return i;
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
            return -1;
        }
    }

    public int read(byte[] abyte) throws IOException {
        try {
            this.beforeRead(abyte != null ? abyte.length : 0);
            int i = this.in.read(abyte);

            this.afterRead(i);
            return i;
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
            return -1;
        }
    }

    public int read(byte[] abyte, int i, int j) throws IOException {
        try {
            this.beforeRead(j);
            int k = this.in.read(abyte, i, j);

            this.afterRead(k);
            return k;
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
            return -1;
        }
    }

    public long skip(long i) throws IOException {
        try {
            return this.in.skip(i);
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
            return 0L;
        }
    }

    public int available() throws IOException {
        try {
            return super.available();
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
            return 0;
        }
    }

    public void close() throws IOException {
        try {
            this.in.close();
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

    }

    public synchronized void mark(int i) {
        this.in.mark(i);
    }

    public synchronized void reset() throws IOException {
        try {
            this.in.reset();
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

    }

    public boolean markSupported() {
        return this.in.markSupported();
    }

    protected void beforeRead(int i) throws IOException {}

    protected void afterRead(int i) throws IOException {}

    protected void handleIOException(IOException ioexception) throws IOException {
        throw ioexception;
    }
}
