package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class DemuxInputStream extends InputStream {

    private final InheritableThreadLocal m_streams = new InheritableThreadLocal();

    public InputStream bindStream(InputStream inputstream) {
        InputStream inputstream1 = (InputStream) this.m_streams.get();

        this.m_streams.set(inputstream);
        return inputstream1;
    }

    public void close() throws IOException {
        InputStream inputstream = (InputStream) this.m_streams.get();

        if (null != inputstream) {
            inputstream.close();
        }

    }

    public int read() throws IOException {
        InputStream inputstream = (InputStream) this.m_streams.get();

        return null != inputstream ? inputstream.read() : -1;
    }
}
