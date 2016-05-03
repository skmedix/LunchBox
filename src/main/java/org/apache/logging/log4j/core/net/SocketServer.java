package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractServer;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.XMLConfiguration;
import org.apache.logging.log4j.core.config.XMLConfigurationFactory;

public class SocketServer extends AbstractServer implements Runnable {

    private final Logger logger;
    private static final int MAX_PORT = 65534;
    private volatile boolean isActive = true;
    private final ServerSocket server;
    private final ConcurrentMap handlers = new ConcurrentHashMap();

    public SocketServer(int i) throws IOException {
        this.server = new ServerSocket(i);
        this.logger = LogManager.getLogger(this.getClass().getName() + '.' + i);
    }

    public static void main(String[] astring) throws Exception {
        if (astring.length >= 1 && astring.length <= 2) {
            int i = Integer.parseInt(astring[0]);

            if (i > 0 && i < '\ufffe') {
                if (astring.length == 2 && astring[1].length() > 0) {
                    ConfigurationFactory.setConfigurationFactory(new SocketServer.ServerConfigurationFactory(astring[1]));
                }

                SocketServer socketserver = new SocketServer(i);
                Thread thread = new Thread(socketserver);

                thread.start();
                Charset charset = Charset.defaultCharset();
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in, charset));

                String s;

                do {
                    s = bufferedreader.readLine();
                } while (s != null && !s.equalsIgnoreCase("Quit") && !s.equalsIgnoreCase("Stop") && !s.equalsIgnoreCase("Exit"));

                socketserver.shutdown();
                thread.join();
            } else {
                System.err.println("Invalid port number");
                printUsage();
            }
        } else {
            System.err.println("Incorrect number of arguments");
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Usage: ServerSocket port configFilePath");
    }

    public void shutdown() {
        this.isActive = false;
        Thread.currentThread().interrupt();
    }

    public void run() {
        while (this.isActive) {
            try {
                Socket socket = this.server.accept();

                socket.setSoLinger(true, 0);
                SocketServer.SocketHandler socketserver_sockethandler = new SocketServer.SocketHandler(socket);

                this.handlers.put(Long.valueOf(socketserver_sockethandler.getId()), socketserver_sockethandler);
                socketserver_sockethandler.start();
            } catch (IOException ioexception) {
                System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
                ioexception.printStackTrace();
            }
        }

        Iterator iterator = this.handlers.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            SocketServer.SocketHandler socketserver_sockethandler1 = (SocketServer.SocketHandler) entry.getValue();

            socketserver_sockethandler1.shutdown();

            try {
                socketserver_sockethandler1.join();
            } catch (InterruptedException interruptedexception) {
                ;
            }
        }

    }

    private static class ServerConfigurationFactory extends XMLConfigurationFactory {

        private final String path;

        public ServerConfigurationFactory(String s) {
            this.path = s;
        }

        public Configuration getConfiguration(String s, URI uri) {
            if (this.path != null && this.path.length() > 0) {
                File file = null;
                ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource = null;

                try {
                    file = new File(this.path);
                    FileInputStream fileinputstream = new FileInputStream(file);

                    configurationfactory_configurationsource = new ConfigurationFactory.ConfigurationSource(fileinputstream, file);
                } catch (FileNotFoundException filenotfoundexception) {
                    ;
                }

                if (configurationfactory_configurationsource == null) {
                    try {
                        URL url = new URL(this.path);

                        configurationfactory_configurationsource = new ConfigurationFactory.ConfigurationSource(url.openStream(), this.path);
                    } catch (MalformedURLException malformedurlexception) {
                        ;
                    } catch (IOException ioexception) {
                        ;
                    }
                }

                try {
                    if (configurationfactory_configurationsource != null) {
                        return new XMLConfiguration(configurationfactory_configurationsource);
                    }
                } catch (Exception exception) {
                    ;
                }

                System.err.println("Unable to process configuration at " + this.path + ", using default.");
            }

            return super.getConfiguration(s, uri);
        }
    }

    private class SocketHandler extends Thread {

        private final ObjectInputStream ois;
        private boolean shutdown = false;

        public SocketHandler(Socket socket) throws IOException {
            this.ois = new ObjectInputStream(socket.getInputStream());
        }

        public void shutdown() {
            this.shutdown = true;
            this.interrupt();
        }

        public void run() {
            boolean flag = false;

            try {
                try {
                    while (!this.shutdown) {
                        LogEvent logevent = (LogEvent) this.ois.readObject();

                        if (logevent != null) {
                            SocketServer.this.log(logevent);
                        }
                    }
                } catch (EOFException eofexception) {
                    flag = true;
                } catch (OptionalDataException optionaldataexception) {
                    SocketServer.this.logger.error("OptionalDataException eof=" + optionaldataexception.eof + " length=" + optionaldataexception.length, (Throwable) optionaldataexception);
                } catch (ClassNotFoundException classnotfoundexception) {
                    SocketServer.this.logger.error("Unable to locate LogEvent class", (Throwable) classnotfoundexception);
                } catch (IOException ioexception) {
                    SocketServer.this.logger.error("IOException encountered while reading from socket", (Throwable) ioexception);
                }

                if (!flag) {
                    try {
                        this.ois.close();
                    } catch (Exception exception) {
                        ;
                    }
                }
            } finally {
                SocketServer.this.handlers.remove(Long.valueOf(this.getId()));
            }

        }
    }
}
