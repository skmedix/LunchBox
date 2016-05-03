package org.apache.logging.log4j.core.helpers;

public class Booleans {

    public static boolean parseBoolean(String s, boolean flag) {
        return "true".equalsIgnoreCase(s) || flag && !"false".equalsIgnoreCase(s);
    }
}
