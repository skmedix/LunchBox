package org.apache.logging.log4j.core.jmx;

import java.util.List;
import javax.management.ObjectName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.helpers.Assert;

public class LoggerConfigAdmin implements LoggerConfigAdminMBean {

    private final String contextName;
    private final LoggerConfig loggerConfig;
    private final ObjectName objectName;

    public LoggerConfigAdmin(String s, LoggerConfig loggerconfig) {
        this.contextName = (String) Assert.isNotNull(s, "contextName");
        this.loggerConfig = (LoggerConfig) Assert.isNotNull(loggerconfig, "loggerConfig");

        try {
            String s1 = Server.escape(this.contextName);
            String s2 = Server.escape(loggerconfig.getName());
            String s3 = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s", new Object[] { s1, s2});

            this.objectName = new ObjectName(s3);
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    public ObjectName getObjectName() {
        return this.objectName;
    }

    public String getName() {
        return this.loggerConfig.getName();
    }

    public String getLevel() {
        return this.loggerConfig.getLevel().name();
    }

    public void setLevel(String s) {
        this.loggerConfig.setLevel(Level.valueOf(s));
    }

    public boolean isAdditive() {
        return this.loggerConfig.isAdditive();
    }

    public void setAdditive(boolean flag) {
        this.loggerConfig.setAdditive(flag);
    }

    public boolean isIncludeLocation() {
        return this.loggerConfig.isIncludeLocation();
    }

    public String getFilter() {
        return String.valueOf(this.loggerConfig.getFilter());
    }

    public String[] getAppenderRefs() {
        List list = this.loggerConfig.getAppenderRefs();
        String[] astring = new String[list.size()];

        for (int i = 0; i < astring.length; ++i) {
            astring[i] = ((AppenderRef) list.get(i)).getRef();
        }

        return astring;
    }
}
