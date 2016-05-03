package org.apache.logging.log4j.core.helpers;

import java.nio.charset.Charset;
import org.apache.logging.log4j.status.StatusLogger;

public final class Charsets {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static Charset getSupportedCharset(String s) {
        return getSupportedCharset(s, Charset.defaultCharset());
    }

    public static Charset getSupportedCharset(String s, Charset charset) {
        Charset charset1 = null;

        if (s != null && Charset.isSupported(s)) {
            charset1 = Charset.forName(s);
        }

        if (charset1 == null) {
            charset1 = charset;
            if (s != null) {
                StatusLogger.getLogger().error("Charset " + s + " is not supported for layout, using " + charset.displayName());
            }
        }

        return charset1;
    }
}
