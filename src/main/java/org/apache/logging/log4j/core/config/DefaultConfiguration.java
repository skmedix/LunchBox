package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.util.PropertiesUtil;

public class DefaultConfiguration extends BaseConfiguration {

    public static final String DEFAULT_NAME = "Default";
    public static final String DEFAULT_LEVEL = "org.apache.logging.log4j.level";

    public DefaultConfiguration() {
        this.setName("Default");
        PatternLayout patternlayout = PatternLayout.createLayout("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n", (Configuration) null, (RegexReplacement) null, (String) null, (String) null);
        ConsoleAppender consoleappender = ConsoleAppender.createAppender(patternlayout, (Filter) null, "SYSTEM_OUT", "Console", "false", "true");

        consoleappender.start();
        this.addAppender(consoleappender);
        LoggerConfig loggerconfig = this.getRootLogger();

        loggerconfig.addAppender(consoleappender, (Level) null, (Filter) null);
        String s = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level");
        Level level = s != null && Level.valueOf(s) != null ? Level.valueOf(s) : Level.ERROR;

        loggerconfig.setLevel(level);
    }

    protected void doConfigure() {}
}
