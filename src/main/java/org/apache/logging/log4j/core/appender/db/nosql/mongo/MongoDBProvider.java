package org.apache.logging.log4j.core.appender.db.nosql.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLProvider;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "MongoDb",
    category = "Core",
    printObject = true
)
public final class MongoDBProvider implements NoSQLProvider {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private final String collectionName;
    private final DB database;
    private final String description;
    private final WriteConcern writeConcern;

    private MongoDBProvider(DB db, WriteConcern writeconcern, String s, String s1) {
        this.database = db;
        this.writeConcern = writeconcern;
        this.collectionName = s;
        this.description = "mongoDb{ " + s1 + " }";
    }

    public MongoDBConnection getConnection() {
        return new MongoDBConnection(this.database, this.writeConcern, this.collectionName);
    }

    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static MongoDBProvider createNoSQLProvider(@PluginAttribute("collectionName") String s, @PluginAttribute("writeConcernConstant") String s1, @PluginAttribute("writeConcernConstantClass") String s2, @PluginAttribute("databaseName") String s3, @PluginAttribute("server") String s4, @PluginAttribute("port") String s5, @PluginAttribute("username") String s6, @PluginAttribute("password") String s7, @PluginAttribute("factoryClassName") String s8, @PluginAttribute("factoryMethodName") String s9) {
        DB db;
        String s10;

        if (s8 != null && s8.length() > 0 && s9 != null && s9.length() > 0) {
            try {
                Class oclass = Class.forName(s8);
                Method method = oclass.getMethod(s9, new Class[0]);
                Object object = method.invoke((Object) null, new Object[0]);

                if (object instanceof DB) {
                    db = (DB) object;
                } else {
                    if (!(object instanceof MongoClient)) {
                        if (object == null) {
                            MongoDBProvider.LOGGER.error("The factory method [{}.{}()] returned null.", new Object[] { s8, s9});
                            return null;
                        }

                        MongoDBProvider.LOGGER.error("The factory method [{}.{}()] returned an unsupported type [{}].", new Object[] { s8, s9, object.getClass().getName()});
                        return null;
                    }

                    if (s3 == null || s3.length() <= 0) {
                        MongoDBProvider.LOGGER.error("The factory method [{}.{}()] returned a MongoClient so the database name is required.", new Object[] { s8, s9});
                        return null;
                    }

                    db = ((MongoClient) object).getDB(s3);
                }

                s10 = "database=" + db.getName();
                List list = db.getMongo().getAllAddress();

                if (list.size() == 1) {
                    s10 = s10 + ", server=" + ((ServerAddress) list.get(0)).getHost() + ", port=" + ((ServerAddress) list.get(0)).getPort();
                } else {
                    s10 = s10 + ", servers=[";

                    ServerAddress serveraddress;

                    for (Iterator iterator = list.iterator(); iterator.hasNext(); s10 = s10 + " { " + serveraddress.getHost() + ", " + serveraddress.getPort() + " } ") {
                        serveraddress = (ServerAddress) iterator.next();
                    }

                    s10 = s10 + "]";
                }
            } catch (ClassNotFoundException classnotfoundexception) {
                MongoDBProvider.LOGGER.error("The factory class [{}] could not be loaded.", new Object[] { s8, classnotfoundexception});
                return null;
            } catch (NoSuchMethodException nosuchmethodexception) {
                MongoDBProvider.LOGGER.error("The factory class [{}] does not have a no-arg method named [{}].", new Object[] { s8, s9, nosuchmethodexception});
                return null;
            } catch (Exception exception) {
                MongoDBProvider.LOGGER.error("The factory method [{}.{}()] could not be invoked.", new Object[] { s8, s9, exception});
                return null;
            }
        } else {
            if (s3 == null || s3.length() <= 0) {
                MongoDBProvider.LOGGER.error("No factory method was provided so the database name is required.");
                return null;
            }

            s10 = "database=" + s3;

            try {
                if (s4 != null && s4.length() > 0) {
                    int i = AbstractAppender.parseInt(s5, 0);

                    s10 = s10 + ", server=" + s4;
                    if (i > 0) {
                        s10 = s10 + ", port=" + i;
                        db = (new MongoClient(s4, i)).getDB(s3);
                    } else {
                        db = (new MongoClient(s4)).getDB(s3);
                    }
                } else {
                    db = (new MongoClient()).getDB(s3);
                }
            } catch (Exception exception1) {
                MongoDBProvider.LOGGER.error("Failed to obtain a database instance from the MongoClient at server [{}] and port [{}].", new Object[] { s4, s5});
                return null;
            }
        }

        if (!db.isAuthenticated()) {
            if (s6 == null || s6.length() <= 0 || s7 == null || s7.length() <= 0) {
                MongoDBProvider.LOGGER.error("The database is not already authenticated so you must supply a username and password for the MongoDB provider.");
                return null;
            }

            s10 = s10 + ", username=" + s6 + ", passwordHash=" + NameUtil.md5(s7 + MongoDBProvider.class.getName());
            MongoDBConnection.authenticate(db, s6, s7);
        }

        WriteConcern writeconcern;

        if (s1 != null && s1.length() > 0) {
            if (s2 != null && s2.length() > 0) {
                try {
                    Class oclass1 = Class.forName(s2);
                    Field field = oclass1.getField(s1);

                    writeconcern = (WriteConcern) field.get((Object) null);
                } catch (Exception exception2) {
                    MongoDBProvider.LOGGER.error("Write concern constant [{}.{}] not found, using default.", new Object[] { s2, s1});
                    writeconcern = WriteConcern.ACKNOWLEDGED;
                }
            } else {
                writeconcern = WriteConcern.valueOf(s1);
                if (writeconcern == null) {
                    MongoDBProvider.LOGGER.warn("Write concern constant [{}] not found, using default.", new Object[] { s1});
                    writeconcern = WriteConcern.ACKNOWLEDGED;
                }
            }
        } else {
            writeconcern = WriteConcern.ACKNOWLEDGED;
        }

        return new MongoDBProvider(db, writeconcern, s, s10);
    }
}
