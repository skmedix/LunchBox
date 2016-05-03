package org.apache.commons.io.input;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class NullReader extends Reader {

    private final long size;
    private long position;
    private long mark;
    private long readlimit;
    private boolean eof;
    private final boolean throwEofException;
    private final boolean markSupported;

    public NullReader(long i) {
        this(i, true, false);
    }

    public NullReader(long i, boolean flag, boolean flag1) {
        this.mark = -1L;
        this.size = i;
        this.markSupported = flag;
        this.throwEofException = flag1;
    }

    public long getPosition() {
        return this.position;
    }

    public long getSize() {
        return this.size;
    }

    public void close() throws IOException {
        this.eof = false;
        this.position = 0L;
        this.mark = -1L;
    }

    public synchronized void mark(int i) {
        if (!this.markSupported) {
            throw new UnsupportedOperationException("Mark not supported");
        } else {
            this.mark = this.position;
            this.readlimit = (long) i;
        }
    }

    public boolean markSupported() {
        return this.markSupported;
    }

    public int read() throws IOException {
        if (this.eof) {
            throw new IOException("Read after end of file");
        } else if (this.position == this.size) {
            return this.doEndOfFile();
        } else {
            ++this.position;
            return this.processChar();
        }
    }

    public int read(char[] achar) throws IOException {
        return this.read(achar, 0, achar.length);
    }

    public int read(char[] achar, int i, int j) throws IOException {
        if (this.eof) {
            throw new IOException("Read after end of file");
        } else if (this.position == this.size) {
            return this.doEndOfFile();
        } else {
            this.position += (long) j;
            int k = j;

            if (this.position > this.size) {
                k = j - (int) (this.position - this.size);
                this.position = this.size;
            }

            this.processChars(achar, i, k);
            return k;
        }
    }

    public synchronized void reset() throws IOException {
        if (!this.markSupported) {
            throw new UnsupportedOperationException("Mark not supported");
        } else if (this.mark < 0L) {
            throw new IOException("No position has been marked");
        } else if (this.position > this.mark + this.readlimit) {
            throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readlimit + "]");
        } else {
            this.position = this.mark;
            this.eof = false;
        }
    }

    public long skip(long i) throws IOException {
        if (this.eof) {
            throw new IOException("Skip after end of file");
        } else if (this.position == this.size) {
            return (long) this.doEndOfFile();
        } else {
            this.position += i;
            long j = i;

            if (this.position > this.size) {
                j = i - (this.position - this.size);
                this.position = this.size;
            }

            return j;
        }
    }

    protected int processChar() {
        return 0;
    }

    protected void processChars(char[] achar, int i, int j) {}

    private int doEndOfFile() throws EOFException {
        this.eof = true;
        if (this.throwEofException) {
            throw new EOFException();
        } else {
            return -1;
        }
    }
}
