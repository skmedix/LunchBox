package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "Column",
    category = "Core",
    printObject = true
)
public final class ColumnConfig {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private final String columnName;
    private final PatternLayout layout;
    private final String literalValue;
    private final boolean eventTimestamp;
    private final boolean unicode;
    private final boolean clob;

    private ColumnConfig(String s, PatternLayout patternlayout, String s1, boolean flag, boolean flag1, boolean flag2) {
        this.columnName = s;
        this.layout = patternlayout;
        this.literalValue = s1;
        this.eventTimestamp = flag;
        this.unicode = flag1;
        this.clob = flag2;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public PatternLayout getLayout() {
        return this.layout;
    }

    public String getLiteralValue() {
        return this.literalValue;
    }

    public boolean isEventTimestamp() {
        return this.eventTimestamp;
    }

    public boolean isUnicode() {
        return this.unicode;
    }

    public boolean isClob() {
        return this.clob;
    }

    public String toString() {
        return "{ name=" + this.columnName + ", layout=" + this.layout + ", literal=" + this.literalValue + ", timestamp=" + this.eventTimestamp + " }";
    }

    @PluginFactory
    public static ColumnConfig createColumnConfig(@PluginConfiguration Configuration configuration, @PluginAttribute("name") String s, @PluginAttribute("pattern") String s1, @PluginAttribute("literal") String s2, @PluginAttribute("isEventTimestamp") String s3, @PluginAttribute("isUnicode") String s4, @PluginAttribute("isClob") String s5) {
        if (Strings.isEmpty(s)) {
            ColumnConfig.LOGGER.error("The column config is not valid because it does not contain a column name.");
            return null;
        } else {
            boolean flag = Strings.isNotEmpty(s1);
            boolean flag1 = Strings.isNotEmpty(s2);
            boolean flag2 = Boolean.parseBoolean(s3);
            boolean flag3 = Booleans.parseBoolean(s4, true);
            boolean flag4 = Boolean.parseBoolean(s5);

            if ((!flag || !flag1) && (!flag || !flag2) && (!flag1 || !flag2)) {
                if (flag2) {
                    return new ColumnConfig(s, (PatternLayout) null, (String) null, true, false, false);
                } else if (flag1) {
                    return new ColumnConfig(s, (PatternLayout) null, s2, false, false, false);
                } else if (flag) {
                    return new ColumnConfig(s, PatternLayout.createLayout(s1, configuration, (RegexReplacement) null, (String) null, "false"), (String) null, false, flag3, flag4);
                } else {
                    ColumnConfig.LOGGER.error("To configure a column you must specify a pattern or literal or set isEventDate to true.");
                    return null;
                }
            } else {
                ColumnConfig.LOGGER.error("The pattern, literal, and isEventTimestamp attributes are mutually exclusive.");
                return null;
            }
        }
    }
}
