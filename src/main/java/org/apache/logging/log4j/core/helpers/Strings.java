package org.apache.logging.log4j.core.helpers;

public class Strings {

    public static boolean isEmpty(CharSequence charsequence) {
        return charsequence == null || charsequence.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence charsequence) {
        return !isEmpty(charsequence);
    }
}
