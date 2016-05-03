package org.apache.logging.log4j.core.appender.db.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "DataSource",
    category = "Core",
    elementType = "connectionSource",
    printObject = true
)
public final class DataSourceConnectionSource implements ConnectionSource {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private final DataSource dataSource;
    private final String description;

    private DataSourceConnectionSource(String s, DataSource datasource) {
        this.dataSource = datasource;
        this.description = "dataSource{ name=" + s + ", value=" + datasource + " }";
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static DataSourceConnectionSource createConnectionSource(@PluginAttribute("jndiName") String s) {
        if (Strings.isEmpty(s)) {
            DataSourceConnectionSource.LOGGER.error("No JNDI name provided.");
            return null;
        } else {
            try {
                InitialContext initialcontext = new InitialContext();
                DataSource datasource = (DataSource) initialcontext.lookup(s);

                if (datasource == null) {
                    DataSourceConnectionSource.LOGGER.error("No data source found with JNDI name [" + s + "].");
                    return null;
                } else {
                    return new DataSourceConnectionSource(s, datasource);
                }
            } catch (NamingException namingexception) {
                DataSourceConnectionSource.LOGGER.error(namingexception.getMessage(), (Throwable) namingexception);
                return null;
            }
        }
    }
}
