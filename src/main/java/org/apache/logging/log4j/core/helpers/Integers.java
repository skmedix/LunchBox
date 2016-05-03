package org.apache.logging.log4j.core.helpers;

public class Integers {

    public static int parseInt(String s, int i) {
        return Strings.isEmpty(s) ? i : Integer.parseInt(s);
    }

    public static int parseInt(String s) {
        return parseInt(s, 0);
    }
}
