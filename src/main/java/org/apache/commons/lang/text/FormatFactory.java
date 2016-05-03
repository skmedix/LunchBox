package org.apache.commons.lang.text;

import java.text.Format;
import java.util.Locale;

public interface FormatFactory {

    Format getFormat(String s, String s1, Locale locale);
}
