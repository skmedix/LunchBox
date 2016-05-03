package org.apache.logging.log4j.core.net;

public enum Protocol {

    TCP, UDP;

    public boolean isEqual(String s) {
        return this.name().equalsIgnoreCase(s);
    }
}
