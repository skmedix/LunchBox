package org.apache.commons.io.output;

import java.io.OutputStream;

public class CountingOutputStream extends ProxyOutputStream {

    private long count = 0L;

    public CountingOutputStream(OutputStream outputstream) {
        super(outputstream);
    }

    protected synchronized void beforeWrite(int i) {
        this.count += (long) i;
    }

    public int getCount() {
        long i = this.getByteCount();

        if (i > 2147483647L) {
            throw new ArithmeticException("The byte count " + i + " is too large to be converted to an int");
        } else {
            return (int) i;
        }
    }

    public int resetCount() {
        long i = this.resetByteCount();

        if (i > 2147483647L) {
            throw new ArithmeticException("The byte count " + i + " is too large to be converted to an int");
        } else {
            return (int) i;
        }
    }

    public synchronized long getByteCount() {
        return this.count;
    }

    public synchronized long resetByteCount() {
        long i = this.count;

        this.count = 0L;
        return i;
    }
}
