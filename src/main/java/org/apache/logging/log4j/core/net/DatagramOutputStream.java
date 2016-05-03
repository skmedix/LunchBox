package org.apache.logging.log4j.core.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.status.StatusLogger;

public class DatagramOutputStream extends OutputStream {

    protected static final Logger LOGGER = StatusLogger.getLogger();
    private static final int SHIFT_1 = 8;
    private static final int SHIFT_2 = 16;
    private static final int SHIFT_3 = 24;
    private DatagramSocket ds;
    private final InetAddress address;
    private final int port;
    private byte[] data;
    private final byte[] header;
    private final byte[] footer;

    public DatagramOutputStream(String s, int i, byte[] abyte, byte[] abyte1) {
        this.port = i;
        this.header = abyte;
        this.footer = abyte1;

        String s1;

        try {
            this.address = InetAddress.getByName(s);
        } catch (UnknownHostException unknownhostexception) {
            s1 = "Could not find host " + s;
            DatagramOutputStream.LOGGER.error(s1, (Throwable) unknownhostexception);
            throw new AppenderLoggingException(s1, unknownhostexception);
        }

        try {
            this.ds = new DatagramSocket();
        } catch (SocketException socketexception) {
            s1 = "Could not instantiate DatagramSocket to " + s;
            DatagramOutputStream.LOGGER.error(s1, (Throwable) socketexception);
            throw new AppenderLoggingException(s1, socketexception);
        }
    }

    public synchronized void write(byte[] abyte, int i, int j) throws IOException {
        this.copy(abyte, i, j);
    }

    public synchronized void write(int i) throws IOException {
        this.copy(new byte[] { (byte) (i >>> 24), (byte) (i >>> 16), (byte) (i >>> 8), (byte) i}, 0, 4);
    }

    public synchronized void write(byte[] abyte) throws IOException {
        this.copy(abyte, 0, abyte.length);
    }

    public synchronized void flush() throws IOException {
        try {
            if (this.data != null && this.ds != null && this.address != null) {
                if (this.footer != null) {
                    this.copy(this.footer, 0, this.footer.length);
                }

                DatagramPacket datagrampacket = new DatagramPacket(this.data, this.data.length, this.address, this.port);

                this.ds.send(datagrampacket);
            }
        } finally {
            this.data = null;
            if (this.header != null) {
                this.copy(this.header, 0, this.header.length);
            }

        }

    }

    public synchronized void close() throws IOException {
        if (this.ds != null) {
            if (this.data != null) {
                this.flush();
            }

            this.ds.close();
            this.ds = null;
        }

    }

    private void copy(byte[] abyte, int i, int j) {
        int k = this.data == null ? 0 : this.data.length;
        byte[] abyte1 = new byte[j + k];

        if (this.data != null) {
            System.arraycopy(this.data, 0, abyte1, 0, this.data.length);
        }

        System.arraycopy(abyte, i, abyte1, k, j);
        this.data = abyte1;
    }
}
