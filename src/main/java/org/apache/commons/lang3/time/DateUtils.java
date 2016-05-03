package org.apache.commons.lang3.time;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static final long MILLIS_PER_SECOND = 1000L;
    public static final long MILLIS_PER_MINUTE = 60000L;
    public static final long MILLIS_PER_HOUR = 3600000L;
    public static final long MILLIS_PER_DAY = 86400000L;
    public static final int SEMI_MONTH = 1001;
    private static final int[][] fields = new int[][] { { 14}, { 13}, { 12}, { 11, 10}, { 5, 5, 9}, { 2, 1001}, { 1}, { 0}};
    public static final int RANGE_WEEK_SUNDAY = 1;
    public static final int RANGE_WEEK_MONDAY = 2;
    public static final int RANGE_WEEK_RELATIVE = 3;
    public static final int RANGE_WEEK_CENTER = 4;
    public static final int RANGE_MONTH_SUNDAY = 5;
    public static final int RANGE_MONTH_MONDAY = 6;
    private static final int MODIFY_TRUNCATE = 0;
    private static final int MODIFY_ROUND = 1;
    private static final int MODIFY_CEILING = 2;

    public static boolean isSameDay(Date date, Date date1) {
        if (date != null && date1 != null) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);
            Calendar calendar1 = Calendar.getInstance();

            calendar1.setTime(date1);
            return isSameDay(calendar, calendar1);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameDay(Calendar calendar, Calendar calendar1) {
        if (calendar != null && calendar1 != null) {
            return calendar.get(0) == calendar1.get(0) && calendar.get(1) == calendar1.get(1) && calendar.get(6) == calendar1.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameInstant(Date date, Date date1) {
        if (date != null && date1 != null) {
            return date.getTime() == date1.getTime();
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameInstant(Calendar calendar, Calendar calendar1) {
        if (calendar != null && calendar1 != null) {
            return calendar.getTime().getTime() == calendar1.getTime().getTime();
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameLocalTime(Calendar calendar, Calendar calendar1) {
        if (calendar != null && calendar1 != null) {
            return calendar.get(14) == calendar1.get(14) && calendar.get(13) == calendar1.get(13) && calendar.get(12) == calendar1.get(12) && calendar.get(11) == calendar1.get(11) && calendar.get(6) == calendar1.get(6) && calendar.get(1) == calendar1.get(1) && calendar.get(0) == calendar1.get(0) && calendar.getClass() == calendar1.getClass();
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static Date parseDate(String s, String... astring) throws ParseException {
        return parseDate(s, (Locale) null, astring);
    }

    public static Date parseDate(String s, Locale locale, String... astring) throws ParseException {
        return parseDateWithLeniency(s, locale, astring, true);
    }

    public static Date parseDateStrictly(String s, String... astring) throws ParseException {
        return parseDateStrictly(s, (Locale) null, astring);
    }

    public static Date parseDateStrictly(String s, Locale locale, String... astring) throws ParseException {
        return parseDateWithLeniency(s, (Locale) null, astring, false);
    }

    private static Date parseDateWithLeniency(String s, Locale locale, String[] astring, boolean flag) throws ParseException {
        if (s != null && astring != null) {
            SimpleDateFormat simpledateformat;

            if (locale == null) {
                simpledateformat = new SimpleDateFormat();
            } else {
                simpledateformat = new SimpleDateFormat("", locale);
            }

            simpledateformat.setLenient(flag);
            ParsePosition parseposition = new ParsePosition(0);
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s1 = astring1[j];
                String s2 = s1;

                if (s1.endsWith("ZZ")) {
                    s2 = s1.substring(0, s1.length() - 1);
                }

                simpledateformat.applyPattern(s2);
                parseposition.setIndex(0);
                String s3 = s;

                if (s1.endsWith("ZZ")) {
                    s3 = s.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2");
                }

                Date date = simpledateformat.parse(s3, parseposition);

                if (date != null && parseposition.getIndex() == s3.length()) {
                    return date;
                }
            }

            throw new ParseException("Unable to parse the date: " + s, -1);
        } else {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
    }

    public static Date addYears(Date date, int i) {
        return add(date, 1, i);
    }

    public static Date addMonths(Date date, int i) {
        return add(date, 2, i);
    }

    public static Date addWeeks(Date date, int i) {
        return add(date, 3, i);
    }

    public static Date addDays(Date date, int i) {
        return add(date, 5, i);
    }

    public static Date addHours(Date date, int i) {
        return add(date, 11, i);
    }

    public static Date addMinutes(Date date, int i) {
        return add(date, 12, i);
    }

    public static Date addSeconds(Date date, int i) {
        return add(date, 13, i);
    }

    public static Date addMilliseconds(Date date, int i) {
        return add(date, 14, i);
    }

    private static Date add(Date date, int i, int j) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);
            calendar.add(i, j);
            return calendar.getTime();
        }
    }

    public static Date setYears(Date date, int i) {
        return set(date, 1, i);
    }

    public static Date setMonths(Date date, int i) {
        return set(date, 2, i);
    }

    public static Date setDays(Date date, int i) {
        return set(date, 5, i);
    }

    public static Date setHours(Date date, int i) {
        return set(date, 11, i);
    }

    public static Date setMinutes(Date date, int i) {
        return set(date, 12, i);
    }

    public static Date setSeconds(Date date, int i) {
        return set(date, 13, i);
    }

    public static Date setMilliseconds(Date date, int i) {
        return set(date, 14, i);
    }

    private static Date set(Date date, int i, int j) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar = Calendar.getInstance();

            calendar.setLenient(false);
            calendar.setTime(date);
            calendar.set(i, j);
            return calendar.getTime();
        }
    }

    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        return calendar;
    }

    public static Date round(Date date, int i) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);
            modify(calendar, i, 1);
            return calendar.getTime();
        }
    }

    public static Calendar round(Calendar calendar, int i) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar1 = (Calendar) calendar.clone();

            modify(calendar1, i, 1);
            return calendar1;
        }
    }

    public static Date round(Object object, int i) {
        if (object == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (object instanceof Date) {
            return round((Date) object, i);
        } else if (object instanceof Calendar) {
            return round((Calendar) object, i).getTime();
        } else {
            throw new ClassCastException("Could not round " + object);
        }
    }

    public static Date truncate(Date date, int i) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);
            modify(calendar, i, 0);
            return calendar.getTime();
        }
    }

    public static Calendar truncate(Calendar calendar, int i) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar1 = (Calendar) calendar.clone();

            modify(calendar1, i, 0);
            return calendar1;
        }
    }

    public static Date truncate(Object object, int i) {
        if (object == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (object instanceof Date) {
            return truncate((Date) object, i);
        } else if (object instanceof Calendar) {
            return truncate((Calendar) object, i).getTime();
        } else {
            throw new ClassCastException("Could not truncate " + object);
        }
    }

    public static Date ceiling(Date date, int i) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);
            modify(calendar, i, 2);
            return calendar.getTime();
        }
    }

    public static Calendar ceiling(Calendar calendar, int i) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar1 = (Calendar) calendar.clone();

            modify(calendar1, i, 2);
            return calendar1;
        }
    }

    public static Date ceiling(Object object, int i) {
        if (object == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (object instanceof Date) {
            return ceiling((Date) object, i);
        } else if (object instanceof Calendar) {
            return ceiling((Calendar) object, i).getTime();
        } else {
            throw new ClassCastException("Could not find ceiling of for type: " + object.getClass());
        }
    }

    private static void modify(Calendar calendar, int i, int j) {
        if (calendar.get(1) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        } else if (i != 14) {
            Date date = calendar.getTime();
            long k = date.getTime();
            boolean flag = false;
            int l = calendar.get(14);

            if (0 == j || l < 500) {
                k -= (long) l;
            }

            if (i == 13) {
                flag = true;
            }

            int i1 = calendar.get(13);

            if (!flag && (0 == j || i1 < 30)) {
                k -= (long) i1 * 1000L;
            }

            if (i == 12) {
                flag = true;
            }

            int j1 = calendar.get(12);

            if (!flag && (0 == j || j1 < 30)) {
                k -= (long) j1 * 60000L;
            }

            if (date.getTime() != k) {
                date.setTime(k);
                calendar.setTime(date);
            }

            boolean flag1 = false;
            int[][] aint = DateUtils.fields;
            int k1 = aint.length;

            for (int l1 = 0; l1 < k1; ++l1) {
                int[] aint1 = aint[l1];
                int[] aint2 = aint1;
                int i2 = aint1.length;

                int j2;
                int k2;

                for (j2 = 0; j2 < i2; ++j2) {
                    k2 = aint2[j2];
                    if (k2 == i) {
                        if (j == 2 || j == 1 && flag1) {
                            if (i == 1001) {
                                if (calendar.get(5) == 1) {
                                    calendar.add(5, 15);
                                } else {
                                    calendar.add(5, -15);
                                    calendar.add(2, 1);
                                }
                            } else if (i == 9) {
                                if (calendar.get(11) == 0) {
                                    calendar.add(11, 12);
                                } else {
                                    calendar.add(11, -12);
                                    calendar.add(5, 1);
                                }
                            } else {
                                calendar.add(aint1[0], 1);
                            }
                        }

                        return;
                    }
                }

                int l2 = 0;
                boolean flag2 = false;

                switch (i) {
                case 9:
                    if (aint1[0] == 11) {
                        l2 = calendar.get(11);
                        if (l2 >= 12) {
                            l2 -= 12;
                        }

                        flag1 = l2 >= 6;
                        flag2 = true;
                    }
                    break;

                case 1001:
                    if (aint1[0] == 5) {
                        l2 = calendar.get(5) - 1;
                        if (l2 >= 15) {
                            l2 -= 15;
                        }

                        flag1 = l2 > 7;
                        flag2 = true;
                    }
                }

                if (!flag2) {
                    j2 = calendar.getActualMinimum(aint1[0]);
                    k2 = calendar.getActualMaximum(aint1[0]);
                    l2 = calendar.get(aint1[0]) - j2;
                    flag1 = l2 > (k2 - j2) / 2;
                }

                if (l2 != 0) {
                    calendar.set(aint1[0], calendar.get(aint1[0]) - l2);
                }
            }

            throw new IllegalArgumentException("The field " + i + " is not supported");
        }
    }

    public static Iterator iterator(Date date, int i) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);
            return iterator(calendar, i);
        }
    }

    public static Iterator iterator(Calendar calendar, int i) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar1;
            Calendar calendar2;
            int j;
            int k;

            calendar1 = null;
            calendar2 = null;
            j = 1;
            k = 7;
            label40:
            switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
                calendar1 = truncate(calendar, 5);
                calendar2 = truncate(calendar, 5);
                switch (i) {
                case 1:
                default:
                    break label40;

                case 2:
                    j = 2;
                    k = 1;
                    break label40;

                case 3:
                    j = calendar.get(7);
                    k = j - 1;
                    break label40;

                case 4:
                    j = calendar.get(7) - 3;
                    k = calendar.get(7) + 3;
                    break label40;
                }

            case 5:
            case 6:
                calendar1 = truncate(calendar, 2);
                calendar2 = (Calendar) calendar1.clone();
                calendar2.add(2, 1);
                calendar2.add(5, -1);
                if (i == 6) {
                    j = 2;
                    k = 1;
                }
                break;

            default:
                throw new IllegalArgumentException("The range style " + i + " is not valid.");
            }

            if (j < 1) {
                j += 7;
            }

            if (j > 7) {
                j -= 7;
            }

            if (k < 1) {
                k += 7;
            }

            if (k > 7) {
                k -= 7;
            }

            while (calendar1.get(7) != j) {
                calendar1.add(5, -1);
            }

            while (calendar2.get(7) != k) {
                calendar2.add(5, 1);
            }

            return new DateUtils.DateIterator(calendar1, calendar2);
        }
    }

    public static Iterator iterator(Object object, int i) {
        if (object == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (object instanceof Date) {
            return iterator((Date) object, i);
        } else if (object instanceof Calendar) {
            return iterator((Calendar) object, i);
        } else {
            throw new ClassCastException("Could not iterate based on " + object);
        }
    }

    public static long getFragmentInMilliseconds(Date date, int i) {
        return getFragment(date, i, TimeUnit.MILLISECONDS);
    }

    public static long getFragmentInSeconds(Date date, int i) {
        return getFragment(date, i, TimeUnit.SECONDS);
    }

    public static long getFragmentInMinutes(Date date, int i) {
        return getFragment(date, i, TimeUnit.MINUTES);
    }

    public static long getFragmentInHours(Date date, int i) {
        return getFragment(date, i, TimeUnit.HOURS);
    }

    public static long getFragmentInDays(Date date, int i) {
        return getFragment(date, i, TimeUnit.DAYS);
    }

    public static long getFragmentInMilliseconds(Calendar calendar, int i) {
        return getFragment(calendar, i, TimeUnit.MILLISECONDS);
    }

    public static long getFragmentInSeconds(Calendar calendar, int i) {
        return getFragment(calendar, i, TimeUnit.SECONDS);
    }

    public static long getFragmentInMinutes(Calendar calendar, int i) {
        return getFragment(calendar, i, TimeUnit.MINUTES);
    }

    public static long getFragmentInHours(Calendar calendar, int i) {
        return getFragment(calendar, i, TimeUnit.HOURS);
    }

    public static long getFragmentInDays(Calendar calendar, int i) {
        return getFragment(calendar, i, TimeUnit.DAYS);
    }

    private static long getFragment(Date date, int i, TimeUnit timeunit) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);
            return getFragment(calendar, i, timeunit);
        }
    }

    private static long getFragment(Calendar calendar, int i, TimeUnit timeunit) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            long j = 0L;
            int k = timeunit == TimeUnit.DAYS ? 0 : 1;

            switch (i) {
            case 1:
                j += timeunit.convert((long) (calendar.get(6) - k), TimeUnit.DAYS);
                break;

            case 2:
                j += timeunit.convert((long) (calendar.get(5) - k), TimeUnit.DAYS);
            }

            switch (i) {
            case 1:
            case 2:
            case 5:
            case 6:
                j += timeunit.convert((long) calendar.get(11), TimeUnit.HOURS);

            case 11:
                j += timeunit.convert((long) calendar.get(12), TimeUnit.MINUTES);

            case 12:
                j += timeunit.convert((long) calendar.get(13), TimeUnit.SECONDS);

            case 13:
                j += timeunit.convert((long) calendar.get(14), TimeUnit.MILLISECONDS);

            case 14:
                return j;

            case 3:
            case 4:
            case 7:
            case 8:
            case 9:
            case 10:
            default:
                throw new IllegalArgumentException("The fragment " + i + " is not supported");
            }
        }
    }

    public static boolean truncatedEquals(Calendar calendar, Calendar calendar1, int i) {
        return truncatedCompareTo(calendar, calendar1, i) == 0;
    }

    public static boolean truncatedEquals(Date date, Date date1, int i) {
        return truncatedCompareTo(date, date1, i) == 0;
    }

    public static int truncatedCompareTo(Calendar calendar, Calendar calendar1, int i) {
        Calendar calendar2 = truncate(calendar, i);
        Calendar calendar3 = truncate(calendar1, i);

        return calendar2.compareTo(calendar3);
    }

    public static int truncatedCompareTo(Date date, Date date1, int i) {
        Date date2 = truncate(date, i);
        Date date3 = truncate(date1, i);

        return date2.compareTo(date3);
    }

    static class DateIterator implements Iterator {

        private final Calendar endFinal;
        private final Calendar spot;

        DateIterator(Calendar calendar, Calendar calendar1) {
            this.endFinal = calendar1;
            this.spot = calendar;
            this.spot.add(5, -1);
        }

        public boolean hasNext() {
            return this.spot.before(this.endFinal);
        }

        public Calendar next() {
            if (this.spot.equals(this.endFinal)) {
                throw new NoSuchElementException();
            } else {
                this.spot.add(5, 1);
                return (Calendar) this.spot.clone();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
