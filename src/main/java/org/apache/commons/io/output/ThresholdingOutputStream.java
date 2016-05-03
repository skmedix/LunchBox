package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ThresholdingOutputStream extends OutputStream {

    private final int threshold;
    private long written;
    private boolean thresholdExceeded;

    public ThresholdingOutputStream(int i) {
        this.threshold = i;
    }

    public void write(int i) throws IOException {
        this.checkThreshold(1);
        this.getStream().write(i);
        ++this.written;
    }

    public void write(byte[] abyte) throws IOException {
        this.checkThreshold(abyte.length);
        this.getStream().write(abyte);
        this.written += (long) abyte.length;
    }

    public void write(byte[] abyte, int i, int j) throws IOException {
        this.checkThreshold(j);
        this.getStream().write(abyte, i, j);
        this.written += (long) j;
    }

    public void flush() throws IOException {
        this.getStream().flush();
    }

    public void close() throws IOException {
        try {
            this.flush();
        } catch (IOException ioexception) {
            ;
        }

        this.getStream().close();
    }

    public int getThreshold() {
        return this.threshold;
    }

    public long getByteCount() {
        return this.written;
    }

    public boolean isThresholdExceeded() {
        return this.written > (long) this.threshold;
    }

    protected void checkThreshold(int i) throws IOException {
        if (!this.thresholdExceeded && this.written + (long) i > (long) this.threshold) {
            this.thresholdExceeded = true;
            this.thresholdReached();
        }

    }

    protected void resetByteCount() {
        this.thresholdExceeded = false;
        this.written = 0L;
    }

    protected abstract OutputStream getStream() throws IOException;

    protected abstract void thresholdReached() throws IOException;
}
