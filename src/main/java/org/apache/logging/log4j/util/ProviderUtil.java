package org.apache.logging.log4j.util;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.status.StatusLogger;

public final class ProviderUtil {

    private static final String PROVIDER_RESOURCE = "META-INF/log4j-provider.properties";
    private static final String API_VERSION = "Log4jAPIVersion";
    private static final String[] COMPATIBLE_API_VERSIONS = new String[] { "2.0.0"};
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final List PROVIDERS = new ArrayList();

    public static Iterator getProviders() {
        return ProviderUtil.PROVIDERS.iterator();
    }

    public static boolean hasProviders() {
        return ProviderUtil.PROVIDERS.size() > 0;
    }

    public static ClassLoader findClassLoader() {
        ClassLoader classloader;

        if (System.getSecurityManager() == null) {
            classloader = Thread.currentThread().getContextClassLoader();
        } else {
            classloader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
                public ClassLoader run() {
                    return Thread.currentThread().getContextClassLoader();
                }
            });
        }

        if (classloader == null) {
            classloader = ProviderUtil.class.getClassLoader();
        }

        return classloader;
    }

    private static boolean validVersion(String s) {
        String[] astring = ProviderUtil.COMPATIBLE_API_VERSIONS;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s1 = astring[j];

            if (s.startsWith(s1)) {
                return true;
            }
        }

        return false;
    }

    static {
        ClassLoader classloader = findClassLoader();
        Enumeration enumeration = null;

        try {
            enumeration = classloader.getResources("META-INF/log4j-provider.properties");
        } catch (IOException ioexception) {
            ProviderUtil.LOGGER.fatal("Unable to locate META-INF/log4j-provider.properties", (Throwable) ioexception);
        }

        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                URL url = (URL) enumeration.nextElement();

                try {
                    Properties properties = PropertiesUtil.loadClose(url.openStream(), url);

                    if (validVersion(properties.getProperty("Log4jAPIVersion"))) {
                        ProviderUtil.PROVIDERS.add(new Provider(properties, url));
                    }
                } catch (IOException ioexception1) {
                    ProviderUtil.LOGGER.error("Unable to open " + url.toString(), (Throwable) ioexception1);
                }
            }
        }

    }
}
