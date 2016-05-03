package org.apache.logging.log4j.core.appender.db.nosql.couch;

import java.lang.reflect.Method;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLProvider;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;

@Plugin(
    name = "CouchDb",
    category = "Core",
    printObject = true
)
public final class CouchDBProvider implements NoSQLProvider {

    private static final int HTTP = 80;
    private static final int HTTPS = 443;
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final CouchDbClient client;
    private final String description;

    private CouchDBProvider(CouchDbClient couchdbclient, String s) {
        this.client = couchdbclient;
        this.description = "couchDb{ " + s + " }";
    }

    public CouchDBConnection getConnection() {
        return new CouchDBConnection(this.client);
    }

    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static CouchDBProvider createNoSQLProvider(@PluginAttribute("databaseName") String s, @PluginAttribute("protocol") String s1, @PluginAttribute("server") String s2, @PluginAttribute("port") String s3, @PluginAttribute("username") String s4, @PluginAttribute("password") String s5, @PluginAttribute("factoryClassName") String s6, @PluginAttribute("factoryMethodName") String s7) {
        CouchDbClient couchdbclient;
        String s8;

        if (s6 != null && s6.length() > 0 && s7 != null && s7.length() > 0) {
            try {
                Class oclass = Class.forName(s6);
                Method method = oclass.getMethod(s7, new Class[0]);
                Object object = method.invoke((Object) null, new Object[0]);

                if (object instanceof CouchDbClient) {
                    couchdbclient = (CouchDbClient) object;
                    s8 = "uri=" + couchdbclient.getDBUri();
                } else {
                    if (!(object instanceof CouchDbProperties)) {
                        if (object == null) {
                            CouchDBProvider.LOGGER.error("The factory method [{}.{}()] returned null.", new Object[] { s6, s7});
                            return null;
                        }

                        CouchDBProvider.LOGGER.error("The factory method [{}.{}()] returned an unsupported type [{}].", new Object[] { s6, s7, object.getClass().getName()});
                        return null;
                    }

                    CouchDbProperties couchdbproperties = (CouchDbProperties) object;

                    couchdbclient = new CouchDbClient(couchdbproperties);
                    s8 = "uri=" + couchdbclient.getDBUri() + ", username=" + couchdbproperties.getUsername() + ", passwordHash=" + NameUtil.md5(s5 + CouchDBProvider.class.getName()) + ", maxConnections=" + couchdbproperties.getMaxConnections() + ", connectionTimeout=" + couchdbproperties.getConnectionTimeout() + ", socketTimeout=" + couchdbproperties.getSocketTimeout();
                }
            } catch (ClassNotFoundException classnotfoundexception) {
                CouchDBProvider.LOGGER.error("The factory class [{}] could not be loaded.", new Object[] { s6, classnotfoundexception});
                return null;
            } catch (NoSuchMethodException nosuchmethodexception) {
                CouchDBProvider.LOGGER.error("The factory class [{}] does not have a no-arg method named [{}].", new Object[] { s6, s7, nosuchmethodexception});
                return null;
            } catch (Exception exception) {
                CouchDBProvider.LOGGER.error("The factory method [{}.{}()] could not be invoked.", new Object[] { s6, s7, exception});
                return null;
            }
        } else {
            if (s == null || s.length() <= 0) {
                CouchDBProvider.LOGGER.error("No factory method was provided so the database name is required.");
                return null;
            }

            if (s1 != null && s1.length() > 0) {
                s1 = s1.toLowerCase();
                if (!s1.equals("http") && !s1.equals("https")) {
                    CouchDBProvider.LOGGER.error("Only protocols [http] and [https] are supported, [{}] specified.", new Object[] { s1});
                    return null;
                }
            } else {
                s1 = "http";
                CouchDBProvider.LOGGER.warn("No protocol specified, using default port [http].");
            }

            int i = AbstractAppender.parseInt(s3, s1.equals("https") ? 443 : 80);

            if (Strings.isEmpty(s2)) {
                s2 = "localhost";
                CouchDBProvider.LOGGER.warn("No server specified, using default server localhost.");
            }

            if (Strings.isEmpty(s4) || Strings.isEmpty(s5)) {
                CouchDBProvider.LOGGER.error("You must provide a username and password for the CouchDB provider.");
                return null;
            }

            couchdbclient = new CouchDbClient(s, false, s1, s2, i, s4, s5);
            s8 = "uri=" + couchdbclient.getDBUri() + ", username=" + s4 + ", passwordHash=" + NameUtil.md5(s5 + CouchDBProvider.class.getName());
        }

        return new CouchDBProvider(couchdbclient, s8);
    }
}
