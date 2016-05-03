package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class BoundedInputStream extends InputStream {

    private final InputStream in;
    private final long max;
    private long pos;
    private long mark;
    private boolean propagateClose;

    public BoundedInputStream(InputStream inputstream, long i) {
        this.pos = 0L;
        this.mark = -1L;
        this.propagateClose = true;
        this.max = i;
        this.in = inputstream;
    }

    public BoundedInputStream(InputStream inputstream) {
        this(inputstream, -1L);
    }

    public int read() throws IOException {
        if (this.max >= 0L && this.pos >= this.max) {
            return -1;
        } else {
            int i = this.in.read();

            ++this.pos;
            return i;
        }
    }

    public int read(byte[] abyte) throws IOException {
        return this.read(abyte, 0, abyte.length);
    }

    public int read(byte[] abyte, int i, int j) throws IOException {
        if (this.max >= 0L && this.pos >= this.max) {
            return -1;
        } else {
            long k = this.max >= 0L ? Math.min((long) j, this.max - this.pos) : (long) j;
            int l = this.in.read(abyte, i, (int) k);

            if (l == -1) {
                return -1;
            } else {
                this.pos += (long) l;
                return l;
            }
        }
    }

    public long skip(long i) throws IOException {
        long j = this.max >= 0L ? Math.min(i, this.max - this.pos) : i;
        long k = this.in.skip(j);

        this.pos += k;
        return k;
    }

    public int available() throws IOException {
        return this.max >= 0L && this.pos >= this.max ? 0 : this.in.available();
    }

    public String toString() {
        return this.in.toString();
    }

    public void close() throws IOException {
        if (this.propagateClose) {
            this.in.close();
        }

    }

    public synchronized void reset() throws IOException {
        this.in.reset();
        this.pos = this.mark;
    }

    public synchronized void mark(int i) {
        this.in.mark(i);
        this.mark = this.pos;
    }

    public boolean markSupported() {
        return this.in.markSupported();
    }

    public boolean isPropagateClose() {
        return this.propagateClose;
    }

    public void setPropagateClose(boolean flag) {
        this.propagateClose = flag;
    }
}
