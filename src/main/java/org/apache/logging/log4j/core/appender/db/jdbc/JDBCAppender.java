package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(
    name = "JDBC",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class JDBCAppender extends AbstractDatabaseAppender {

    private final String description = this.getName() + "{ manager=" + this.getManager() + " }";

    private JDBCAppender(String s, Filter filter, boolean flag, JDBCDatabaseManager jdbcdatabasemanager) {
        super(s, filter, flag, jdbcdatabasemanager);
    }

    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static JDBCAppender createAppender(@PluginAttribute("name") String s, @PluginAttribute("ignoreExceptions") String s1, @PluginElement("Filter") Filter filter, @PluginElement("ConnectionSource") ConnectionSource connectionsource, @PluginAttribute("bufferSize") String s2, @PluginAttribute("tableName") String s3, @PluginElement("ColumnConfigs") ColumnConfig[] acolumnconfig) {
        int i = AbstractAppender.parseInt(s2, 0);
        boolean flag = Booleans.parseBoolean(s1, true);
        StringBuilder stringbuilder = (new StringBuilder("jdbcManager{ description=")).append(s).append(", bufferSize=").append(i).append(", connectionSource=").append(connectionsource.toString()).append(", tableName=").append(s3).append(", columns=[ ");
        int j = 0;
        ColumnConfig[] acolumnconfig1 = acolumnconfig;
        int k = acolumnconfig.length;

        for (int l = 0; l < k; ++l) {
            ColumnConfig columnconfig = acolumnconfig1[l];

            if (j++ > 0) {
                stringbuilder.append(", ");
            }

            stringbuilder.append(columnconfig.toString());
        }

        stringbuilder.append(" ] }");
        JDBCDatabaseManager jdbcdatabasemanager = JDBCDatabaseManager.getJDBCDatabaseManager(stringbuilder.toString(), i, connectionsource, s3, acolumnconfig);

        if (jdbcdatabasemanager == null) {
            return null;
        } else {
            return new JDBCAppender(s, filter, flag, jdbcdatabasemanager);
        }
    }
}
