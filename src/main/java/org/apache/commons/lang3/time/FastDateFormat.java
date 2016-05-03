package org.apache.commons.lang3.time;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormat extends Format implements DateParser, DatePrinter {

    private static final long serialVersionUID = 2L;
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private static final FormatCache cache = new FormatCache() {
        protected FastDateFormat createInstance(String s, TimeZone timezone, Locale locale) {
            return new FastDateFormat(s, timezone, locale);
        }
    };
    private final FastDatePrinter printer;
    private final FastDateParser parser;

    public static FastDateFormat getInstance() {
        return (FastDateFormat) FastDateFormat.cache.getInstance();
    }

    public static FastDateFormat getInstance(String s) {
        return (FastDateFormat) FastDateFormat.cache.getInstance(s, (TimeZone) null, (Locale) null);
    }

    public static FastDateFormat getInstance(String s, TimeZone timezone) {
        return (FastDateFormat) FastDateFormat.cache.getInstance(s, timezone, (Locale) null);
    }

    public static FastDateFormat getInstance(String s, Locale locale) {
        return (FastDateFormat) FastDateFormat.cache.getInstance(s, (TimeZone) null, locale);
    }

    public static FastDateFormat getInstance(String s, TimeZone timezone, Locale locale) {
        return (FastDateFormat) FastDateFormat.cache.getInstance(s, timezone, locale);
    }

    public static FastDateFormat getDateInstance(int i) {
        return (FastDateFormat) FastDateFormat.cache.getDateInstance(i, (TimeZone) null, (Locale) null);
    }

    public static FastDateFormat getDateInstance(int i, Locale locale) {
        return (FastDateFormat) FastDateFormat.cache.getDateInstance(i, (TimeZone) null, locale);
    }

    public static FastDateFormat getDateInstance(int i, TimeZone timezone) {
        return (FastDateFormat) FastDateFormat.cache.getDateInstance(i, timezone, (Locale) null);
    }

    public static FastDateFormat getDateInstance(int i, TimeZone timezone, Locale locale) {
        return (FastDateFormat) FastDateFormat.cache.getDateInstance(i, timezone, locale);
    }

    public static FastDateFormat getTimeInstance(int i) {
        return (FastDateFormat) FastDateFormat.cache.getTimeInstance(i, (TimeZone) null, (Locale) null);
    }

    public static FastDateFormat getTimeInstance(int i, Locale locale) {
        return (FastDateFormat) FastDateFormat.cache.getTimeInstance(i, (TimeZone) null, locale);
    }

    public static FastDateFormat getTimeInstance(int i, TimeZone timezone) {
        return (FastDateFormat) FastDateFormat.cache.getTimeInstance(i, timezone, (Locale) null);
    }

    public static FastDateFormat getTimeInstance(int i, TimeZone timezone, Locale locale) {
        return (FastDateFormat) FastDateFormat.cache.getTimeInstance(i, timezone, locale);
    }

    public static FastDateFormat getDateTimeInstance(int i, int j) {
        return (FastDateFormat) FastDateFormat.cache.getDateTimeInstance(i, j, (TimeZone) null, (Locale) null);
    }

    public static FastDateFormat getDateTimeInstance(int i, int j, Locale locale) {
        return (FastDateFormat) FastDateFormat.cache.getDateTimeInstance(i, j, (TimeZone) null, locale);
    }

    public static FastDateFormat getDateTimeInstance(int i, int j, TimeZone timezone) {
        return getDateTimeInstance(i, j, timezone, (Locale) null);
    }

    public static FastDateFormat getDateTimeInstance(int i, int j, TimeZone timezone, Locale locale) {
        return (FastDateFormat) FastDateFormat.cache.getDateTimeInstance(i, j, timezone, locale);
    }

    protected FastDateFormat(String s, TimeZone timezone, Locale locale) {
        this(s, timezone, locale, (Date) null);
    }

    protected FastDateFormat(String s, TimeZone timezone, Locale locale, Date date) {
        this.printer = new FastDatePrinter(s, timezone, locale);
        this.parser = new FastDateParser(s, timezone, locale, date);
    }

    public StringBuffer format(Object object, StringBuffer stringbuffer, FieldPosition fieldposition) {
        return this.printer.format(object, stringbuffer, fieldposition);
    }

    public String format(long i) {
        return this.printer.format(i);
    }

    public String format(Date date) {
        return this.printer.format(date);
    }

    public String format(Calendar calendar) {
        return this.printer.format(calendar);
    }

    public StringBuffer format(long i, StringBuffer stringbuffer) {
        return this.printer.format(i, stringbuffer);
    }

    public StringBuffer format(Date date, StringBuffer stringbuffer) {
        return this.printer.format(date, stringbuffer);
    }

    public StringBuffer format(Calendar calendar, StringBuffer stringbuffer) {
        return this.printer.format(calendar, stringbuffer);
    }

    public Date parse(String s) throws ParseException {
        return this.parser.parse(s);
    }

    public Date parse(String s, ParsePosition parseposition) {
        return this.parser.parse(s, parseposition);
    }

    public Object parseObject(String s, ParsePosition parseposition) {
        return this.parser.parseObject(s, parseposition);
    }

    public String getPattern() {
        return this.printer.getPattern();
    }

    public TimeZone getTimeZone() {
        return this.printer.getTimeZone();
    }

    public Locale getLocale() {
        return this.printer.getLocale();
    }

    public int getMaxLengthEstimate() {
        return this.printer.getMaxLengthEstimate();
    }

    public boolean equals(Object object) {
        if (!(object instanceof FastDateFormat)) {
            return false;
        } else {
            FastDateFormat fastdateformat = (FastDateFormat) object;

            return this.printer.equals(fastdateformat.printer);
        }
    }

    public int hashCode() {
        return this.printer.hashCode();
    }

    public String toString() {
        return "FastDateFormat[" + this.printer.getPattern() + "," + this.printer.getLocale() + "," + this.printer.getTimeZone().getID() + "]";
    }

    protected StringBuffer applyRules(Calendar calendar, StringBuffer stringbuffer) {
        return this.printer.applyRules(calendar, stringbuffer);
    }
}
