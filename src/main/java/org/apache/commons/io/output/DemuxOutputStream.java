package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class DemuxOutputStream extends OutputStream {

    private final InheritableThreadLocal m_streams = new InheritableThreadLocal();

    public OutputStream bindStream(OutputStream outputstream) {
        OutputStream outputstream1 = (OutputStream) this.m_streams.get();

        this.m_streams.set(outputstream);
        return outputstream1;
    }

    public void close() throws IOException {
        OutputStream outputstream = (OutputStream) this.m_streams.get();

        if (null != outputstream) {
            outputstream.close();
        }

    }

    public void flush() throws IOException {
        OutputStream outputstream = (OutputStream) this.m_streams.get();

        if (null != outputstream) {
            outputstream.flush();
        }

    }

    public void write(int i) throws IOException {
        OutputStream outputstream = (OutputStream) this.m_streams.get();

        if (null != outputstream) {
            outputstream.write(i);
        }

    }
}
