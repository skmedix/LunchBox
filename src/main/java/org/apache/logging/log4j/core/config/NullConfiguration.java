package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;

public class NullConfiguration extends BaseConfiguration {

    public static final String NULL_NAME = "Null";

    public NullConfiguration() {
        this.setName("Null");
        LoggerConfig loggerconfig = this.getRootLogger();

        loggerconfig.setLevel(Level.OFF);
    }
}
