package org.apache.commons.io.output;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class ProxyWriter extends FilterWriter {

    public ProxyWriter(Writer writer) {
        super(writer);
    }

    public Writer append(char c0) throws IOException {
        try {
            this.beforeWrite(1);
            this.out.append(c0);
            this.afterWrite(1);
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

        return this;
    }

    public Writer append(CharSequence charsequence, int i, int j) throws IOException {
        try {
            this.beforeWrite(j - i);
            this.out.append(charsequence, i, j);
            this.afterWrite(j - i);
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

        return this;
    }

    public Writer append(CharSequence charsequence) throws IOException {
        try {
            int i = 0;

            if (charsequence != null) {
                i = charsequence.length();
            }

            this.beforeWrite(i);
            this.out.append(charsequence);
            this.afterWrite(i);
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

        return this;
    }

    public void write(int i) throws IOException {
        try {
            this.beforeWrite(1);
            this.out.write(i);
            this.afterWrite(1);
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

    }

    public void write(char[] achar) throws IOException {
        try {
            int i = 0;

            if (achar != null) {
                i = achar.length;
            }

            this.beforeWrite(i);
            this.out.write(achar);
            this.afterWrite(i);
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

    }

    public void write(char[] achar, int i, int j) throws IOException {
        try {
            this.beforeWrite(j);
            this.out.write(achar, i, j);
            this.afterWrite(j);
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

    }

    public void write(String s) throws IOException {
        try {
            int i = 0;

            if (s != null) {
                i = s.length();
            }

            this.beforeWrite(i);
            this.out.write(s);
            this.afterWrite(i);
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

    }

    public void write(String s, int i, int j) throws IOException {
        try {
            this.beforeWrite(j);
            this.out.write(s, i, j);
            this.afterWrite(j);
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

    }

    public void flush() throws IOException {
        try {
            this.out.flush();
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

    }

    public void close() throws IOException {
        try {
            this.out.close();
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

    }

    protected void beforeWrite(int i) throws IOException {}

    protected void afterWrite(int i) throws IOException {}

    protected void handleIOException(IOException ioexception) throws IOException {
        throw ioexception;
    }
}
