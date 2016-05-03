package org.apache.logging.log4j.core.appender.db.jdbc;

import java.io.Closeable;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import org.apache.logging.log4j.core.helpers.Closer;
import org.apache.logging.log4j.core.layout.PatternLayout;

public final class JDBCDatabaseManager extends AbstractDatabaseManager {

    private static final JDBCDatabaseManager.JDBCDatabaseManagerFactory FACTORY = new JDBCDatabaseManager.JDBCDatabaseManagerFactory((JDBCDatabaseManager.SyntheticClass_1) null);
    private final List columns;
    private final ConnectionSource connectionSource;
    private final String sqlStatement;
    private Connection connection;
    private PreparedStatement statement;

    private JDBCDatabaseManager(String s, int i, ConnectionSource connectionsource, String s1, List list) {
        super(s, i);
        this.connectionSource = connectionsource;
        this.sqlStatement = s1;
        this.columns = list;
    }

    protected void connectInternal() throws SQLException {
        this.connection = this.connectionSource.getConnection();
        this.statement = this.connection.prepareStatement(this.sqlStatement);
    }

    protected void disconnectInternal() throws SQLException {
        try {
            Closer.close((Statement) this.statement);
        } finally {
            Closer.close(this.connection);
        }

    }

    protected void writeInternal(LogEvent logevent) {
        StringReader stringreader = null;

        try {
            if (!this.isConnected() || this.connection == null || this.connection.isClosed()) {
                throw new AppenderLoggingException("Cannot write logging event; JDBC manager not connected to the database.");
            } else {
                int i = 1;
                Iterator iterator = this.columns.iterator();

                while (iterator.hasNext()) {
                    JDBCDatabaseManager.Column jdbcdatabasemanager_column = (JDBCDatabaseManager.Column) iterator.next();

                    if (jdbcdatabasemanager_column.isEventTimestamp) {
                        this.statement.setTimestamp(i++, new Timestamp(logevent.getMillis()));
                    } else if (jdbcdatabasemanager_column.isClob) {
                        stringreader = new StringReader(jdbcdatabasemanager_column.layout.toSerializable(logevent));
                        if (jdbcdatabasemanager_column.isUnicode) {
                            this.statement.setNClob(i++, stringreader);
                        } else {
                            this.statement.setClob(i++, stringreader);
                        }
                    } else if (jdbcdatabasemanager_column.isUnicode) {
                        this.statement.setNString(i++, jdbcdatabasemanager_column.layout.toSerializable(logevent));
                    } else {
                        this.statement.setString(i++, jdbcdatabasemanager_column.layout.toSerializable(logevent));
                    }
                }

                if (this.statement.executeUpdate() == 0) {
                    throw new AppenderLoggingException("No records inserted in database table for log event in JDBC manager.");
                }
            }
        } catch (SQLException sqlexception) {
            throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + sqlexception.getMessage(), sqlexception);
        } finally {
            Closer.closeSilent((Closeable) stringreader);
        }
    }

    public static JDBCDatabaseManager getJDBCDatabaseManager(String s, int i, ConnectionSource connectionsource, String s1, ColumnConfig[] acolumnconfig) {
        return (JDBCDatabaseManager) AbstractDatabaseManager.getManager(s, (AbstractDatabaseManager.AbstractFactoryData) (new JDBCDatabaseManager.FactoryData(i, connectionsource, s1, acolumnconfig)), (ManagerFactory) JDBCDatabaseManager.FACTORY);
    }

    JDBCDatabaseManager(String s, int i, ConnectionSource connectionsource, String s1, List list, JDBCDatabaseManager.SyntheticClass_1 jdbcdatabasemanager_syntheticclass_1) {
        this(s, i, connectionsource, s1, list);
    }

    static class SyntheticClass_1 {    }

    private static final class Column {

        private final PatternLayout layout;
        private final boolean isEventTimestamp;
        private final boolean isUnicode;
        private final boolean isClob;

        private Column(PatternLayout patternlayout, boolean flag, boolean flag1, boolean flag2) {
            this.layout = patternlayout;
            this.isEventTimestamp = flag;
            this.isUnicode = flag1;
            this.isClob = flag2;
        }

        Column(PatternLayout patternlayout, boolean flag, boolean flag1, boolean flag2, JDBCDatabaseManager.SyntheticClass_1 jdbcdatabasemanager_syntheticclass_1) {
            this(patternlayout, flag, flag1, flag2);
        }
    }

    private static final class JDBCDatabaseManagerFactory implements ManagerFactory {

        private JDBCDatabaseManagerFactory() {}

        public JDBCDatabaseManager createManager(String s, JDBCDatabaseManager.FactoryData jdbcdatabasemanager_factorydata) {
            StringBuilder stringbuilder = new StringBuilder();
            StringBuilder stringbuilder1 = new StringBuilder();
            ArrayList arraylist = new ArrayList();
            int i = 0;
            ColumnConfig[] acolumnconfig = jdbcdatabasemanager_factorydata.columnConfigs;
            int j = acolumnconfig.length;

            for (int k = 0; k < j; ++k) {
                ColumnConfig columnconfig = acolumnconfig[k];

                if (i++ > 0) {
                    stringbuilder.append(',');
                    stringbuilder1.append(',');
                }

                stringbuilder.append(columnconfig.getColumnName());
                if (columnconfig.getLiteralValue() != null) {
                    stringbuilder1.append(columnconfig.getLiteralValue());
                } else {
                    arraylist.add(new JDBCDatabaseManager.Column(columnconfig.getLayout(), columnconfig.isEventTimestamp(), columnconfig.isUnicode(), columnconfig.isClob(), (JDBCDatabaseManager.SyntheticClass_1) null));
                    stringbuilder1.append('?');
                }
            }

            String s1 = "INSERT INTO " + jdbcdatabasemanager_factorydata.tableName + " (" + stringbuilder + ") VALUES (" + stringbuilder1 + ")";

            return new JDBCDatabaseManager(s, jdbcdatabasemanager_factorydata.getBufferSize(), jdbcdatabasemanager_factorydata.connectionSource, s1, arraylist, (JDBCDatabaseManager.SyntheticClass_1) null);
        }

        JDBCDatabaseManagerFactory(JDBCDatabaseManager.SyntheticClass_1 jdbcdatabasemanager_syntheticclass_1) {
            this();
        }
    }

    private static final class FactoryData extends AbstractDatabaseManager.AbstractFactoryData {

        private final ColumnConfig[] columnConfigs;
        private final ConnectionSource connectionSource;
        private final String tableName;

        protected FactoryData(int i, ConnectionSource connectionsource, String s, ColumnConfig[] acolumnconfig) {
            super(i);
            this.connectionSource = connectionsource;
            this.tableName = s;
            this.columnConfigs = acolumnconfig;
        }
    }
}
