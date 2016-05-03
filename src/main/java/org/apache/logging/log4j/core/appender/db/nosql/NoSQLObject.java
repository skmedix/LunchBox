package org.apache.logging.log4j.core.appender.db.nosql;

public interface NoSQLObject {

    void set(String s, Object object);

    void set(String s, NoSQLObject nosqlobject);

    void set(String s, Object[] aobject);

    void set(String s, NoSQLObject[] anosqlobject);

    Object unwrap();
}
