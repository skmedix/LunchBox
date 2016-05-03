package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends ProxyInputStream {

    private long count;

    public CountingInputStream(InputStream inputstream) {
        super(inputstream);
    }

    public synchronized long skip(long i) throws IOException {
        long j = super.skip(i);

        this.count += j;
        return j;
    }

    protected synchronized void afterRead(int i) {
        if (i != -1) {
            this.count += (long) i;
        }

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
