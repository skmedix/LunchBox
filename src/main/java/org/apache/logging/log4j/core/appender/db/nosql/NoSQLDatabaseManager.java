package org.apache.logging.log4j.core.appender.db.nosql;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;

public final class NoSQLDatabaseManager extends AbstractDatabaseManager {

    private static final NoSQLDatabaseManager.NoSQLDatabaseManagerFactory FACTORY = new NoSQLDatabaseManager.NoSQLDatabaseManagerFactory((NoSQLDatabaseManager.SyntheticClass_1) null);
    private final NoSQLProvider provider;
    private NoSQLConnection connection;

    private NoSQLDatabaseManager(String s, int i, NoSQLProvider nosqlprovider) {
        super(s, i);
        this.provider = nosqlprovider;
    }

    protected void connectInternal() {
        this.connection = this.provider.getConnection();
    }

    protected void disconnectInternal() {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }

    }

    protected void writeInternal(LogEvent logevent) {
        if (this.isConnected() && this.connection != null && !this.connection.isClosed()) {
            NoSQLObject nosqlobject = this.connection.createObject();

            nosqlobject.set("level", (Object) logevent.getLevel());
            nosqlobject.set("loggerName", (Object) logevent.getLoggerName());
            nosqlobject.set("message", (Object) (logevent.getMessage() == null ? null : logevent.getMessage().getFormattedMessage()));
            StackTraceElement stacktraceelement = logevent.getSource();

            if (stacktraceelement == null) {
                nosqlobject.set("source", (Object) null);
            } else {
                nosqlobject.set("source", this.convertStackTraceElement(stacktraceelement));
            }

            Marker marker = logevent.getMarker();
            NoSQLObject nosqlobject1;
            NoSQLObject nosqlobject2;

            if (marker == null) {
                nosqlobject.set("marker", (Object) null);
            } else {
                NoSQLObject nosqlobject3 = this.connection.createObject();

                nosqlobject1 = nosqlobject3;
                nosqlobject3.set("name", (Object) marker.getName());

                while (marker.getParent() != null) {
                    marker = marker.getParent();
                    nosqlobject2 = this.connection.createObject();
                    nosqlobject2.set("name", (Object) marker.getName());
                    nosqlobject1.set("parent", nosqlobject2);
                    nosqlobject1 = nosqlobject2;
                }

                nosqlobject.set("marker", nosqlobject3);
            }

            nosqlobject.set("threadName", (Object) logevent.getThreadName());
            nosqlobject.set("millis", (Object) Long.valueOf(logevent.getMillis()));
            nosqlobject.set("date", (Object) (new Date(logevent.getMillis())));
            Throwable throwable = logevent.getThrown();

            if (throwable == null) {
                nosqlobject.set("thrown", (Object) null);
            } else {
                nosqlobject1 = this.connection.createObject();
                nosqlobject2 = nosqlobject1;
                nosqlobject1.set("type", (Object) throwable.getClass().getName());
                nosqlobject1.set("message", (Object) throwable.getMessage());
                nosqlobject1.set("stackTrace", this.convertStackTrace(throwable.getStackTrace()));

                while (throwable.getCause() != null) {
                    throwable = throwable.getCause();
                    NoSQLObject nosqlobject4 = this.connection.createObject();

                    nosqlobject4.set("type", (Object) throwable.getClass().getName());
                    nosqlobject4.set("message", (Object) throwable.getMessage());
                    nosqlobject4.set("stackTrace", this.convertStackTrace(throwable.getStackTrace()));
                    nosqlobject2.set("cause", nosqlobject4);
                    nosqlobject2 = nosqlobject4;
                }

                nosqlobject.set("thrown", nosqlobject1);
            }

            Map map = logevent.getContextMap();

            if (map == null) {
                nosqlobject.set("contextMap", (Object) null);
            } else {
                nosqlobject2 = this.connection.createObject();
                Iterator iterator = map.entrySet().iterator();

                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();

                    nosqlobject2.set((String) entry.getKey(), entry.getValue());
                }

                nosqlobject.set("contextMap", nosqlobject2);
            }

            ThreadContext.ContextStack threadcontext_contextstack = logevent.getContextStack();

            if (threadcontext_contextstack == null) {
                nosqlobject.set("contextStack", (Object) null);
            } else {
                nosqlobject.set("contextStack", threadcontext_contextstack.asList().toArray());
            }

            this.connection.insertObject(nosqlobject);
        } else {
            throw new AppenderLoggingException("Cannot write logging event; NoSQL manager not connected to the database.");
        }
    }

    private NoSQLObject[] convertStackTrace(StackTraceElement[] astacktraceelement) {
        NoSQLObject[] anosqlobject = this.connection.createList(astacktraceelement.length);

        for (int i = 0; i < astacktraceelement.length; ++i) {
            anosqlobject[i] = this.convertStackTraceElement(astacktraceelement[i]);
        }

        return anosqlobject;
    }

    private NoSQLObject convertStackTraceElement(StackTraceElement stacktraceelement) {
        NoSQLObject nosqlobject = this.connection.createObject();

        nosqlobject.set("className", (Object) stacktraceelement.getClassName());
        nosqlobject.set("methodName", (Object) stacktraceelement.getMethodName());
        nosqlobject.set("fileName", (Object) stacktraceelement.getFileName());
        nosqlobject.set("lineNumber", (Object) Integer.valueOf(stacktraceelement.getLineNumber()));
        return nosqlobject;
    }

    public static NoSQLDatabaseManager getNoSQLDatabaseManager(String s, int i, NoSQLProvider nosqlprovider) {
        return (NoSQLDatabaseManager) AbstractDatabaseManager.getManager(s, (AbstractDatabaseManager.AbstractFactoryData) (new NoSQLDatabaseManager.FactoryData(i, nosqlprovider)), (ManagerFactory) NoSQLDatabaseManager.FACTORY);
    }

    NoSQLDatabaseManager(String s, int i, NoSQLProvider nosqlprovider, NoSQLDatabaseManager.SyntheticClass_1 nosqldatabasemanager_syntheticclass_1) {
        this(s, i, nosqlprovider);
    }

    static class SyntheticClass_1 {    }

    private static final class NoSQLDatabaseManagerFactory implements ManagerFactory {

        private NoSQLDatabaseManagerFactory() {}

        public NoSQLDatabaseManager createManager(String s, NoSQLDatabaseManager.FactoryData nosqldatabasemanager_factorydata) {
            return new NoSQLDatabaseManager(s, nosqldatabasemanager_factorydata.getBufferSize(), nosqldatabasemanager_factorydata.provider, (NoSQLDatabaseManager.SyntheticClass_1) null);
        }

        NoSQLDatabaseManagerFactory(NoSQLDatabaseManager.SyntheticClass_1 nosqldatabasemanager_syntheticclass_1) {
            this();
        }
    }

    private static final class FactoryData extends AbstractDatabaseManager.AbstractFactoryData {

        private final NoSQLProvider provider;

        protected FactoryData(int i, NoSQLProvider nosqlprovider) {
            super(i);
            this.provider = nosqlprovider;
        }
    }
}
