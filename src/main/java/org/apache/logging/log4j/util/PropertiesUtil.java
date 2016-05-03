package org.apache.logging.log4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class PropertiesUtil {

    private static final PropertiesUtil LOG4J_PROPERTIES = new PropertiesUtil("log4j2.component.properties");
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final Properties props;

    public PropertiesUtil(Properties properties) {
        this.props = properties;
    }

    static Properties loadClose(InputStream inputstream, Object object) {
        Properties properties = new Properties();

        if (null != inputstream) {
            try {
                properties.load(inputstream);
            } catch (IOException ioexception) {
                PropertiesUtil.LOGGER.error("Unable to read " + object, (Throwable) ioexception);
            } finally {
                try {
                    inputstream.close();
                } catch (IOException ioexception1) {
                    PropertiesUtil.LOGGER.error("Unable to close " + object, (Throwable) ioexception1);
                }

            }
        }

        return properties;
    }

    public PropertiesUtil(String s) {
        ClassLoader classloader = ProviderUtil.findClassLoader();
        InputStream inputstream = classloader.getResourceAsStream(s);

        this.props = loadClose(inputstream, s);
    }

    public static PropertiesUtil getProperties() {
        return PropertiesUtil.LOG4J_PROPERTIES;
    }

    public String getStringProperty(String s) {
        String s1 = null;

        try {
            s1 = System.getProperty(s);
        } catch (SecurityException securityexception) {
            ;
        }

        return s1 == null ? this.props.getProperty(s) : s1;
    }

    public int getIntegerProperty(String s, int i) {
        String s1 = null;

        try {
            s1 = System.getProperty(s);
        } catch (SecurityException securityexception) {
            ;
        }

        if (s1 == null) {
            s1 = this.props.getProperty(s);
        }

        if (s1 != null) {
            try {
                return Integer.parseInt(s1);
            } catch (Exception exception) {
                return i;
            }
        } else {
            return i;
        }
    }

    public long getLongProperty(String s, long i) {
        String s1 = null;

        try {
            s1 = System.getProperty(s);
        } catch (SecurityException securityexception) {
            ;
        }

        if (s1 == null) {
            s1 = this.props.getProperty(s);
        }

        if (s1 != null) {
            try {
                return Long.parseLong(s1);
            } catch (Exception exception) {
                return i;
            }
        } else {
            return i;
        }
    }

    public String getStringProperty(String s, String s1) {
        String s2 = this.getStringProperty(s);

        return s2 == null ? s1 : s2;
    }

    public boolean getBooleanProperty(String s) {
        return this.getBooleanProperty(s, false);
    }

    public boolean getBooleanProperty(String s, boolean flag) {
        String s1 = this.getStringProperty(s);

        return s1 == null ? flag : "true".equalsIgnoreCase(s1);
    }

    public static Properties getSystemProperties() {
        try {
            return new Properties(System.getProperties());
        } catch (SecurityException securityexception) {
            StatusLogger.getLogger().error("Unable to access system properties.");
            return new Properties();
        }
    }
}
