package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProxyOutputStream extends FilterOutputStream {

    public ProxyOutputStream(OutputStream outputstream) {
        super(outputstream);
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

    public void write(byte[] abyte) throws IOException {
        try {
            int i = abyte != null ? abyte.length : 0;

            this.beforeWrite(i);
            this.out.write(abyte);
            this.afterWrite(i);
        } catch (IOException ioexception) {
            this.handleIOException(ioexception);
        }

    }

    public void write(byte[] abyte, int i, int j) throws IOException {
        try {
            this.beforeWrite(j);
            this.out.write(abyte, i, j);
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
