package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.logging.log4j.core.Layout;

public class OutputStreamManager extends AbstractManager {

    private volatile OutputStream os;
    private final byte[] footer;
    private final byte[] header;

    protected OutputStreamManager(OutputStream outputstream, String s, Layout layout) {
        super(s);
        this.os = outputstream;
        if (layout != null) {
            this.footer = layout.getFooter();
            this.header = layout.getHeader();
            if (this.header != null) {
                try {
                    this.os.write(this.header, 0, this.header.length);
                } catch (IOException ioexception) {
                    OutputStreamManager.LOGGER.error("Unable to write header", (Throwable) ioexception);
                }
            }
        } else {
            this.footer = null;
            this.header = null;
        }

    }

    public static OutputStreamManager getManager(String s, Object object, ManagerFactory managerfactory) {
        return (OutputStreamManager) AbstractManager.getManager(s, managerfactory, object);
    }

    public void releaseSub() {
        if (this.footer != null) {
            this.write(this.footer);
        }

        this.close();
    }

    public boolean isOpen() {
        return this.getCount() > 0;
    }

    protected OutputStream getOutputStream() {
        return this.os;
    }

    protected void setOutputStream(OutputStream outputstream) {
        if (this.header != null) {
            try {
                outputstream.write(this.header, 0, this.header.length);
                this.os = outputstream;
            } catch (IOException ioexception) {
                OutputStreamManager.LOGGER.error("Unable to write header", (Throwable) ioexception);
            }
        } else {
            this.os = outputstream;
        }

    }

    protected synchronized void write(byte[] abyte, int i, int j) {
        try {
            this.os.write(abyte, i, j);
        } catch (IOException ioexception) {
            String s = "Error writing to stream " + this.getName();

            throw new AppenderLoggingException(s, ioexception);
        }
    }

    protected void write(byte[] abyte) {
        this.write(abyte, 0, abyte.length);
    }

    protected synchronized void close() {
        OutputStream outputstream = this.os;

        if (outputstream != System.out && outputstream != System.err) {
            try {
                outputstream.close();
            } catch (IOException ioexception) {
                OutputStreamManager.LOGGER.error("Unable to close stream " + this.getName() + ". " + ioexception);
            }

        }
    }

    public synchronized void flush() {
        try {
            this.os.flush();
        } catch (IOException ioexception) {
            String s = "Error flushing stream " + this.getName();

            throw new AppenderLoggingException(s, ioexception);
        }
    }
}
