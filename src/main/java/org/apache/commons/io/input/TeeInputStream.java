package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TeeInputStream extends ProxyInputStream {

    private final OutputStream branch;
    private final boolean closeBranch;

    public TeeInputStream(InputStream inputstream, OutputStream outputstream) {
        this(inputstream, outputstream, false);
    }

    public TeeInputStream(InputStream inputstream, OutputStream outputstream, boolean flag) {
        super(inputstream);
        this.branch = outputstream;
        this.closeBranch = flag;
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (this.closeBranch) {
                this.branch.close();
            }

        }

    }

    public int read() throws IOException {
        int i = super.read();

        if (i != -1) {
            this.branch.write(i);
        }

        return i;
    }

    public int read(byte[] abyte, int i, int j) throws IOException {
        int k = super.read(abyte, i, j);

        if (k != -1) {
            this.branch.write(abyte, i, k);
        }

        return k;
    }

    public int read(byte[] abyte) throws IOException {
        int i = super.read(abyte);

        if (i != -1) {
            this.branch.write(abyte, 0, i);
        }

        return i;
    }
}
