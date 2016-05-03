package org.apache.commons.lang.time;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;

public class DateUtils {

    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
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
    /** @deprecated */
    public static final int MILLIS_IN_SECOND = 1000;
    /** @deprecated */
    public static final int MILLIS_IN_MINUTE = 60000;
    /** @deprecated */
    public static final int MILLIS_IN_HOUR = 3600000;
    /** @deprecated */
    public static final int MILLIS_IN_DAY = 86400000;

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();

            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();

            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameInstant(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return date1.getTime() == date2.getTime();
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameInstant(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.getTime().getTime() == cal2.getTime().getTime();
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(14) == cal2.get(14) && cal1.get(13) == cal2.get(13) && cal1.get(12) == cal2.get(12) && cal1.get(10) == cal2.get(10) && cal1.get(6) == cal2.get(6) && cal1.get(1) == cal2.get(1) && cal1.get(0) == cal2.get(0) && cal1.getClass() == cal2.getClass();
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static Date parseDate(String str, String[] parsePatterns) throws ParseException {
        return parseDateWithLeniency(str, parsePatterns, true);
    }

    public static Date parseDateStrictly(String str, String[] parsePatterns) throws ParseException {
        return parseDateWithLeniency(str, parsePatterns, false);
    }

    private static Date parseDateWithLeniency(String str, String[] parsePatterns, boolean lenient) throws ParseException {
        if (str != null && parsePatterns != null) {
            SimpleDateFormat parser = new SimpleDateFormat();

            parser.setLenient(lenient);
            ParsePosition pos = new ParsePosition(0);

            for (int i = 0; i < parsePatterns.length; ++i) {
                String pattern = parsePatterns[i];

                if (parsePatterns[i].endsWith("ZZ")) {
                    pattern = pattern.substring(0, pattern.length() - 1);
                }

                parser.applyPattern(pattern);
                pos.setIndex(0);
                String str2 = str;

                if (parsePatterns[i].endsWith("ZZ")) {
                    for (int date = indexOfSignChars(str, 0); date >= 0; date = indexOfSignChars(str2, date)) {
                        str2 = reformatTimezone(str2, date);
                        ++date;
                    }
                }

                Date date = parser.parse(str2, pos);

                if (date != null && pos.getIndex() == str2.length()) {
                    return date;
                }
            }

            throw new ParseException("Unable to parse the date: " + str, -1);
        } else {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
    }

    private static int indexOfSignChars(String str, int startPos) {
        int idx = StringUtils.indexOf(str, '+', startPos);

        if (idx < 0) {
            idx = StringUtils.indexOf(str, '-', startPos);
        }

        return idx;
    }

    private static String reformatTimezone(String str, int signIdx) {
        String str2 = str;

        if (signIdx >= 0 && signIdx + 5 < str.length() && Character.isDigit(str.charAt(signIdx + 1)) && Character.isDigit(str.charAt(signIdx + 2)) && str.charAt(signIdx + 3) == 58 && Character.isDigit(str.charAt(signIdx + 4)) && Character.isDigit(str.charAt(signIdx + 5))) {
            str2 = str.substring(0, signIdx + 3) + str.substring(signIdx + 4);
        }

        return str2;
    }

    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        return add(date, 3, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    public static Date addMilliseconds(Date date, int amount) {
        return add(date, 14, amount);
    }

    /** @deprecated */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();

            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    public static Date setYears(Date date, int amount) {
        return set(date, 1, amount);
    }

    public static Date setMonths(Date date, int amount) {
        return set(date, 2, amount);
    }

    public static Date setDays(Date date, int amount) {
        return set(date, 5, amount);
    }

    public static Date setHours(Date date, int amount) {
        return set(date, 11, amount);
    }

    public static Date setMinutes(Date date, int amount) {
        return set(date, 12, amount);
    }

    public static Date setSeconds(Date date, int amount) {
        return set(date, 13, amount);
    }

    public static Date setMilliseconds(Date date, int amount) {
        return set(date, 14, amount);
    }

    private static Date set(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();

            c.setLenient(false);
            c.setTime(date);
            c.set(calendarField, amount);
            return c.getTime();
        }
    }

    public static Calendar toCalendar(Date date) {
        Calendar c = Calendar.getInstance();

        c.setTime(date);
        return c;
    }

    public static Date round(Date date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar gval = Calendar.getInstance();

            gval.setTime(date);
            modify(gval, field, 1);
            return gval.getTime();
        }
    }

    public static Calendar round(Calendar date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar rounded = (Calendar) date.clone();

            modify(rounded, field, 1);
            return rounded;
        }
    }

    public static Date round(Object date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (date instanceof Date) {
            return round((Date) date, field);
        } else if (date instanceof Calendar) {
            return round((Calendar) date, field).getTime();
        } else {
            throw new ClassCastException("Could not round " + date);
        }
    }

    public static Date truncate(Date date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar gval = Calendar.getInstance();

            gval.setTime(date);
            modify(gval, field, 0);
            return gval.getTime();
        }
    }

    public static Calendar truncate(Calendar date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar truncated = (Calendar) date.clone();

            modify(truncated, field, 0);
            return truncated;
        }
    }

    public static Date truncate(Object date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (date instanceof Date) {
            return truncate((Date) date, field);
        } else if (date instanceof Calendar) {
            return truncate((Calendar) date, field).getTime();
        } else {
            throw new ClassCastException("Could not truncate " + date);
        }
    }

    public static Date ceiling(Date date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar gval = Calendar.getInstance();

            gval.setTime(date);
            modify(gval, field, 2);
            return gval.getTime();
        }
    }

    public static Calendar ceiling(Calendar date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar ceiled = (Calendar) date.clone();

            modify(ceiled, field, 2);
            return ceiled;
        }
    }

    public static Date ceiling(Object date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (date instanceof Date) {
            return ceiling((Date) date, field);
        } else if (date instanceof Calendar) {
            return ceiling((Calendar) date, field).getTime();
        } else {
            throw new ClassCastException("Could not find ceiling of for type: " + date.getClass());
        }
    }

    private static void modify(Calendar val, int field, int modType) {
        if (val.get(1) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        } else if (field != 14) {
            Date date = val.getTime();
            long time = date.getTime();
            boolean done = false;
            int millisecs = val.get(14);

            if (0 == modType || millisecs < 500) {
                time -= (long) millisecs;
            }

            if (field == 13) {
                done = true;
            }

            int seconds = val.get(13);

            if (!done && (0 == modType || seconds < 30)) {
                time -= (long) seconds * 1000L;
            }

            if (field == 12) {
                done = true;
            }

            int minutes = val.get(12);

            if (!done && (0 == modType || minutes < 30)) {
                time -= (long) minutes * 60000L;
            }

            if (date.getTime() != time) {
                date.setTime(time);
                val.setTime(date);
            }

            boolean roundUp = false;

            for (int i = 0; i < DateUtils.fields.length; ++i) {
                int offset;

                for (offset = 0; offset < DateUtils.fields[i].length; ++offset) {
                    if (DateUtils.fields[i][offset] == field) {
                        if (modType == 2 || modType == 1 && roundUp) {
                            if (field == 1001) {
                                if (val.get(5) == 1) {
                                    val.add(5, 15);
                                } else {
                                    val.add(5, -15);
                                    val.add(2, 1);
                                }
                            } else if (field == 9) {
                                if (val.get(11) == 0) {
                                    val.add(11, 12);
                                } else {
                                    val.add(11, -12);
                                    val.add(5, 1);
                                }
                            } else {
                                val.add(DateUtils.fields[i][0], 1);
                            }
                        }

                        return;
                    }
                }

                offset = 0;
                boolean offsetSet = false;

                switch (field) {
                case 9:
                    if (DateUtils.fields[i][0] == 11) {
                        offset = val.get(11);
                        if (offset >= 12) {
                            offset -= 12;
                        }

                        roundUp = offset >= 6;
                        offsetSet = true;
                    }
                    break;

                case 1001:
                    if (DateUtils.fields[i][0] == 5) {
                        offset = val.get(5) - 1;
                        if (offset >= 15) {
                            offset -= 15;
                        }

                        roundUp = offset > 7;
                        offsetSet = true;
                    }
                }

                if (!offsetSet) {
                    int min = val.getActualMinimum(DateUtils.fields[i][0]);
                    int max = val.getActualMaximum(DateUtils.fields[i][0]);

                    offset = val.get(DateUtils.fields[i][0]) - min;
                    roundUp = offset > (max - min) / 2;
                }

                if (offset != 0) {
                    val.set(DateUtils.fields[i][0], val.get(DateUtils.fields[i][0]) - offset);
                }
            }

            throw new IllegalArgumentException("The field " + field + " is not supported");
        }
    }

    public static Iterator iterator(Date focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar gval = Calendar.getInstance();

            gval.setTime(focus);
            return iterator(gval, rangeStyle);
        }
    }

    public static Iterator iterator(Calendar focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar start;
            Calendar end;
            int startCutoff;
            int endCutoff;

            start = null;
            end = null;
            startCutoff = 1;
            endCutoff = 7;
            label40:
            switch (rangeStyle) {
            case 1:
            case 2:
            case 3:
            case 4:
                start = truncate(focus, 5);
                end = truncate(focus, 5);
                switch (rangeStyle) {
                case 1:
                default:
                    break label40;

                case 2:
                    startCutoff = 2;
                    endCutoff = 1;
                    break label40;

                case 3:
                    startCutoff = focus.get(7);
                    endCutoff = startCutoff - 1;
                    break label40;

                case 4:
                    startCutoff = focus.get(7) - 3;
                    endCutoff = focus.get(7) + 3;
                    break label40;
                }

            case 5:
            case 6:
                start = truncate(focus, 2);
                end = (Calendar) start.clone();
                end.add(2, 1);
                end.add(5, -1);
                if (rangeStyle == 6) {
                    startCutoff = 2;
                    endCutoff = 1;
                }
                break;

            default:
                throw new IllegalArgumentException("The range style " + rangeStyle + " is not valid.");
            }

            if (startCutoff < 1) {
                startCutoff += 7;
            }

            if (startCutoff > 7) {
                startCutoff -= 7;
            }

            if (endCutoff < 1) {
                endCutoff += 7;
            }

            if (endCutoff > 7) {
                endCutoff -= 7;
            }

            while (start.get(7) != startCutoff) {
                start.add(5, -1);
            }

            while (end.get(7) != endCutoff) {
                end.add(5, 1);
            }

            return new DateUtils.DateIterator(start, end);
        }
    }

    public static Iterator iterator(Object focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (focus instanceof Date) {
            return iterator((Date) focus, rangeStyle);
        } else if (focus instanceof Calendar) {
            return iterator((Calendar) focus, rangeStyle);
        } else {
            throw new ClassCastException("Could not iterate based on " + focus);
        }
    }

    public static long getFragmentInMilliseconds(Date date, int fragment) {
        return getFragment(date, fragment, 14);
    }

    public static long getFragmentInSeconds(Date date, int fragment) {
        return getFragment(date, fragment, 13);
    }

    public static long getFragmentInMinutes(Date date, int fragment) {
        return getFragment(date, fragment, 12);
    }

    public static long getFragmentInHours(Date date, int fragment) {
        return getFragment(date, fragment, 11);
    }

    public static long getFragmentInDays(Date date, int fragment) {
        return getFragment(date, fragment, 6);
    }

    public static long getFragmentInMilliseconds(Calendar calendar, int fragment) {
        return getFragment(calendar, fragment, 14);
    }

    public static long getFragmentInSeconds(Calendar calendar, int fragment) {
        return getFragment(calendar, fragment, 13);
    }

    public static long getFragmentInMinutes(Calendar calendar, int fragment) {
        return getFragment(calendar, fragment, 12);
    }

    public static long getFragmentInHours(Calendar calendar, int fragment) {
        return getFragment(calendar, fragment, 11);
    }

    public static long getFragmentInDays(Calendar calendar, int fragment) {
        return getFragment(calendar, fragment, 6);
    }

    private static long getFragment(Date date, int fragment, int unit) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);
            return getFragment(calendar, fragment, unit);
        }
    }

    private static long getFragment(Calendar calendar, int fragment, int unit) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            long millisPerUnit = getMillisPerUnit(unit);
            long result = 0L;

            switch (fragment) {
            case 1:
                result += (long) calendar.get(6) * 86400000L / millisPerUnit;
                break;

            case 2:
                result += (long) calendar.get(5) * 86400000L / millisPerUnit;
            }

            switch (fragment) {
            case 1:
            case 2:
            case 5:
            case 6:
                result += (long) calendar.get(11) * 3600000L / millisPerUnit;

            case 11:
                result += (long) calendar.get(12) * 60000L / millisPerUnit;

            case 12:
                result += (long) calendar.get(13) * 1000L / millisPerUnit;

            case 13:
                result += (long) (calendar.get(14) * 1) / millisPerUnit;

            case 14:
                return result;

            case 3:
            case 4:
            case 7:
            case 8:
            case 9:
            case 10:
            default:
                throw new IllegalArgumentException("The fragment " + fragment + " is not supported");
            }
        }
    }

    public static boolean truncatedEquals(Calendar cal1, Calendar cal2, int field) {
        return truncatedCompareTo(cal1, cal2, field) == 0;
    }

    public static boolean truncatedEquals(Date date1, Date date2, int field) {
        return truncatedCompareTo(date1, date2, field) == 0;
    }

    public static int truncatedCompareTo(Calendar cal1, Calendar cal2, int field) {
        Calendar truncatedCal1 = truncate(cal1, field);
        Calendar truncatedCal2 = truncate(cal2, field);

        return truncatedCal1.getTime().compareTo(truncatedCal2.getTime());
    }

    public static int truncatedCompareTo(Date date1, Date date2, int field) {
        Date truncatedDate1 = truncate(date1, field);
        Date truncatedDate2 = truncate(date2, field);

        return truncatedDate1.compareTo(truncatedDate2);
    }

    private static long getMillisPerUnit(int unit) {
        long result = Long.MAX_VALUE;

        switch (unit) {
        case 5:
        case 6:
            result = 86400000L;
            break;

        case 7:
        case 8:
        case 9:
        case 10:
        default:
            throw new IllegalArgumentException("The unit " + unit + " cannot be represented is milleseconds");

        case 11:
            result = 3600000L;
            break;

        case 12:
            result = 60000L;
            break;

        case 13:
            result = 1000L;
            break;

        case 14:
            result = 1L;
        }

        return result;
    }

    static class DateIterator implements Iterator {

        private final Calendar endFinal;
        private final Calendar spot;

        DateIterator(Calendar startFinal, Calendar endFinal) {
            this.endFinal = endFinal;
            this.spot = startFinal;
            this.spot.add(5, -1);
        }

        public boolean hasNext() {
            return this.spot.before(this.endFinal);
        }

        public Object next() {
            if (this.spot.equals(this.endFinal)) {
                throw new NoSuchElementException();
            } else {
                this.spot.add(5, 1);
                return this.spot.clone();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
