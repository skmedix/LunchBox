package org.apache.logging.log4j.core.appender.db.nosql;

import java.io.Closeable;

public interface NoSQLConnection extends Closeable {

    NoSQLObject createObject();

    NoSQLObject[] createList(int i);

    void insertObject(NoSQLObject nosqlobject);

    void close();

    boolean isClosed();
}
