package org.apache.logging.log4j.core.helpers;

public final class Assert {

    public static Object isNotNull(Object object, String s) {
        if (object == null) {
            throw new NullPointerException(s + " is null");
        } else {
            return object;
        }
    }
}
