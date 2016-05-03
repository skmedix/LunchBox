package org.apache.logging.log4j.core.pattern;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

final class CachedDateFormat extends DateFormat {

    public static final int NO_MILLISECONDS = -2;
    public static final int UNRECOGNIZED_MILLISECONDS = -1;
    private static final long serialVersionUID = -1253877934598423628L;
    private static final String DIGITS = "0123456789";
    private static final int MAGIC1 = 654;
    private static final String MAGICSTRING1 = "654";
    private static final int MAGIC2 = 987;
    private static final String MAGICSTRING2 = "987";
    private static final String ZERO_STRING = "000";
    private static final int BUF_SIZE = 50;
    private static final int DEFAULT_VALIDITY = 1000;
    private static final int THREE_DIGITS = 100;
    private static final int TWO_DIGITS = 10;
    private static final long SLOTS = 1000L;
    private final DateFormat formatter;
    private int millisecondStart;
    private long slotBegin;
    private final StringBuffer cache = new StringBuffer(50);
    private final int expiration;
    private long previousTime;
    private final Date tmpDate = new Date(0L);

    public CachedDateFormat(DateFormat dateformat, int i) {
        if (dateformat == null) {
            throw new IllegalArgumentException("dateFormat cannot be null");
        } else if (i < 0) {
            throw new IllegalArgumentException("expiration must be non-negative");
        } else {
            this.formatter = dateformat;
            this.expiration = i;
            this.millisecondStart = 0;
            this.previousTime = Long.MIN_VALUE;
            this.slotBegin = Long.MIN_VALUE;
        }
    }

    public static int findMillisecondStart(long i, String s, DateFormat dateformat) {
        long j = i / 1000L * 1000L;

        if (j > i) {
            j -= 1000L;
        }

        int k = (int) (i - j);
        short short0 = 654;
        String s1 = "654";

        if (k == 654) {
            short0 = 987;
            s1 = "987";
        }

        String s2 = dateformat.format(new Date(j + (long) short0));

        if (s2.length() != s.length()) {
            return -1;
        } else {
            for (int l = 0; l < s.length(); ++l) {
                if (s.charAt(l) != s2.charAt(l)) {
                    StringBuffer stringbuffer = new StringBuffer("ABC");

                    millisecondFormat(k, stringbuffer, 0);
                    String s3 = dateformat.format(new Date(j));

                    if (s3.length() == s.length() && s1.regionMatches(0, s2, l, s1.length()) && stringbuffer.toString().regionMatches(0, s, l, s1.length()) && "000".regionMatches(0, s3, l, "000".length())) {
                        return l;
                    }

                    return -1;
                }
            }

            return -2;
        }
    }

    public StringBuffer format(Date date, StringBuffer stringbuffer, FieldPosition fieldposition) {
        this.format(date.getTime(), stringbuffer);
        return stringbuffer;
    }

    public StringBuffer format(long i, StringBuffer stringbuffer) {
        if (i == this.previousTime) {
            stringbuffer.append(this.cache);
            return stringbuffer;
        } else if (this.millisecondStart != -1 && i < this.slotBegin + (long) this.expiration && i >= this.slotBegin && i < this.slotBegin + 1000L) {
            if (this.millisecondStart >= 0) {
                millisecondFormat((int) (i - this.slotBegin), this.cache, this.millisecondStart);
            }

            this.previousTime = i;
            stringbuffer.append(this.cache);
            return stringbuffer;
        } else {
            this.cache.setLength(0);
            this.tmpDate.setTime(i);
            this.cache.append(this.formatter.format(this.tmpDate));
            stringbuffer.append(this.cache);
            this.previousTime = i;
            this.slotBegin = this.previousTime / 1000L * 1000L;
            if (this.slotBegin > this.previousTime) {
                this.slotBegin -= 1000L;
            }

            if (this.millisecondStart >= 0) {
                this.millisecondStart = findMillisecondStart(i, this.cache.toString(), this.formatter);
            }

            return stringbuffer;
        }
    }

    private static void millisecondFormat(int i, StringBuffer stringbuffer, int j) {
        stringbuffer.setCharAt(j, "0123456789".charAt(i / 100));
        stringbuffer.setCharAt(j + 1, "0123456789".charAt(i / 10 % 10));
        stringbuffer.setCharAt(j + 2, "0123456789".charAt(i % 10));
    }

    public void setTimeZone(TimeZone timezone) {
        this.formatter.setTimeZone(timezone);
        this.previousTime = Long.MIN_VALUE;
        this.slotBegin = Long.MIN_VALUE;
    }

    public Date parse(String s, ParsePosition parseposition) {
        return this.formatter.parse(s, parseposition);
    }

    public NumberFormat getNumberFormat() {
        return this.formatter.getNumberFormat();
    }

    public static int getMaximumCacheValidity(String s) {
        int i = s.indexOf(83);

        return i >= 0 && i != s.lastIndexOf("SSS") ? 1 : 1000;
    }
}
