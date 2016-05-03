package org.apache.logging.log4j.core.helpers;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public final class NetUtils {

    private static final Logger LOGGER = StatusLogger.getLogger();

    public static String getLocalHostname() {
        try {
            InetAddress inetaddress = InetAddress.getLocalHost();

            return inetaddress.getHostName();
        } catch (UnknownHostException unknownhostexception) {
            try {
                Enumeration enumeration = NetworkInterface.getNetworkInterfaces();

                while (enumeration.hasMoreElements()) {
                    NetworkInterface networkinterface = (NetworkInterface) enumeration.nextElement();
                    Enumeration enumeration1 = networkinterface.getInetAddresses();

                    while (enumeration1.hasMoreElements()) {
                        InetAddress inetaddress1 = (InetAddress) enumeration1.nextElement();

                        if (!inetaddress1.isLoopbackAddress()) {
                            String s = inetaddress1.getHostName();

                            if (s != null) {
                                return s;
                            }
                        }
                    }
                }
            } catch (SocketException socketexception) {
                NetUtils.LOGGER.error("Could not determine local host name", (Throwable) unknownhostexception);
                return "UNKNOWN_LOCALHOST";
            }

            NetUtils.LOGGER.error("Could not determine local host name", (Throwable) unknownhostexception);
            return "UNKNOWN_LOCALHOST";
        }
    }
}
