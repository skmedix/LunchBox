package org.apache.logging.log4j.core.appender.db.nosql.couch;

import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;

public final class CouchDBConnection implements NoSQLConnection {

    private final CouchDbClient client;
    private boolean closed = false;

    public CouchDBConnection(CouchDbClient couchdbclient) {
        this.client = couchdbclient;
    }

    public CouchDBObject createObject() {
        return new CouchDBObject();
    }

    public CouchDBObject[] createList(int i) {
        return new CouchDBObject[i];
    }

    public void insertObject(NoSQLObject nosqlobject) {
        try {
            Response response = this.client.save(nosqlobject.unwrap());

            if (response.getError() != null && response.getError().length() > 0) {
                throw new AppenderLoggingException("Failed to write log event to CouchDB due to error: " + response.getError() + ".");
            }
        } catch (Exception exception) {
            throw new AppenderLoggingException("Failed to write log event to CouchDB due to error: " + exception.getMessage(), exception);
        }
    }

    public synchronized void close() {
        this.closed = true;
        this.client.shutdown();
    }

    public synchronized boolean isClosed() {
        return this.closed;
    }
}
