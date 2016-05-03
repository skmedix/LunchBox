package org.apache.commons.lang3.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;

public class DurationFormatUtils {

    public static final String ISO_EXTENDED_FORMAT_PATTERN = "\'P\'yyyy\'Y\'M\'M\'d\'DT\'H\'H\'m\'M\'s.S\'S\'";
    static final Object y = "y";
    static final Object M = "M";
    static final Object d = "d";
    static final Object H = "H";
    static final Object m = "m";
    static final Object s = "s";
    static final Object S = "S";

    public static String formatDurationHMS(long i) {
        return formatDuration(i, "H:mm:ss.SSS");
    }

    public static String formatDurationISO(long i) {
        return formatDuration(i, "\'P\'yyyy\'Y\'M\'M\'d\'DT\'H\'H\'m\'M\'s.S\'S\'", false);
    }

    public static String formatDuration(long i, String s) {
        return formatDuration(i, s, true);
    }

    public static String formatDuration(long i, String s, boolean flag) {
        DurationFormatUtils.Token[] adurationformatutils_token = lexx(s);
        long j = 0L;
        long k = 0L;
        long l = 0L;
        long i1 = 0L;
        long j1 = i;

        if (DurationFormatUtils.Token.containsTokenWithValue(adurationformatutils_token, DurationFormatUtils.d)) {
            j = i / 86400000L;
            j1 = i - j * 86400000L;
        }

        if (DurationFormatUtils.Token.containsTokenWithValue(adurationformatutils_token, DurationFormatUtils.H)) {
            k = j1 / 3600000L;
            j1 -= k * 3600000L;
        }

        if (DurationFormatUtils.Token.containsTokenWithValue(adurationformatutils_token, DurationFormatUtils.m)) {
            l = j1 / 60000L;
            j1 -= l * 60000L;
        }

        if (DurationFormatUtils.Token.containsTokenWithValue(adurationformatutils_token, DurationFormatUtils.s)) {
            i1 = j1 / 1000L;
            j1 -= i1 * 1000L;
        }

        return format(adurationformatutils_token, 0L, 0L, j, k, l, i1, j1, flag);
    }

    public static String formatDurationWords(long i, boolean flag, boolean flag1) {
        String s = formatDuration(i, "d\' days \'H\' hours \'m\' minutes \'s\' seconds\'");
        String s1;

        if (flag) {
            s = " " + s;
            s1 = StringUtils.replaceOnce(s, " 0 days", "");
            if (s1.length() != s.length()) {
                s = s1;
                s1 = StringUtils.replaceOnce(s1, " 0 hours", "");
                if (s1.length() != s.length()) {
                    s1 = StringUtils.replaceOnce(s1, " 0 minutes", "");
                    s = s1;
                    if (s1.length() != s1.length()) {
                        s = StringUtils.replaceOnce(s1, " 0 seconds", "");
                    }
                }
            }

            if (s.length() != 0) {
                s = s.substring(1);
            }
        }

        if (flag1) {
            s1 = StringUtils.replaceOnce(s, " 0 seconds", "");
            if (s1.length() != s.length()) {
                s = s1;
                s1 = StringUtils.replaceOnce(s1, " 0 minutes", "");
                if (s1.length() != s.length()) {
                    s = s1;
                    s1 = StringUtils.replaceOnce(s1, " 0 hours", "");
                    if (s1.length() != s.length()) {
                        s = StringUtils.replaceOnce(s1, " 0 days", "");
                    }
                }
            }
        }

        s = " " + s;
        s = StringUtils.replaceOnce(s, " 1 seconds", " 1 second");
        s = StringUtils.replaceOnce(s, " 1 minutes", " 1 minute");
        s = StringUtils.replaceOnce(s, " 1 hours", " 1 hour");
        s = StringUtils.replaceOnce(s, " 1 days", " 1 day");
        return s.trim();
    }

    public static String formatPeriodISO(long i, long j) {
        return formatPeriod(i, j, "\'P\'yyyy\'Y\'M\'M\'d\'DT\'H\'H\'m\'M\'s.S\'S\'", false, TimeZone.getDefault());
    }

    public static String formatPeriod(long i, long j, String s) {
        return formatPeriod(i, j, s, true, TimeZone.getDefault());
    }

    public static String formatPeriod(long i, long j, String s, boolean flag, TimeZone timezone) {
        DurationFormatUtils.Token[] adurationformatutils_token = lexx(s);
        Calendar calendar = Calendar.getInstance(timezone);

        calendar.setTime(new Date(i));
        Calendar calendar1 = Calendar.getInstance(timezone);

        calendar1.setTime(new Date(j));
        int k = calendar1.get(14) - calendar.get(14);
        int l = calendar1.get(13) - calendar.get(13);
        int i1 = calendar1.get(12) - calendar.get(12);
        int j1 = calendar1.get(11) - calendar.get(11);
        int k1 = calendar1.get(5) - calendar.get(5);
        int l1 = calendar1.get(2) - calendar.get(2);

        int i2;

        for (i2 = calendar1.get(1) - calendar.get(1); k < 0; --l) {
            k += 1000;
        }

        while (l < 0) {
            l += 60;
            --i1;
        }

        while (i1 < 0) {
            i1 += 60;
            --j1;
        }

        while (j1 < 0) {
            j1 += 24;
            --k1;
        }

        if (DurationFormatUtils.Token.containsTokenWithValue(adurationformatutils_token, DurationFormatUtils.M)) {
            while (k1 < 0) {
                k1 += calendar.getActualMaximum(5);
                --l1;
                calendar.add(2, 1);
            }

            while (l1 < 0) {
                l1 += 12;
                --i2;
            }

            if (!DurationFormatUtils.Token.containsTokenWithValue(adurationformatutils_token, DurationFormatUtils.y) && i2 != 0) {
                while (i2 != 0) {
                    l1 += 12 * i2;
                    i2 = 0;
                }
            }
        } else {
            if (!DurationFormatUtils.Token.containsTokenWithValue(adurationformatutils_token, DurationFormatUtils.y)) {
                int j2 = calendar1.get(1);

                if (l1 < 0) {
                    --j2;
                }

                while (calendar.get(1) != j2) {
                    k1 += calendar.getActualMaximum(6) - calendar.get(6);
                    if (calendar instanceof GregorianCalendar && calendar.get(2) == 1 && calendar.get(5) == 29) {
                        ++k1;
                    }

                    calendar.add(1, 1);
                    k1 += calendar.get(6);
                }

                i2 = 0;
            }

            while (calendar.get(2) != calendar1.get(2)) {
                k1 += calendar.getActualMaximum(5);
                calendar.add(2, 1);
            }

            l1 = 0;

            while (k1 < 0) {
                k1 += calendar.getActualMaximum(5);
                --l1;
                calendar.add(2, 1);
            }
        }

        if (!DurationFormatUtils.Token.containsTokenWithValue(adurationformatutils_token, DurationFormatUtils.d)) {
            j1 += 24 * k1;
            k1 = 0;
        }

        if (!DurationFormatUtils.Token.containsTokenWithValue(adurationformatutils_token, DurationFormatUtils.H)) {
            i1 += 60 * j1;
            j1 = 0;
        }

        if (!DurationFormatUtils.Token.containsTokenWithValue(adurationformatutils_token, DurationFormatUtils.m)) {
            l += 60 * i1;
            i1 = 0;
        }

        if (!DurationFormatUtils.Token.containsTokenWithValue(adurationformatutils_token, DurationFormatUtils.s)) {
            k += 1000 * l;
            l = 0;
        }

        return format(adurationformatutils_token, (long) i2, (long) l1, (long) k1, (long) j1, (long) i1, (long) l, (long) k, flag);
    }

    static String format(DurationFormatUtils.Token[] adurationformatutils_token, long i, long j, long k, long l, long i1, long j1, long k1, boolean flag) {
        StringBuilder stringbuilder = new StringBuilder();
        boolean flag1 = false;
        int l1 = adurationformatutils_token.length;

        for (int i2 = 0; i2 < l1; ++i2) {
            DurationFormatUtils.Token durationformatutils_token = adurationformatutils_token[i2];
            Object object = durationformatutils_token.getValue();
            int j2 = durationformatutils_token.getCount();

            if (object instanceof StringBuilder) {
                stringbuilder.append(object.toString());
            } else if (object == DurationFormatUtils.y) {
                stringbuilder.append(paddedValue(i, flag, j2));
                flag1 = false;
            } else if (object == DurationFormatUtils.M) {
                stringbuilder.append(paddedValue(j, flag, j2));
                flag1 = false;
            } else if (object == DurationFormatUtils.d) {
                stringbuilder.append(paddedValue(k, flag, j2));
                flag1 = false;
            } else if (object == DurationFormatUtils.H) {
                stringbuilder.append(paddedValue(l, flag, j2));
                flag1 = false;
            } else if (object == DurationFormatUtils.m) {
                stringbuilder.append(paddedValue(i1, flag, j2));
                flag1 = false;
            } else if (object == DurationFormatUtils.s) {
                stringbuilder.append(paddedValue(j1, flag, j2));
                flag1 = true;
            } else if (object == DurationFormatUtils.S) {
                if (flag1) {
                    int k2 = flag ? Math.max(3, j2) : 3;

                    stringbuilder.append(paddedValue(k1, true, k2));
                } else {
                    stringbuilder.append(paddedValue(k1, flag, j2));
                }

                flag1 = false;
            }
        }

        return stringbuilder.toString();
    }

    private static String paddedValue(long i, boolean flag, int j) {
        String s = Long.toString(i);

        return flag ? StringUtils.leftPad(s, j, '0') : s;
    }

    static DurationFormatUtils.Token[] lexx(String s) {
        ArrayList arraylist = new ArrayList(s.length());
        boolean flag = false;
        StringBuilder stringbuilder = null;
        DurationFormatUtils.Token durationformatutils_token = null;

        for (int i = 0; i < s.length(); ++i) {
            char c0 = s.charAt(i);

            if (flag && c0 != 39) {
                stringbuilder.append(c0);
            } else {
                Object object = null;

                switch (c0) {
                case '\'':
                    if (flag) {
                        stringbuilder = null;
                        flag = false;
                    } else {
                        stringbuilder = new StringBuilder();
                        arraylist.add(new DurationFormatUtils.Token(stringbuilder));
                        flag = true;
                    }
                    break;

                case 'H':
                    object = DurationFormatUtils.H;
                    break;

                case 'M':
                    object = DurationFormatUtils.M;
                    break;

                case 'S':
                    object = DurationFormatUtils.S;
                    break;

                case 'd':
                    object = DurationFormatUtils.d;
                    break;

                case 'm':
                    object = DurationFormatUtils.m;
                    break;

                case 's':
                    object = DurationFormatUtils.s;
                    break;

                case 'y':
                    object = DurationFormatUtils.y;
                    break;

                default:
                    if (stringbuilder == null) {
                        stringbuilder = new StringBuilder();
                        arraylist.add(new DurationFormatUtils.Token(stringbuilder));
                    }

                    stringbuilder.append(c0);
                }

                if (object != null) {
                    if (durationformatutils_token != null && durationformatutils_token.getValue() == object) {
                        durationformatutils_token.increment();
                    } else {
                        DurationFormatUtils.Token durationformatutils_token1 = new DurationFormatUtils.Token(object);

                        arraylist.add(durationformatutils_token1);
                        durationformatutils_token = durationformatutils_token1;
                    }

                    stringbuilder = null;
                }
            }
        }

        if (flag) {
            throw new IllegalArgumentException("Unmatched quote in format: " + s);
        } else {
            return (DurationFormatUtils.Token[]) arraylist.toArray(new DurationFormatUtils.Token[arraylist.size()]);
        }
    }

    static class Token {

        private final Object value;
        private int count;

        static boolean containsTokenWithValue(DurationFormatUtils.Token[] adurationformatutils_token, Object object) {
            int i = adurationformatutils_token.length;

            for (int j = 0; j < i; ++j) {
                if (adurationformatutils_token[j].getValue() == object) {
                    return true;
                }
            }

            return false;
        }

        Token(Object object) {
            this.value = object;
            this.count = 1;
        }

        Token(Object object, int i) {
            this.value = object;
            this.count = i;
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

        public boolean equals(Object object) {
            if (object instanceof DurationFormatUtils.Token) {
                DurationFormatUtils.Token durationformatutils_token = (DurationFormatUtils.Token) object;

                return this.value.getClass() != durationformatutils_token.value.getClass() ? false : (this.count != durationformatutils_token.count ? false : (this.value instanceof StringBuilder ? this.value.toString().equals(durationformatutils_token.value.toString()) : (this.value instanceof Number ? this.value.equals(durationformatutils_token.value) : this.value == durationformatutils_token.value)));
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
