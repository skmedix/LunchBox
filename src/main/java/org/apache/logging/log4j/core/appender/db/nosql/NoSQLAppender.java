package org.apache.logging.log4j.core.appender.db.nosql;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(
    name = "NoSql",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class NoSQLAppender extends AbstractDatabaseAppender {

    private final String description = this.getName() + "{ manager=" + this.getManager() + " }";

    private NoSQLAppender(String s, Filter filter, boolean flag, NoSQLDatabaseManager nosqldatabasemanager) {
        super(s, filter, flag, nosqldatabasemanager);
    }

    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static NoSQLAppender createAppender(@PluginAttribute("name") String s, @PluginAttribute("ignoreExceptions") String s1, @PluginElement("Filter") Filter filter, @PluginAttribute("bufferSize") String s2, @PluginElement("NoSqlProvider") NoSQLProvider nosqlprovider) {
        if (nosqlprovider == null) {
            NoSQLAppender.LOGGER.error("NoSQL provider not specified for appender [{}].", new Object[] { s});
            return null;
        } else {
            int i = AbstractAppender.parseInt(s2, 0);
            boolean flag = Booleans.parseBoolean(s1, true);
            String s3 = "noSqlManager{ description=" + s + ", bufferSize=" + i + ", provider=" + nosqlprovider + " }";
            NoSQLDatabaseManager nosqldatabasemanager = NoSQLDatabaseManager.getNoSQLDatabaseManager(s3, i, nosqlprovider);

            return nosqldatabasemanager == null ? null : new NoSQLAppender(s, filter, flag, nosqldatabasemanager);
        }
    }
}
