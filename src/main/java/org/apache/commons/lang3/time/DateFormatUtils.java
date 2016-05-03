package org.apache.commons.lang3.time;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatUtils {

    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
    public static final FastDateFormat ISO_DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd\'T\'HH:mm:ss");
    public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd\'T\'HH:mm:ssZZ");
    public static final FastDateFormat ISO_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
    public static final FastDateFormat ISO_DATE_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-ddZZ");
    public static final FastDateFormat ISO_TIME_FORMAT = FastDateFormat.getInstance("\'T\'HH:mm:ss");
    public static final FastDateFormat ISO_TIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("\'T\'HH:mm:ssZZ");
    public static final FastDateFormat ISO_TIME_NO_T_FORMAT = FastDateFormat.getInstance("HH:mm:ss");
    public static final FastDateFormat ISO_TIME_NO_T_TIME_ZONE_FORMAT = FastDateFormat.getInstance("HH:mm:ssZZ");
    public static final FastDateFormat SMTP_DATETIME_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    public static String formatUTC(long i, String s) {
        return format(new Date(i), s, DateFormatUtils.UTC_TIME_ZONE, (Locale) null);
    }

    public static String formatUTC(Date date, String s) {
        return format(date, s, DateFormatUtils.UTC_TIME_ZONE, (Locale) null);
    }

    public static String formatUTC(long i, String s, Locale locale) {
        return format(new Date(i), s, DateFormatUtils.UTC_TIME_ZONE, locale);
    }

    public static String formatUTC(Date date, String s, Locale locale) {
        return format(date, s, DateFormatUtils.UTC_TIME_ZONE, locale);
    }

    public static String format(long i, String s) {
        return format(new Date(i), s, (TimeZone) null, (Locale) null);
    }

    public static String format(Date date, String s) {
        return format(date, s, (TimeZone) null, (Locale) null);
    }

    public static String format(Calendar calendar, String s) {
        return format(calendar, s, (TimeZone) null, (Locale) null);
    }

    public static String format(long i, String s, TimeZone timezone) {
        return format(new Date(i), s, timezone, (Locale) null);
    }

    public static String format(Date date, String s, TimeZone timezone) {
        return format(date, s, timezone, (Locale) null);
    }

    public static String format(Calendar calendar, String s, TimeZone timezone) {
        return format(calendar, s, timezone, (Locale) null);
    }

    public static String format(long i, String s, Locale locale) {
        return format(new Date(i), s, (TimeZone) null, locale);
    }

    public static String format(Date date, String s, Locale locale) {
        return format(date, s, (TimeZone) null, locale);
    }

    public static String format(Calendar calendar, String s, Locale locale) {
        return format(calendar, s, (TimeZone) null, locale);
    }

    public static String format(long i, String s, TimeZone timezone, Locale locale) {
        return format(new Date(i), s, timezone, locale);
    }

    public static String format(Date date, String s, TimeZone timezone, Locale locale) {
        FastDateFormat fastdateformat = FastDateFormat.getInstance(s, timezone, locale);

        return fastdateformat.format(date);
    }

    public static String format(Calendar calendar, String s, TimeZone timezone, Locale locale) {
        FastDateFormat fastdateformat = FastDateFormat.getInstance(s, timezone, locale);

        return fastdateformat.format(calendar);
    }
}
