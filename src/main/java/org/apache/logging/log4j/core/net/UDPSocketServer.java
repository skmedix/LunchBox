package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractServer;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.XMLConfiguration;
import org.apache.logging.log4j.core.config.XMLConfigurationFactory;

public class UDPSocketServer extends AbstractServer implements Runnable {

    private final Logger logger;
    private static final int MAX_PORT = 65534;
    private volatile boolean isActive = true;
    private final DatagramSocket server;
    private final int maxBufferSize = 67584;

    public UDPSocketServer(int i) throws IOException {
        this.server = new DatagramSocket(i);
        this.logger = LogManager.getLogger(this.getClass().getName() + '.' + i);
    }

    public static void main(String[] astring) throws Exception {
        if (astring.length >= 1 && astring.length <= 2) {
            int i = Integer.parseInt(astring[0]);

            if (i > 0 && i < '\ufffe') {
                if (astring.length == 2 && astring[1].length() > 0) {
                    ConfigurationFactory.setConfigurationFactory(new UDPSocketServer.ServerConfigurationFactory(astring[1]));
                }

                UDPSocketServer udpsocketserver = new UDPSocketServer(i);
                Thread thread = new Thread(udpsocketserver);

                thread.start();
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));

                String s;

                do {
                    s = bufferedreader.readLine();
                } while (s != null && !s.equalsIgnoreCase("Quit") && !s.equalsIgnoreCase("Stop") && !s.equalsIgnoreCase("Exit"));

                udpsocketserver.shutdown();
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
                byte[] abyte = new byte[67584];
                DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length);

                this.server.receive(datagrampacket);
                ObjectInputStream objectinputstream = new ObjectInputStream(new ByteArrayInputStream(datagrampacket.getData(), datagrampacket.getOffset(), datagrampacket.getLength()));
                LogEvent logevent = (LogEvent) objectinputstream.readObject();

                if (logevent != null) {
                    this.log(logevent);
                }
            } catch (OptionalDataException optionaldataexception) {
                this.logger.error("OptionalDataException eof=" + optionaldataexception.eof + " length=" + optionaldataexception.length, (Throwable) optionaldataexception);
            } catch (ClassNotFoundException classnotfoundexception) {
                this.logger.error("Unable to locate LogEvent class", (Throwable) classnotfoundexception);
            } catch (EOFException eofexception) {
                this.logger.info("EOF encountered");
            } catch (IOException ioexception) {
                this.logger.error("Exception encountered on accept. Ignoring. Stack Trace :", (Throwable) ioexception);
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
}
