package org.apache.logging.log4j.core.jmx;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public interface LoggerContextAdminMBean {

    String PATTERN = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s";
    String NOTIF_TYPE_RECONFIGURED = "com.apache.logging.log4j.core.jmx.config.reconfigured";

    String getStatus();

    String getName();

    String getConfigLocationURI();

    void setConfigLocationURI(String s) throws URISyntaxException, IOException;

    String getConfigText() throws IOException;

    String getConfigText(String s) throws IOException;

    void setConfigText(String s, String s1);

    String getConfigName();

    String getConfigClassName();

    String getConfigFilter();

    String getConfigMonitorClassName();

    Map getConfigProperties();
}
