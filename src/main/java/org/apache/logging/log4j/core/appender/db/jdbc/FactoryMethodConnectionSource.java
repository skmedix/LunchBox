package org.apache.logging.log4j.core.appender.db.jdbc;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "ConnectionFactory",
    category = "Core",
    elementType = "connectionSource",
    printObject = true
)
public final class FactoryMethodConnectionSource implements ConnectionSource {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private final DataSource dataSource;
    private final String description;

    private FactoryMethodConnectionSource(DataSource datasource, String s, String s1, String s2) {
        this.dataSource = datasource;
        this.description = "factory{ public static " + s2 + " " + s + "." + s1 + "() }";
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static FactoryMethodConnectionSource createConnectionSource(@PluginAttribute("class") String s, @PluginAttribute("method") String s1) {
        if (!Strings.isEmpty(s) && !Strings.isEmpty(s1)) {
            Class oclass;
            final Method method;

            try {
                oclass = Class.forName(s);
                method = oclass.getMethod(s1, new Class[0]);
            } catch (Exception exception) {
                FactoryMethodConnectionSource.LOGGER.error(exception.toString(), (Throwable) exception);
                return null;
            }

            oclass = method.getReturnType();
            String s2 = oclass.getName();
            DataSource datasource;

            if (oclass == DataSource.class) {
                try {
                    datasource = (DataSource) method.invoke((Object) null, new Object[0]);
                    s2 = s2 + "[" + datasource + "]";
                } catch (Exception exception1) {
                    FactoryMethodConnectionSource.LOGGER.error(exception1.toString(), (Throwable) exception1);
                    return null;
                }
            } else {
                if (oclass != Connection.class) {
                    FactoryMethodConnectionSource.LOGGER.error("Method [{}.{}()] returns unsupported type [{}].", new Object[] { s, s1, oclass.getName()});
                    return null;
                }

                datasource = new DataSource() {
                    public Connection getConnection() throws SQLException {
                        try {
                            return (Connection) method.invoke((Object) null, new Object[0]);
                        } catch (Exception exception) {
                            throw new SQLException("Failed to obtain connection from factory method.", exception);
                        }
                    }

                    public Connection getConnection(String s, String s1) throws SQLException {
                        throw new UnsupportedOperationException();
                    }

                    public int getLoginTimeout() throws SQLException {
                        throw new UnsupportedOperationException();
                    }

                    public PrintWriter getLogWriter() throws SQLException {
                        throw new UnsupportedOperationException();
                    }

                    public java.util.logging.Logger getParentLogger() {
                        throw new UnsupportedOperationException();
                    }

                    public boolean isWrapperFor(Class oclass) throws SQLException {
                        return false;
                    }

                    public void setLoginTimeout(int i) throws SQLException {
                        throw new UnsupportedOperationException();
                    }

                    public void setLogWriter(PrintWriter printwriter) throws SQLException {
                        throw new UnsupportedOperationException();
                    }

                    public Object unwrap(Class oclass) throws SQLException {
                        return null;
                    }
                };
            }

            return new FactoryMethodConnectionSource(datasource, s, s1, s2);
        } else {
            FactoryMethodConnectionSource.LOGGER.error("No class name or method name specified for the connection factory method.");
            return null;
        }
    }
}
