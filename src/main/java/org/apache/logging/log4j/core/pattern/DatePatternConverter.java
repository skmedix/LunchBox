package org.apache.logging.log4j.core.pattern;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "DatePatternConverter",
    category = "Converter"
)
@ConverterKeys({ "d", "date"})
public final class DatePatternConverter extends LogEventPatternConverter implements ArrayPatternConverter {

    private static final String ABSOLUTE_FORMAT = "ABSOLUTE";
    private static final String COMPACT_FORMAT = "COMPACT";
    private static final String ABSOLUTE_TIME_PATTERN = "HH:mm:ss,SSS";
    private static final String DATE_AND_TIME_FORMAT = "DATE";
    private static final String DATE_AND_TIME_PATTERN = "dd MMM yyyy HH:mm:ss,SSS";
    private static final String ISO8601_FORMAT = "ISO8601";
    private static final String ISO8601_BASIC_FORMAT = "ISO8601_BASIC";
    private static final String ISO8601_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
    private static final String ISO8601_BASIC_PATTERN = "yyyyMMdd HHmmss,SSS";
    private static final String COMPACT_PATTERN = "yyyyMMddHHmmssSSS";
    private String cachedDate;
    private long lastTimestamp;
    private final SimpleDateFormat simpleFormat;

    private DatePatternConverter(String[] astring) {
        super("Date", "date");
        String s;

        if (astring != null && astring.length != 0) {
            s = astring[0];
        } else {
            s = null;
        }

        String s1;

        if (s != null && !s.equalsIgnoreCase("ISO8601")) {
            if (s.equalsIgnoreCase("ISO8601_BASIC")) {
                s1 = "yyyyMMdd HHmmss,SSS";
            } else if (s.equalsIgnoreCase("ABSOLUTE")) {
                s1 = "HH:mm:ss,SSS";
            } else if (s.equalsIgnoreCase("DATE")) {
                s1 = "dd MMM yyyy HH:mm:ss,SSS";
            } else if (s.equalsIgnoreCase("COMPACT")) {
                s1 = "yyyyMMddHHmmssSSS";
            } else {
                s1 = s;
            }
        } else {
            s1 = "yyyy-MM-dd HH:mm:ss,SSS";
        }

        SimpleDateFormat simpledateformat;

        try {
            simpledateformat = new SimpleDateFormat(s1);
        } catch (IllegalArgumentException illegalargumentexception) {
            DatePatternConverter.LOGGER.warn("Could not instantiate SimpleDateFormat with pattern " + s, (Throwable) illegalargumentexception);
            simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        }

        if (astring != null && astring.length > 1) {
            TimeZone timezone = TimeZone.getTimeZone(astring[1]);

            simpledateformat.setTimeZone(timezone);
        }

        this.simpleFormat = simpledateformat;
    }

    public static DatePatternConverter newInstance(String[] astring) {
        return new DatePatternConverter(astring);
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        long i = logevent.getMillis();

        synchronized (this) {
            if (i != this.lastTimestamp) {
                this.lastTimestamp = i;
                this.cachedDate = this.simpleFormat.format(Long.valueOf(i));
            }
        }

        stringbuilder.append(this.cachedDate);
    }

    public void format(StringBuilder stringbuilder, Object... aobject) {
        Object[] aobject1 = aobject;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject1[j];

            if (object instanceof Date) {
                this.format(object, stringbuilder);
                break;
            }
        }

    }

    public void format(Object object, StringBuilder stringbuilder) {
        if (object instanceof Date) {
            this.format((Date) object, stringbuilder);
        }

        super.format(object, stringbuilder);
    }

    public void format(Date date, StringBuilder stringbuilder) {
        synchronized (this) {
            stringbuilder.append(this.simpleFormat.format(Long.valueOf(date.getTime())));
        }
    }

    public String getPattern() {
        return this.simpleFormat.toPattern();
    }
}
