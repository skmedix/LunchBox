package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.helpers.Strings;

public class TCPSocketManager extends AbstractSocketManager {

    public static final int DEFAULT_RECONNECTION_DELAY = 30000;
    private static final int DEFAULT_PORT = 4560;
    private static final TCPSocketManager.TCPSocketManagerFactory FACTORY = new TCPSocketManager.TCPSocketManagerFactory();
    private final int reconnectionDelay;
    private TCPSocketManager.Reconnector connector = null;
    private Socket socket;
    private final boolean retry;
    private final boolean immediateFail;

    public TCPSocketManager(String s, OutputStream outputstream, Socket socket, InetAddress inetaddress, String s1, int i, int j, boolean flag, Layout layout) {
        super(s, outputstream, inetaddress, s1, i, layout);
        this.reconnectionDelay = j;
        this.socket = socket;
        this.immediateFail = flag;
        this.retry = j > 0;
        if (socket == null) {
            this.connector = new TCPSocketManager.Reconnector(this);
            this.connector.setDaemon(true);
            this.connector.setPriority(1);
            this.connector.start();
        }

    }

    public static TCPSocketManager getSocketManager(String s, int i, int j, boolean flag, Layout layout) {
        if (Strings.isEmpty(s)) {
            throw new IllegalArgumentException("A host name is required");
        } else {
            if (i <= 0) {
                i = 4560;
            }

            if (j == 0) {
                j = 30000;
            }

            return (TCPSocketManager) getManager("TCP:" + s + ":" + i, (Object) (new TCPSocketManager.FactoryData(s, i, j, flag, layout)), (ManagerFactory) TCPSocketManager.FACTORY);
        }
    }

    protected void write(byte[] abyte, int i, int j) {
        if (this.socket == null) {
            if (this.connector != null && !this.immediateFail) {
                this.connector.latch();
            }

            if (this.socket == null) {
                String s = "Error writing to " + this.getName() + " socket not available";

                throw new AppenderLoggingException(s);
            }
        }

        synchronized (this) {
            try {
                this.getOutputStream().write(abyte, i, j);
            } catch (IOException ioexception) {
                if (this.retry && this.connector == null) {
                    this.connector = new TCPSocketManager.Reconnector(this);
                    this.connector.setDaemon(true);
                    this.connector.setPriority(1);
                    this.connector.start();
                }

                String s1 = "Error writing to " + this.getName();

                throw new AppenderLoggingException(s1, ioexception);
            }

        }
    }

    protected synchronized void close() {
        super.close();
        if (this.connector != null) {
            this.connector.shutdown();
            this.connector.interrupt();
            this.connector = null;
        }

    }

    public Map getContentFormat() {
        HashMap hashmap = new HashMap(super.getContentFormat());

        hashmap.put("protocol", "tcp");
        hashmap.put("direction", "out");
        return hashmap;
    }

    protected Socket createSocket(InetAddress inetaddress, int i) throws IOException {
        return this.createSocket(inetaddress.getHostName(), i);
    }

    protected Socket createSocket(String s, int i) throws IOException {
        return new Socket(s, i);
    }

    protected static class TCPSocketManagerFactory implements ManagerFactory {

        public TCPSocketManager createManager(String s, TCPSocketManager.FactoryData tcpsocketmanager_factorydata) {
            InetAddress inetaddress;

            try {
                inetaddress = InetAddress.getByName(tcpsocketmanager_factorydata.host);
            } catch (UnknownHostException unknownhostexception) {
                TCPSocketManager.LOGGER.error("Could not find address of " + tcpsocketmanager_factorydata.host, (Throwable) unknownhostexception);
                return null;
            }

            try {
                Socket socket = new Socket(tcpsocketmanager_factorydata.host, tcpsocketmanager_factorydata.port);
                OutputStream outputstream = socket.getOutputStream();

                return new TCPSocketManager(s, outputstream, socket, inetaddress, tcpsocketmanager_factorydata.host, tcpsocketmanager_factorydata.port, tcpsocketmanager_factorydata.delay, tcpsocketmanager_factorydata.immediateFail, tcpsocketmanager_factorydata.layout);
            } catch (IOException ioexception) {
                TCPSocketManager.LOGGER.error("TCPSocketManager (" + s + ") " + ioexception);
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

                return tcpsocketmanager_factorydata.delay == 0 ? null : new TCPSocketManager(s, bytearrayoutputstream, (Socket) null, inetaddress, tcpsocketmanager_factorydata.host, tcpsocketmanager_factorydata.port, tcpsocketmanager_factorydata.delay, tcpsocketmanager_factorydata.immediateFail, tcpsocketmanager_factorydata.layout);
            }
        }
    }

    private static class FactoryData {

        private final String host;
        private final int port;
        private final int delay;
        private final boolean immediateFail;
        private final Layout layout;

        public FactoryData(String s, int i, int j, boolean flag, Layout layout) {
            this.host = s;
            this.port = i;
            this.delay = j;
            this.immediateFail = flag;
            this.layout = layout;
        }
    }

    private class Reconnector extends Thread {

        private final CountDownLatch latch = new CountDownLatch(1);
        private boolean shutdown = false;
        private final Object owner;

        public Reconnector(OutputStreamManager outputstreammanager) {
            this.owner = outputstreammanager;
        }

        public void latch() {
            try {
                this.latch.await();
            } catch (InterruptedException interruptedexception) {
                ;
            }

        }

        public void shutdown() {
            this.shutdown = true;
        }

        public void run() {
            while (!this.shutdown) {
                try {
                    sleep((long) TCPSocketManager.this.reconnectionDelay);
                    Socket socket = TCPSocketManager.this.createSocket(TCPSocketManager.this.address, TCPSocketManager.this.port);
                    OutputStream outputstream = socket.getOutputStream();
                    Object object = this.owner;

                    synchronized (this.owner) {
                        try {
                            TCPSocketManager.this.getOutputStream().close();
                        } catch (IOException ioexception) {
                            ;
                        }

                        TCPSocketManager.this.setOutputStream(outputstream);
                        TCPSocketManager.this.socket = socket;
                        TCPSocketManager.this.connector = null;
                        this.shutdown = true;
                    }

                    TCPSocketManager.LOGGER.debug("Connection to " + TCPSocketManager.this.host + ":" + TCPSocketManager.this.port + " reestablished.");
                } catch (InterruptedException interruptedexception) {
                    TCPSocketManager.LOGGER.debug("Reconnection interrupted.");
                } catch (ConnectException connectexception) {
                    TCPSocketManager.LOGGER.debug(TCPSocketManager.this.host + ":" + TCPSocketManager.this.port + " refused connection");
                } catch (IOException ioexception1) {
                    TCPSocketManager.LOGGER.debug("Unable to reconnect to " + TCPSocketManager.this.host + ":" + TCPSocketManager.this.port);
                } finally {
                    this.latch.countDown();
                }
            }

        }
    }
}
