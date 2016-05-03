package org.apache.logging.log4j.core.lookup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.status.StatusLogger;

public class Interpolator implements StrLookup {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final char PREFIX_SEPARATOR = ':';
    private final Map lookups = new HashMap();
    private final StrLookup defaultLookup;

    public Interpolator(StrLookup strlookup) {
        this.defaultLookup = (StrLookup) (strlookup == null ? new MapLookup(new HashMap()) : strlookup);
        PluginManager pluginmanager = new PluginManager("Lookup");

        pluginmanager.collectPlugins();
        Map map = pluginmanager.getPlugins();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            Class oclass = ((PluginType) entry.getValue()).getPluginClass();

            try {
                this.lookups.put(entry.getKey(), oclass.newInstance());
            } catch (Exception exception) {
                Interpolator.LOGGER.error("Unable to create Lookup for " + (String) entry.getKey(), (Throwable) exception);
            }
        }

    }

    public Interpolator() {
        this.defaultLookup = new MapLookup(new HashMap());
        this.lookups.put("sys", new SystemPropertiesLookup());
        this.lookups.put("env", new EnvironmentLookup());
        this.lookups.put("jndi", new JndiLookup());

        try {
            if (Class.forName("javax.servlet.ServletContext") != null) {
                this.lookups.put("web", new WebLookup());
            }
        } catch (ClassNotFoundException classnotfoundexception) {
            Interpolator.LOGGER.debug("ServletContext not present - WebLookup not added");
        } catch (Exception exception) {
            Interpolator.LOGGER.error("Unable to locate ServletContext", (Throwable) exception);
        }

    }

    public String lookup(String s) {
        return this.lookup((LogEvent) null, s);
    }

    public String lookup(LogEvent logevent, String s) {
        if (s == null) {
            return null;
        } else {
            int i = s.indexOf(58);

            if (i >= 0) {
                String s1 = s.substring(0, i);
                String s2 = s.substring(i + 1);
                StrLookup strlookup = (StrLookup) this.lookups.get(s1);
                String s3 = null;

                if (strlookup != null) {
                    s3 = logevent == null ? strlookup.lookup(s2) : strlookup.lookup(logevent, s2);
                }

                if (s3 != null) {
                    return s3;
                }

                s = s.substring(i + 1);
            }

            return this.defaultLookup != null ? (logevent == null ? this.defaultLookup.lookup(s) : this.defaultLookup.lookup(logevent, s)) : null;
        }
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        String s;

        for (Iterator iterator = this.lookups.keySet().iterator(); iterator.hasNext(); stringbuilder.append(s)) {
            s = (String) iterator.next();
            if (stringbuilder.length() == 0) {
                stringbuilder.append("{");
            } else {
                stringbuilder.append(", ");
            }
        }

        if (stringbuilder.length() > 0) {
            stringbuilder.append("}");
        }

        return stringbuilder.toString();
    }
}
