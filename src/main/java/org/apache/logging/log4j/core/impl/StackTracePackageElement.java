package org.apache.logging.log4j.core.impl;

import java.io.Serializable;

public class StackTracePackageElement implements Serializable {

    private static final long serialVersionUID = -2171069569241280505L;
    private final String location;
    private final String version;
    private final boolean isExact;

    public StackTracePackageElement(String s, String s1, boolean flag) {
        this.location = s;
        this.version = s1;
        this.isExact = flag;
    }

    public String getLocation() {
        return this.location;
    }

    public String getVersion() {
        return this.version;
    }

    public boolean isExact() {
        return this.isExact;
    }

    public String toString() {
        String s = this.isExact ? "" : "~";

        return s + "[" + this.location + ":" + this.version + "]";
    }
}
