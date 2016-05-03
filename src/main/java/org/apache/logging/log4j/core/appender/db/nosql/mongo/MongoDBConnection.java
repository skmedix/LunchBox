package org.apache.logging.log4j.core.appender.db.nosql.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;
import org.apache.logging.log4j.status.StatusLogger;
import org.bson.BSON;
import org.bson.Transformer;

public final class MongoDBConnection implements NoSQLConnection {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private final DBCollection collection;
    private final Mongo mongo;
    private final WriteConcern writeConcern;

    public MongoDBConnection(DB db, WriteConcern writeconcern, String s) {
        this.mongo = db.getMongo();
        this.collection = db.getCollection(s);
        this.writeConcern = writeconcern;
    }

    public MongoDBObject createObject() {
        return new MongoDBObject();
    }

    public MongoDBObject[] createList(int i) {
        return new MongoDBObject[i];
    }

    public void insertObject(NoSQLObject nosqlobject) {
        try {
            WriteResult writeresult = this.collection.insert((DBObject) nosqlobject.unwrap(), this.writeConcern);

            if (writeresult.getError() != null && writeresult.getError().length() > 0) {
                throw new AppenderLoggingException("Failed to write log event to MongoDB due to error: " + writeresult.getError() + ".");
            }
        } catch (MongoException mongoexception) {
            throw new AppenderLoggingException("Failed to write log event to MongoDB due to error: " + mongoexception.getMessage(), mongoexception);
        }
    }

    public void close() {
        this.mongo.close();
    }

    public boolean isClosed() {
        return !this.mongo.getConnector().isOpen();
    }

    static void authenticate(DB db, String s, String s1) {
        try {
            if (!db.authenticate(s, s1.toCharArray())) {
                MongoDBConnection.LOGGER.error("Failed to authenticate against MongoDB server. Unknown error.");
            }
        } catch (MongoException mongoexception) {
            MongoDBConnection.LOGGER.error("Failed to authenticate against MongoDB: " + mongoexception.getMessage(), (Throwable) mongoexception);
        } catch (IllegalStateException illegalstateexception) {
            MongoDBConnection.LOGGER.error("Factory-supplied MongoDB database connection already authenticated with differentcredentials but lost connection.");
        }

    }

    static {
        BSON.addDecodingHook(Level.class, new Transformer() {
            public Object transform(Object object) {
                return object instanceof Level ? ((Level) object).name() : object;
            }
        });
    }
}
