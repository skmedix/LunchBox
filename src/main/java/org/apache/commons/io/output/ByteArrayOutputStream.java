package org.apache.commons.io.output;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.input.ClosedInputStream;

public class ByteArrayOutputStream extends OutputStream {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private final List buffers;
    private int currentBufferIndex;
    private int filledBufferSum;
    private byte[] currentBuffer;
    private int count;

    public ByteArrayOutputStream() {
        this(1024);
    }

    public ByteArrayOutputStream(int i) {
        this.buffers = new ArrayList();
        if (i < 0) {
            throw new IllegalArgumentException("Negative initial size: " + i);
        } else {
            synchronized (this) {
                this.needNewBuffer(i);
            }
        }
    }

    private void needNewBuffer(int i) {
        if (this.currentBufferIndex < this.buffers.size() - 1) {
            this.filledBufferSum += this.currentBuffer.length;
            ++this.currentBufferIndex;
            this.currentBuffer = (byte[]) this.buffers.get(this.currentBufferIndex);
        } else {
            int j;

            if (this.currentBuffer == null) {
                j = i;
                this.filledBufferSum = 0;
            } else {
                j = Math.max(this.currentBuffer.length << 1, i - this.filledBufferSum);
                this.filledBufferSum += this.currentBuffer.length;
            }

            ++this.currentBufferIndex;
            this.currentBuffer = new byte[j];
            this.buffers.add(this.currentBuffer);
        }

    }

    public void write(byte[] abyte, int i, int j) {
        if (i >= 0 && i <= abyte.length && j >= 0 && i + j <= abyte.length && i + j >= 0) {
            if (j != 0) {
                synchronized (this) {
                    int k = this.count + j;
                    int l = j;
                    int i1 = this.count - this.filledBufferSum;

                    while (l > 0) {
                        int j1 = Math.min(l, this.currentBuffer.length - i1);

                        System.arraycopy(abyte, i + j - l, this.currentBuffer, i1, j1);
                        l -= j1;
                        if (l > 0) {
                            this.needNewBuffer(k);
                            i1 = 0;
                        }
                    }

                    this.count = k;
                }
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public synchronized void write(int i) {
        int j = this.count - this.filledBufferSum;

        if (j == this.currentBuffer.length) {
            this.needNewBuffer(this.count + 1);
            j = 0;
        }

        this.currentBuffer[j] = (byte) i;
        ++this.count;
    }

    public synchronized int write(InputStream inputstream) throws IOException {
        int i = 0;
        int j = this.count - this.filledBufferSum;

        for (int k = inputstream.read(this.currentBuffer, j, this.currentBuffer.length - j); k != -1; k = inputstream.read(this.currentBuffer, j, this.currentBuffer.length - j)) {
            i += k;
            j += k;
            this.count += k;
            if (j == this.currentBuffer.length) {
                this.needNewBuffer(this.currentBuffer.length);
                j = 0;
            }
        }

        return i;
    }

    public synchronized int size() {
        return this.count;
    }

    public void close() throws IOException {}

    public synchronized void reset() {
        this.count = 0;
        this.filledBufferSum = 0;
        this.currentBufferIndex = 0;
        this.currentBuffer = (byte[]) this.buffers.get(this.currentBufferIndex);
    }

    public synchronized void writeTo(OutputStream outputstream) throws IOException {
        int i = this.count;
        Iterator iterator = this.buffers.iterator();

        while (iterator.hasNext()) {
            byte[] abyte = (byte[]) iterator.next();
            int j = Math.min(abyte.length, i);

            outputstream.write(abyte, 0, j);
            i -= j;
            if (i == 0) {
                break;
            }
        }

    }

    public static InputStream toBufferedInputStream(InputStream inputstream) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

        bytearrayoutputstream.write(inputstream);
        return bytearrayoutputstream.toBufferedInputStream();
    }

    private InputStream toBufferedInputStream() {
        int i = this.count;

        if (i == 0) {
            return new ClosedInputStream();
        } else {
            ArrayList arraylist = new ArrayList(this.buffers.size());
            Iterator iterator = this.buffers.iterator();

            while (iterator.hasNext()) {
                byte[] abyte = (byte[]) iterator.next();
                int j = Math.min(abyte.length, i);

                arraylist.add(new ByteArrayInputStream(abyte, 0, j));
                i -= j;
                if (i == 0) {
                    break;
                }
            }

            return new SequenceInputStream(Collections.enumeration(arraylist));
        }
    }

    public synchronized byte[] toByteArray() {
        int i = this.count;

        if (i == 0) {
            return ByteArrayOutputStream.EMPTY_BYTE_ARRAY;
        } else {
            byte[] abyte = new byte[i];
            int j = 0;
            Iterator iterator = this.buffers.iterator();

            while (iterator.hasNext()) {
                byte[] abyte1 = (byte[]) iterator.next();
                int k = Math.min(abyte1.length, i);

                System.arraycopy(abyte1, 0, abyte, j, k);
                j += k;
                i -= k;
                if (i == 0) {
                    break;
                }
            }

            return abyte;
        }
    }

    public String toString() {
        return new String(this.toByteArray());
    }

    public String toString(String s) throws UnsupportedEncodingException {
        return new String(this.toByteArray(), s);
    }
}
