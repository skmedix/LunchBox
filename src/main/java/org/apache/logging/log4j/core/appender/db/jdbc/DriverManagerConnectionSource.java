package org.apache.logging.log4j.core.appender.db.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "DriverManager",
    category = "Core",
    elementType = "connectionSource",
    printObject = true
)
public final class DriverManagerConnectionSource implements ConnectionSource {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private final String databasePassword;
    private final String databaseUrl;
    private final String databaseUsername;
    private final String description;

    private DriverManagerConnectionSource(String s, String s1, String s2) {
        this.databaseUrl = s;
        this.databaseUsername = s1;
        this.databasePassword = s2;
        this.description = "driverManager{ url=" + this.databaseUrl + ", username=" + this.databaseUsername + ", passwordHash=" + NameUtil.md5(this.databasePassword + this.getClass().getName()) + " }";
    }

    public Connection getConnection() throws SQLException {
        return this.databaseUsername == null ? DriverManager.getConnection(this.databaseUrl) : DriverManager.getConnection(this.databaseUrl, this.databaseUsername, this.databasePassword);
    }

    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static DriverManagerConnectionSource createConnectionSource(@PluginAttribute("url") String s, @PluginAttribute("username") String s1, @PluginAttribute("password") String s2) {
        if (Strings.isEmpty(s)) {
            DriverManagerConnectionSource.LOGGER.error("No JDBC URL specified for the database.");
            return null;
        } else {
            Driver driver;

            try {
                driver = DriverManager.getDriver(s);
            } catch (SQLException sqlexception) {
                DriverManagerConnectionSource.LOGGER.error("No matching driver found for database URL [" + s + "].", (Throwable) sqlexception);
                return null;
            }

            if (driver == null) {
                DriverManagerConnectionSource.LOGGER.error("No matching driver found for database URL [" + s + "].");
                return null;
            } else {
                if (s1 == null || s1.trim().isEmpty()) {
                    s1 = null;
                    s2 = null;
                }

                return new DriverManagerConnectionSource(s, s1, s2);
            }
        }
    }
}
