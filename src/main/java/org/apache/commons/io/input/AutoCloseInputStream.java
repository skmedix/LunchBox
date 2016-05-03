package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class AutoCloseInputStream extends ProxyInputStream {

    public AutoCloseInputStream(InputStream inputstream) {
        super(inputstream);
    }

    public void close() throws IOException {
        this.in.close();
        this.in = new ClosedInputStream();
    }

    protected void afterRead(int i) throws IOException {
        if (i == -1) {
            this.close();
        }

    }

    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
