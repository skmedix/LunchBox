package org.apache.logging.log4j.core.jmx;

public interface LoggerConfigAdminMBean {

    String PATTERN = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s";

    String getName();

    String getLevel();

    void setLevel(String s);

    boolean isAdditive();

    void setAdditive(boolean flag);

    boolean isIncludeLocation();

    String getFilter();

    String[] getAppenderRefs();
}
