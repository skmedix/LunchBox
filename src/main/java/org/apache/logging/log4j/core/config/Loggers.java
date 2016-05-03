package org.apache.logging.log4j.core.config;

import java.util.concurrent.ConcurrentMap;

public class Loggers {

    private final ConcurrentMap map;
    private final LoggerConfig root;

    public Loggers(ConcurrentMap concurrentmap, LoggerConfig loggerconfig) {
        this.map = concurrentmap;
        this.root = loggerconfig;
    }

    public ConcurrentMap getMap() {
        return this.map;
    }

    public LoggerConfig getRoot() {
        return this.root;
    }
}
