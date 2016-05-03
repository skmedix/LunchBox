package org.apache.commons.lang.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

public class DurationFormatUtils {

    public static final String ISO_EXTENDED_FORMAT_PATTERN = "\'P\'yyyy\'Y\'M\'M\'d\'DT\'H\'H\'m\'M\'s.S\'S\'";
    static final Object y = "y";
    static final Object M = "M";
    static final Object d = "d";
    static final Object H = "H";
    static final Object m = "m";
    static final Object s = "s";
    static final Object S = "S";

    public static String formatDurationHMS(long durationMillis) {
        return formatDuration(durationMillis, "H:mm:ss.SSS");
    }

    public static String formatDurationISO(long durationMillis) {
        return formatDuration(durationMillis, "\'P\'yyyy\'Y\'M\'M\'d\'DT\'H\'H\'m\'M\'s.S\'S\'", false);
    }

    public static String formatDuration(long durationMillis, String format) {
        return formatDuration(durationMillis, format, true);
    }

    public static String formatDuration(long durationMillis, String format, boolean padWithZeros) {
        DurationFormatUtils.Token[] tokens = lexx(format);
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        int milliseconds = 0;

        if (DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.d)) {
            days = (int) (durationMillis / 86400000L);
            durationMillis -= (long) days * 86400000L;
        }

        if (DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.H)) {
            hours = (int) (durationMillis / 3600000L);
            durationMillis -= (long) hours * 3600000L;
        }

        if (DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.m)) {
            minutes = (int) (durationMillis / 60000L);
            durationMillis -= (long) minutes * 60000L;
        }

        if (DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.s)) {
            seconds = (int) (durationMillis / 1000L);
            durationMillis -= (long) seconds * 1000L;
        }

        if (DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.S)) {
            milliseconds = (int) durationMillis;
        }

        return format(tokens, 0, 0, days, hours, minutes, seconds, milliseconds, padWithZeros);
    }

    public static String formatDurationWords(long durationMillis, boolean suppressLeadingZeroElements, boolean suppressTrailingZeroElements) {
        String duration = formatDuration(durationMillis, "d\' days \'H\' hours \'m\' minutes \'s\' seconds\'");
        String tmp;

        if (suppressLeadingZeroElements) {
            duration = " " + duration;
            tmp = StringUtils.replaceOnce(duration, " 0 days", "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(tmp, " 0 hours", "");
                if (tmp.length() != duration.length()) {
                    tmp = StringUtils.replaceOnce(tmp, " 0 minutes", "");
                    duration = tmp;
                    if (tmp.length() != tmp.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0 seconds", "");
                    }
                }
            }

            if (duration.length() != 0) {
                duration = duration.substring(1);
            }
        }

        if (suppressTrailingZeroElements) {
            tmp = StringUtils.replaceOnce(duration, " 0 seconds", "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(tmp, " 0 minutes", "");
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(tmp, " 0 hours", "");
                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0 days", "");
                    }
                }
            }
        }

        duration = " " + duration;
        duration = StringUtils.replaceOnce(duration, " 1 seconds", " 1 second");
        duration = StringUtils.replaceOnce(duration, " 1 minutes", " 1 minute");
        duration = StringUtils.replaceOnce(duration, " 1 hours", " 1 hour");
        duration = StringUtils.replaceOnce(duration, " 1 days", " 1 day");
        return duration.trim();
    }

    public static String formatPeriodISO(long startMillis, long endMillis) {
        return formatPeriod(startMillis, endMillis, "\'P\'yyyy\'Y\'M\'M\'d\'DT\'H\'H\'m\'M\'s.S\'S\'", false, TimeZone.getDefault());
    }

    public static String formatPeriod(long startMillis, long endMillis, String format) {
        return formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
    }

    public static String formatPeriod(long startMillis, long endMillis, String format, boolean padWithZeros, TimeZone timezone) {
        DurationFormatUtils.Token[] tokens = lexx(format);
        Calendar start = Calendar.getInstance(timezone);

        start.setTime(new Date(startMillis));
        Calendar end = Calendar.getInstance(timezone);

        end.setTime(new Date(endMillis));
        int milliseconds = end.get(14) - start.get(14);
        int seconds = end.get(13) - start.get(13);
        int minutes = end.get(12) - start.get(12);
        int hours = end.get(11) - start.get(11);
        int days = end.get(5) - start.get(5);
        int months = end.get(2) - start.get(2);

        int years;

        for (years = end.get(1) - start.get(1); milliseconds < 0; --seconds) {
            milliseconds += 1000;
        }

        while (seconds < 0) {
            seconds += 60;
            --minutes;
        }

        while (minutes < 0) {
            minutes += 60;
            --hours;
        }

        while (hours < 0) {
            hours += 24;
            --days;
        }

        if (DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.M)) {
            while (days < 0) {
                days += start.getActualMaximum(5);
                --months;
                start.add(2, 1);
            }

            while (months < 0) {
                months += 12;
                --years;
            }

            if (!DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.y) && years != 0) {
                while (years != 0) {
                    months += 12 * years;
                    years = 0;
                }
            }
        } else {
            if (!DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.y)) {
                int target = end.get(1);

                if (months < 0) {
                    --target;
                }

                while (start.get(1) != target) {
                    days += start.getActualMaximum(6) - start.get(6);
                    if (start instanceof GregorianCalendar && start.get(2) == 1 && start.get(5) == 29) {
                        ++days;
                    }

                    start.add(1, 1);
                    days += start.get(6);
                }

                years = 0;
            }

            while (start.get(2) != end.get(2)) {
                days += start.getActualMaximum(5);
                start.add(2, 1);
            }

            months = 0;

            while (days < 0) {
                days += start.getActualMaximum(5);
                --months;
                start.add(2, 1);
            }
        }

        if (!DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.d)) {
            hours += 24 * days;
            days = 0;
        }

        if (!DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.H)) {
            minutes += 60 * hours;
            hours = 0;
        }

        if (!DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.m)) {
            seconds += 60 * minutes;
            minutes = 0;
        }

        if (!DurationFormatUtils.Token.containsTokenWithValue(tokens, DurationFormatUtils.s)) {
            milliseconds += 1000 * seconds;
            seconds = 0;
        }

        return format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
    }

    static String format(DurationFormatUtils.Token[] tokens, int years, int months, int days, int hours, int minutes, int seconds, int milliseconds, boolean padWithZeros) {
        StrBuilder buffer = new StrBuilder();
        boolean lastOutputSeconds = false;
        int sz = tokens.length;

        for (int i = 0; i < sz; ++i) {
            DurationFormatUtils.Token token = tokens[i];
            Object value = token.getValue();
            int count = token.getCount();

            if (value instanceof StringBuffer) {
                buffer.append(value.toString());
            } else if (value == DurationFormatUtils.y) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(years), count, '0') : Integer.toString(years));
                lastOutputSeconds = false;
            } else if (value == DurationFormatUtils.M) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(months), count, '0') : Integer.toString(months));
                lastOutputSeconds = false;
            } else if (value == DurationFormatUtils.d) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(days), count, '0') : Integer.toString(days));
                lastOutputSeconds = false;
            } else if (value == DurationFormatUtils.H) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(hours), count, '0') : Integer.toString(hours));
                lastOutputSeconds = false;
            } else if (value == DurationFormatUtils.m) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(minutes), count, '0') : Integer.toString(minutes));
                lastOutputSeconds = false;
            } else if (value == DurationFormatUtils.s) {
                buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(seconds), count, '0') : Integer.toString(seconds));
                lastOutputSeconds = true;
            } else if (value == DurationFormatUtils.S) {
                if (lastOutputSeconds) {
                    milliseconds += 1000;
                    String str = padWithZeros ? StringUtils.leftPad(Integer.toString(milliseconds), count, '0') : Integer.toString(milliseconds);

                    buffer.append(str.substring(1));
                } else {
                    buffer.append(padWithZeros ? StringUtils.leftPad(Integer.toString(milliseconds), count, '0') : Integer.toString(milliseconds));
                }

                lastOutputSeconds = false;
            }
        }

        return buffer.toString();
    }

    static DurationFormatUtils.Token[] lexx(String format) {
        char[] array = format.toCharArray();
        ArrayList list = new ArrayList(array.length);
        boolean inLiteral = false;
        StringBuffer buffer = null;
        DurationFormatUtils.Token previous = null;
        int sz = array.length;

        for (int i = 0; i < sz; ++i) {
            char ch = array[i];

            if (inLiteral && ch != 39) {
                buffer.append(ch);
            } else {
                Object value = null;

                switch (ch) {
                case '\'':
                    if (inLiteral) {
                        buffer = null;
                        inLiteral = false;
                    } else {
                        buffer = new StringBuffer();
                        list.add(new DurationFormatUtils.Token(buffer));
                        inLiteral = true;
                    }
                    break;

                case 'H':
                    value = DurationFormatUtils.H;
                    break;

                case 'M':
                    value = DurationFormatUtils.M;
                    break;

                case 'S':
                    value = DurationFormatUtils.S;
                    break;

                case 'd':
                    value = DurationFormatUtils.d;
                    break;

                case 'm':
                    value = DurationFormatUtils.m;
                    break;

                case 's':
                    value = DurationFormatUtils.s;
                    break;

                case 'y':
                    value = DurationFormatUtils.y;
                    break;

                default:
                    if (buffer == null) {
                        buffer = new StringBuffer();
                        list.add(new DurationFormatUtils.Token(buffer));
                    }

                    buffer.append(ch);
                }

                if (value != null) {
                    if (previous != null && previous.getValue() == value) {
                        previous.increment();
                    } else {
                        DurationFormatUtils.Token token = new DurationFormatUtils.Token(value);

                        list.add(token);
                        previous = token;
                    }

                    buffer = null;
                }
            }
        }

        return (DurationFormatUtils.Token[]) ((DurationFormatUtils.Token[]) list.toArray(new DurationFormatUtils.Token[list.size()]));
    }

    static class Token {

        private Object value;
        private int count;

        static boolean containsTokenWithValue(DurationFormatUtils.Token[] tokens, Object value) {
            int sz = tokens.length;

            for (int i = 0; i < sz; ++i) {
                if (tokens[i].getValue() == value) {
                    return true;
                }
            }

            return false;
        }

        Token(Object value) {
            this.value = value;
            this.count = 1;
        }

        Token(Object value, int count) {
            this.value = value;
            this.count = count;
        }

        void increment() {
            ++this.count;
        }

        int getCount() {
            return this.count;
        }

        Object getValue() {
            return this.value;
        }

        public boolean equals(Object obj2) {
            if (obj2 instanceof DurationFormatUtils.Token) {
                DurationFormatUtils.Token tok2 = (DurationFormatUtils.Token) obj2;

                return this.value.getClass() != tok2.value.getClass() ? false : (this.count != tok2.count ? false : (this.value instanceof StringBuffer ? this.value.toString().equals(tok2.value.toString()) : (this.value instanceof Number ? this.value.equals(tok2.value) : this.value == tok2.value)));
            } else {
                return false;
            }
        }

        public int hashCode() {
            return this.value.hashCode();
        }

        public String toString() {
            return StringUtils.repeat(this.value.toString(), this.count);
        }
    }
}
