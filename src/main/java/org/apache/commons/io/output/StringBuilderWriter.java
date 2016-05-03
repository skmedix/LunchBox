package org.apache.commons.io.output;

import java.io.Serializable;
import java.io.Writer;

public class StringBuilderWriter extends Writer implements Serializable {

    private final StringBuilder builder;

    public StringBuilderWriter() {
        this.builder = new StringBuilder();
    }

    public StringBuilderWriter(int i) {
        this.builder = new StringBuilder(i);
    }

    public StringBuilderWriter(StringBuilder stringbuilder) {
        this.builder = stringbuilder != null ? stringbuilder : new StringBuilder();
    }

    public Writer append(char c0) {
        this.builder.append(c0);
        return this;
    }

    public Writer append(CharSequence charsequence) {
        this.builder.append(charsequence);
        return this;
    }

    public Writer append(CharSequence charsequence, int i, int j) {
        this.builder.append(charsequence, i, j);
        return this;
    }

    public void close() {}

    public void flush() {}

    public void write(String s) {
        if (s != null) {
            this.builder.append(s);
        }

    }

    public void write(char[] achar, int i, int j) {
        if (achar != null) {
            this.builder.append(achar, i, j);
        }

    }

    public StringBuilder getBuilder() {
        return this.builder;
    }

    public String toString() {
        return this.builder.toString();
    }
}
