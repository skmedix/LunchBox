package org.apache.commons.io.input;

import java.io.InputStream;

public class CloseShieldInputStream extends ProxyInputStream {

    public CloseShieldInputStream(InputStream inputstream) {
        super(inputstream);
    }

    public void close() {
        this.in = new ClosedInputStream();
    }
}
