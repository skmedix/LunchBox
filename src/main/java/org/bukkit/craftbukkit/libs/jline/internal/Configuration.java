package org.bukkit.craftbukkit.libs.jline.internal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

public class Configuration {

    public static final String JLINE_CONFIGURATION = "org.bukkit.craftbukkit.libs.jline.configuration";
    public static final String JLINE_RC = ".org.bukkit.craftbukkit.libs.jline.rc";
    private static volatile Properties properties;

    private static Properties initProperties() {
        URL url = determineUrl();
        Properties props = new Properties();

        try {
            loadProperties(url, props);
        } catch (IOException ioexception) {
            Log.debug(new Object[] { "Unable to read configuration from: ", url, ioexception});
        }

        return props;
    }

    private static void loadProperties(URL url, Properties props) throws IOException {
        Log.debug(new Object[] { "Loading properties from: ", url});
        InputStream input = url.openStream();

        try {
            props.load(new BufferedInputStream(input));
        } finally {
            try {
                input.close();
            } catch (IOException ioexception) {
                ;
            }

        }

        if (Log.DEBUG) {
            Log.debug(new Object[] { "Loaded properties:"});
            Iterator i$ = props.entrySet().iterator();

            while (i$.hasNext()) {
                Entry entry = (Entry) i$.next();

                Log.debug(new Object[] { "  ", entry.getKey(), "=", entry.getValue()});
            }
        }

    }

    private static URL determineUrl() {
        String tmp = System.getProperty("org.bukkit.craftbukkit.libs.jline.configuration");

        if (tmp != null) {
            return Urls.create(tmp);
        } else {
            File file = new File(getUserHome(), ".org.bukkit.craftbukkit.libs.jline.rc");

            return Urls.create(file);
        }
    }

    public static void reset() {
        Log.debug(new Object[] { "Resetting"});
        Configuration.properties = null;
        getProperties();
    }

    public static Properties getProperties() {
        if (Configuration.properties == null) {
            Configuration.properties = initProperties();
        }

        return Configuration.properties;
    }

    public static String getString(String name, String defaultValue) {
        Preconditions.checkNotNull(name);
        String value = System.getProperty(name);

        if (value == null) {
            value = getProperties().getProperty(name);
            if (value == null) {
                value = defaultValue;
            }
        }

        return value;
    }

    public static String getString(String name) {
        return getString(name, (String) null);
    }

    public static boolean getBoolean(String name, boolean defaultValue) {
        String value = getString(name);

        return value == null ? defaultValue : value.length() == 0 || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("on") || value.equalsIgnoreCase("true");
    }

    public static int getInteger(String name, int defaultValue) {
        String str = getString(name);

        return str == null ? defaultValue : Integer.parseInt(str);
    }

    public static long getLong(String name, long defaultValue) {
        String str = getString(name);

        return str == null ? defaultValue : Long.parseLong(str);
    }

    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

    public static String getOsName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static boolean isWindows() {
        return getOsName().startsWith("windows");
    }

    public static String getFileEncoding() {
        return System.getProperty("file.encoding");
    }

    public static String getEncoding() {
        String envEncoding = extractEncodingFromCtype(System.getenv("LC_CTYPE"));

        return envEncoding != null ? envEncoding : System.getProperty("input.encoding", Charset.defaultCharset().name());
    }

    static String extractEncodingFromCtype(String ctype) {
        if (ctype != null && ctype.indexOf(46) > 0) {
            String encodingAndModifier = ctype.substring(ctype.indexOf(46) + 1);

            return encodingAndModifier.indexOf(64) > 0 ? encodingAndModifier.substring(0, encodingAndModifier.indexOf(64)) : encodingAndModifier;
        } else {
            return null;
        }
    }
}
