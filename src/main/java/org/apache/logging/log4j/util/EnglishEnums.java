package org.apache.logging.log4j.util;

import java.util.Locale;

public final class EnglishEnums {

    public static Enum valueOf(Class oclass, String s) {
        return valueOf(oclass, s, (Enum) null);
    }

    public static Enum valueOf(Class oclass, String s, Enum oenum) {
        return s == null ? oenum : Enum.valueOf(oclass, s.toUpperCase(Locale.ENGLISH));
    }
}
