package org.apache.commons.io.input;

import java.io.Reader;
import java.io.Serializable;

public class CharSequenceReader extends Reader implements Serializable {

    private final CharSequence charSequence;
    private int idx;
    private int mark;

    public CharSequenceReader(CharSequence charsequence) {
        this.charSequence = (CharSequence) (charsequence != null ? charsequence : "");
    }

    public void close() {
        this.idx = 0;
        this.mark = 0;
    }

    public void mark(int i) {
        this.mark = this.idx;
    }

    public boolean markSupported() {
        return true;
    }

    public int read() {
        return this.idx >= this.charSequence.length() ? -1 : this.charSequence.charAt(this.idx++);
    }

    public int read(char[] achar, int i, int j) {
        if (this.idx >= this.charSequence.length()) {
            return -1;
        } else if (achar == null) {
            throw new NullPointerException("Character array is missing");
        } else if (j >= 0 && i >= 0 && i + j <= achar.length) {
            int k = 0;

            for (int l = 0; l < j; ++l) {
                int i1 = this.read();

                if (i1 == -1) {
                    return k;
                }

                achar[i + l] = (char) i1;
                ++k;
            }

            return k;
        } else {
            throw new IndexOutOfBoundsException("Array Size=" + achar.length + ", offset=" + i + ", length=" + j);
        }
    }

    public void reset() {
        this.idx = this.mark;
    }

    public long skip(long i) {
        if (i < 0L) {
            throw new IllegalArgumentException("Number of characters to skip is less than zero: " + i);
        } else if (this.idx >= this.charSequence.length()) {
            return -1L;
        } else {
            int j = (int) Math.min((long) this.charSequence.length(), (long) this.idx + i);
            int k = j - this.idx;

            this.idx = j;
            return (long) k;
        }
    }

    public String toString() {
        return this.charSequence.toString();
    }
}
