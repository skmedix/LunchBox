package org.apache.commons.io.output;

import java.io.Writer;

public class NullWriter extends Writer {

    public static final NullWriter NULL_WRITER = new NullWriter();

    public Writer append(char c0) {
        return this;
    }

    public Writer append(CharSequence charsequence, int i, int j) {
        return this;
    }

    public Writer append(CharSequence charsequence) {
        return this;
    }

    public void write(int i) {}

    public void write(char[] achar) {}

    public void write(char[] achar, int i, int j) {}

    public void write(String s) {}

    public void write(String s, int i, int j) {}

    public void flush() {}

    public void close() {}
}
