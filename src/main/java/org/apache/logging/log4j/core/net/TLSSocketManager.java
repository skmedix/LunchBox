package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.ssl.SSLConfiguration;

public class TLSSocketManager extends TCPSocketManager {

    public static final int DEFAULT_PORT = 6514;
    private static final TLSSocketManager.TLSSocketManagerFactory FACTORY = new TLSSocketManager.TLSSocketManagerFactory((TLSSocketManager.SyntheticClass_1) null);
    private SSLConfiguration sslConfig;

    public TLSSocketManager(String s, OutputStream outputstream, Socket socket, SSLConfiguration sslconfiguration, InetAddress inetaddress, String s1, int i, int j, boolean flag, Layout layout) {
        super(s, outputstream, socket, inetaddress, s1, i, j, flag, layout);
        this.sslConfig = sslconfiguration;
    }

    public static TLSSocketManager getSocketManager(SSLConfiguration sslconfiguration, String s, int i, int j, boolean flag, Layout layout) {
        if (Strings.isEmpty(s)) {
            throw new IllegalArgumentException("A host name is required");
        } else {
            if (i <= 0) {
                i = 6514;
            }

            if (j == 0) {
                j = 30000;
            }

            return (TLSSocketManager) getManager("TLS:" + s + ":" + i, (Object) (new TLSSocketManager.TLSFactoryData(sslconfiguration, s, i, j, flag, layout)), (ManagerFactory) TLSSocketManager.FACTORY);
        }
    }

    protected Socket createSocket(String s, int i) throws IOException {
        SSLSocketFactory sslsocketfactory = createSSLSocketFactory(this.sslConfig);

        return sslsocketfactory.createSocket(s, i);
    }

    private static SSLSocketFactory createSSLSocketFactory(SSLConfiguration sslconfiguration) {
        SSLSocketFactory sslsocketfactory;

        if (sslconfiguration != null) {
            sslsocketfactory = sslconfiguration.getSSLSocketFactory();
        } else {
            sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        }

        return sslsocketfactory;
    }

    static class SyntheticClass_1 {    }

    private static class TLSSocketManagerFactory implements ManagerFactory {

        private TLSSocketManagerFactory() {}

        public TLSSocketManager createManager(String s, TLSSocketManager.TLSFactoryData tlssocketmanager_tlsfactorydata) {
            InetAddress inetaddress = null;
            Object object = null;
            Socket socket = null;

            try {
                inetaddress = this.resolveAddress(tlssocketmanager_tlsfactorydata.host);
                socket = this.createSocket(tlssocketmanager_tlsfactorydata);
                object = socket.getOutputStream();
                this.checkDelay(tlssocketmanager_tlsfactorydata.delay, (OutputStream) object);
            } catch (IOException ioexception) {
                TLSSocketManager.LOGGER.error("TLSSocketManager (" + s + ") " + ioexception);
                object = new ByteArrayOutputStream();
            } catch (TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactory$TLSSocketManagerFactoryException tlssocketmanager_tlssocketmanagerfactory_tlssocketmanagerfactory$tlssocketmanagerfactoryexception) {
                return null;
            }

            return this.createManager(s, (OutputStream) object, socket, tlssocketmanager_tlsfactorydata.sslConfig, inetaddress, tlssocketmanager_tlsfactorydata.host, tlssocketmanager_tlsfactorydata.port, tlssocketmanager_tlsfactorydata.delay, tlssocketmanager_tlsfactorydata.immediateFail, tlssocketmanager_tlsfactorydata.layout);
        }

        private InetAddress resolveAddress(String s) throws TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactory$TLSSocketManagerFactoryException {
            try {
                InetAddress inetaddress = InetAddress.getByName(s);

                return inetaddress;
            } catch (UnknownHostException unknownhostexception) {
                TLSSocketManager.LOGGER.error("Could not find address of " + s, (Throwable) unknownhostexception);
                throw new TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactory$TLSSocketManagerFactoryException((TLSSocketManager.SyntheticClass_1) null);
            }
        }

        private void checkDelay(int i, OutputStream outputstream) throws TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactory$TLSSocketManagerFactoryException {
            if (i == 0 && outputstream == null) {
                throw new TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactory$TLSSocketManagerFactoryException((TLSSocketManager.SyntheticClass_1) null);
            }
        }

        private Socket createSocket(TLSSocketManager.TLSFactoryData tlssocketmanager_tlsfactorydata) throws IOException {
            SSLSocketFactory sslsocketfactory = TLSSocketManager.createSSLSocketFactory(tlssocketmanager_tlsfactorydata.sslConfig);
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(tlssocketmanager_tlsfactorydata.host, tlssocketmanager_tlsfactorydata.port);

            return sslsocket;
        }

        private TLSSocketManager createManager(String s, OutputStream outputstream, Socket socket, SSLConfiguration sslconfiguration, InetAddress inetaddress, String s1, int i, int j, boolean flag, Layout layout) {
            return new TLSSocketManager(s, outputstream, socket, sslconfiguration, inetaddress, s1, i, j, flag, layout);
        }

        TLSSocketManagerFactory(TLSSocketManager.SyntheticClass_1 tlssocketmanager_syntheticclass_1) {
            this();
        }

        private class TLSSocketManagerFactory$TLSSocketManagerFactoryException extends Exception {

            private TLSSocketManagerFactory$TLSSocketManagerFactoryException() {}

            TLSSocketManagerFactory$TLSSocketManagerFactoryException(TLSSocketManager.SyntheticClass_1 tlssocketmanager_syntheticclass_1) {
                this();
            }
        }
    }

    private static class TLSFactoryData {

        protected SSLConfiguration sslConfig;
        private final String host;
        private final int port;
        private final int delay;
        private final boolean immediateFail;
        private final Layout layout;

        public TLSFactoryData(SSLConfiguration sslconfiguration, String s, int i, int j, boolean flag, Layout layout) {
            this.host = s;
            this.port = i;
            this.delay = j;
            this.immediateFail = flag;
            this.layout = layout;
            this.sslConfig = sslconfiguration;
        }
    }
}
