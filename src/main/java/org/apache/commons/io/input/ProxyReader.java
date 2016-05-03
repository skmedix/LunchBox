package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public abstract class ProxyReader extends FilterReader {

    public ProxyReader(Reader reader) {
        super(reader);
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

    public int read(char[] achar) throws IOException {
        try {
            this.beforeRead(achar != null ? achar.length : 0);
            int i = this.in.read(achar);

            this.afterRead(i);
            return i;
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
            return -1;
        }
    }

    public int read(char[] achar, int i, int j) throws IOException {
        try {
            this.beforeRead(j);
            int k = this.in.read(achar, i, j);

            this.afterRead(k);
            return k;
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
            return -1;
        }
    }

    public int read(CharBuffer charbuffer) throws IOException {
        try {
            this.beforeRead(charbuffer != null ? charbuffer.length() : 0);
            int i = this.in.read(charbuffer);

            this.afterRead(i);
            return i;
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

    public boolean ready() throws IOException {
        try {
            return this.in.ready();
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
            return false;
        }
    }

    public void close() throws IOException {
        try {
            this.in.close();
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

    }

    public synchronized void mark(int i) throws IOException {
        try {
            this.in.mark(i);
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

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
