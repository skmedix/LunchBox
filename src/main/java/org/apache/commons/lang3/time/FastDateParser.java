package org.apache.commons.lang3.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastDateParser implements DateParser, Serializable {

    private static final long serialVersionUID = 2L;
    static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
    private final String pattern;
    private final TimeZone timeZone;
    private final Locale locale;
    private final int century;
    private final int startYear;
    private transient Pattern parsePattern;
    private transient FastDateParser.Strategy[] strategies;
    private transient String currentFormatField;
    private transient FastDateParser.Strategy nextStrategy;
    private static final Pattern formatPattern = Pattern.compile("D+|E+|F+|G+|H+|K+|M+|S+|W+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|\'\'|\'[^\']++(\'\'[^\']*+)*+\'|[^\'A-Za-z]++");
    private static final ConcurrentMap[] caches = new ConcurrentMap[17];
    private static final FastDateParser.Strategy ABBREVIATED_YEAR_STRATEGY = new FastDateParser.NumberStrategy(1) {
        void setCalendar(FastDateParser fastdateparser, Calendar calendar, String s) {
            int i = Integer.parseInt(s);

            if (i < 100) {
                i = fastdateparser.adjustYear(i);
            }

            calendar.set(1, i);
        }
    };
    private static final FastDateParser.Strategy NUMBER_MONTH_STRATEGY = new FastDateParser.NumberStrategy(2) {
        int modify(int i) {
            return i - 1;
        }
    };
    private static final FastDateParser.Strategy LITERAL_YEAR_STRATEGY = new FastDateParser.NumberStrategy(1);
    private static final FastDateParser.Strategy WEEK_OF_YEAR_STRATEGY = new FastDateParser.NumberStrategy(3);
    private static final FastDateParser.Strategy WEEK_OF_MONTH_STRATEGY = new FastDateParser.NumberStrategy(4);
    private static final FastDateParser.Strategy DAY_OF_YEAR_STRATEGY = new FastDateParser.NumberStrategy(6);
    private static final FastDateParser.Strategy DAY_OF_MONTH_STRATEGY = new FastDateParser.NumberStrategy(5);
    private static final FastDateParser.Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new FastDateParser.NumberStrategy(8);
    private static final FastDateParser.Strategy HOUR_OF_DAY_STRATEGY = new FastDateParser.NumberStrategy(11);
    private static final FastDateParser.Strategy MODULO_HOUR_OF_DAY_STRATEGY = new FastDateParser.NumberStrategy(11) {
        int modify(int i) {
            return i % 24;
        }
    };
    private static final FastDateParser.Strategy MODULO_HOUR_STRATEGY = new FastDateParser.NumberStrategy(10) {
        int modify(int i) {
            return i % 12;
        }
    };
    private static final FastDateParser.Strategy HOUR_STRATEGY = new FastDateParser.NumberStrategy(10);
    private static final FastDateParser.Strategy MINUTE_STRATEGY = new FastDateParser.NumberStrategy(12);
    private static final FastDateParser.Strategy SECOND_STRATEGY = new FastDateParser.NumberStrategy(13);
    private static final FastDateParser.Strategy MILLISECOND_STRATEGY = new FastDateParser.NumberStrategy(14);

    protected FastDateParser(String s, TimeZone timezone, Locale locale) {
        this(s, timezone, locale, (Date) null);
    }

    protected FastDateParser(String s, TimeZone timezone, Locale locale, Date date) {
        this.pattern = s;
        this.timeZone = timezone;
        this.locale = locale;
        Calendar calendar = Calendar.getInstance(timezone, locale);
        int i;

        if (date != null) {
            calendar.setTime(date);
            i = calendar.get(1);
        } else if (locale.equals(FastDateParser.JAPANESE_IMPERIAL)) {
            i = 0;
        } else {
            calendar.setTime(new Date());
            i = calendar.get(1) - 80;
        }

        this.century = i / 100 * 100;
        this.startYear = i - this.century;
        this.init(calendar);
    }

    private void init(Calendar calendar) {
        StringBuilder stringbuilder = new StringBuilder();
        ArrayList arraylist = new ArrayList();
        Matcher matcher = FastDateParser.formatPattern.matcher(this.pattern);

        if (!matcher.lookingAt()) {
            throw new IllegalArgumentException("Illegal pattern character \'" + this.pattern.charAt(matcher.regionStart()) + "\'");
        } else {
            this.currentFormatField = matcher.group();
            FastDateParser.Strategy fastdateparser_strategy = this.getStrategy(this.currentFormatField, calendar);

            while (true) {
                matcher.region(matcher.end(), matcher.regionEnd());
                if (!matcher.lookingAt()) {
                    this.nextStrategy = null;
                    if (matcher.regionStart() != matcher.regionEnd()) {
                        throw new IllegalArgumentException("Failed to parse \"" + this.pattern + "\" ; gave up at index " + matcher.regionStart());
                    }

                    if (fastdateparser_strategy.addRegex(this, stringbuilder)) {
                        arraylist.add(fastdateparser_strategy);
                    }

                    this.currentFormatField = null;
                    this.strategies = (FastDateParser.Strategy[]) arraylist.toArray(new FastDateParser.Strategy[arraylist.size()]);
                    this.parsePattern = Pattern.compile(stringbuilder.toString());
                    return;
                }

                String s = matcher.group();

                this.nextStrategy = this.getStrategy(s, calendar);
                if (fastdateparser_strategy.addRegex(this, stringbuilder)) {
                    arraylist.add(fastdateparser_strategy);
                }

                this.currentFormatField = s;
                fastdateparser_strategy = this.nextStrategy;
            }
        }
    }

    public String getPattern() {
        return this.pattern;
    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public Locale getLocale() {
        return this.locale;
    }

    Pattern getParsePattern() {
        return this.parsePattern;
    }

    public boolean equals(Object object) {
        if (!(object instanceof FastDateParser)) {
            return false;
        } else {
            FastDateParser fastdateparser = (FastDateParser) object;

            return this.pattern.equals(fastdateparser.pattern) && this.timeZone.equals(fastdateparser.timeZone) && this.locale.equals(fastdateparser.locale);
        }
    }

    public int hashCode() {
        return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
    }

    public String toString() {
        return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
    }

    private void readObject(ObjectInputStream objectinputstream) throws IOException, ClassNotFoundException {
        objectinputstream.defaultReadObject();
        Calendar calendar = Calendar.getInstance(this.timeZone, this.locale);

        this.init(calendar);
    }

    public Object parseObject(String s) throws ParseException {
        return this.parse(s);
    }

    public Date parse(String s) throws ParseException {
        Date date = this.parse(s, new ParsePosition(0));

        if (date == null) {
            if (this.locale.equals(FastDateParser.JAPANESE_IMPERIAL)) {
                throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\n" + "Unparseable date: \"" + s + "\" does not match " + this.parsePattern.pattern(), 0);
            } else {
                throw new ParseException("Unparseable date: \"" + s + "\" does not match " + this.parsePattern.pattern(), 0);
            }
        } else {
            return date;
        }
    }

    public Object parseObject(String s, ParsePosition parseposition) {
        return this.parse(s, parseposition);
    }

    public Date parse(String s, ParsePosition parseposition) {
        int i = parseposition.getIndex();
        Matcher matcher = this.parsePattern.matcher(s.substring(i));

        if (!matcher.lookingAt()) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance(this.timeZone, this.locale);

            calendar.clear();
            int j = 0;

            while (j < this.strategies.length) {
                FastDateParser.Strategy fastdateparser_strategy = this.strategies[j++];

                fastdateparser_strategy.setCalendar(this, calendar, matcher.group(j));
            }

            parseposition.setIndex(i + matcher.end());
            return calendar.getTime();
        }
    }

    private static StringBuilder escapeRegex(StringBuilder stringbuilder, String s, boolean flag) {
        stringbuilder.append("\\Q");

        for (int i = 0; i < s.length(); ++i) {
            char c0 = s.charAt(i);

            switch (c0) {
            case '\'':
                if (flag) {
                    ++i;
                    if (i == s.length()) {
                        return stringbuilder;
                    }

                    c0 = s.charAt(i);
                }
                break;

            case '\\':
                ++i;
                if (i != s.length()) {
                    stringbuilder.append(c0);
                    c0 = s.charAt(i);
                    if (c0 == 69) {
                        stringbuilder.append("E\\\\E\\");
                        c0 = 81;
                    }
                }
            }

            stringbuilder.append(c0);
        }

        stringbuilder.append("\\E");
        return stringbuilder;
    }

    private static Map getDisplayNames(int i, Calendar calendar, Locale locale) {
        return calendar.getDisplayNames(i, 0, locale);
    }

    private int adjustYear(int i) {
        int j = this.century + i;

        return i >= this.startYear ? j : j + 100;
    }

    boolean isNextNumber() {
        return this.nextStrategy != null && this.nextStrategy.isNumber();
    }

    int getFieldWidth() {
        return this.currentFormatField.length();
    }

    private FastDateParser.Strategy getStrategy(String s, Calendar calendar) {
        switch (s.charAt(0)) {
        case '\'':
            if (s.length() > 2) {
                return new FastDateParser.CopyQuotedStrategy(s.substring(1, s.length() - 1));
            }

        case '(':
        case ')':
        case '*':
        case '+':
        case ',':
        case '-':
        case '.':
        case '/':
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
        case ':':
        case ';':
        case '<':
        case '=':
        case '>':
        case '?':
        case '@':
        case 'A':
        case 'B':
        case 'C':
        case 'I':
        case 'J':
        case 'L':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'T':
        case 'U':
        case 'V':
        case 'X':
        case 'Y':
        case '[':
        case '\\':
        case ']':
        case '^':
        case '_':
        case '`':
        case 'b':
        case 'c':
        case 'e':
        case 'f':
        case 'g':
        case 'i':
        case 'j':
        case 'l':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 't':
        case 'u':
        case 'v':
        case 'x':
        default:
            return new FastDateParser.CopyQuotedStrategy(s);

        case 'D':
            return FastDateParser.DAY_OF_YEAR_STRATEGY;

        case 'E':
            return this.getLocaleSpecificStrategy(7, calendar);

        case 'F':
            return FastDateParser.DAY_OF_WEEK_IN_MONTH_STRATEGY;

        case 'G':
            return this.getLocaleSpecificStrategy(0, calendar);

        case 'H':
            return FastDateParser.MODULO_HOUR_OF_DAY_STRATEGY;

        case 'K':
            return FastDateParser.HOUR_STRATEGY;

        case 'M':
            return s.length() >= 3 ? this.getLocaleSpecificStrategy(2, calendar) : FastDateParser.NUMBER_MONTH_STRATEGY;

        case 'S':
            return FastDateParser.MILLISECOND_STRATEGY;

        case 'W':
            return FastDateParser.WEEK_OF_MONTH_STRATEGY;

        case 'Z':
        case 'z':
            return this.getLocaleSpecificStrategy(15, calendar);

        case 'a':
            return this.getLocaleSpecificStrategy(9, calendar);

        case 'd':
            return FastDateParser.DAY_OF_MONTH_STRATEGY;

        case 'h':
            return FastDateParser.MODULO_HOUR_STRATEGY;

        case 'k':
            return FastDateParser.HOUR_OF_DAY_STRATEGY;

        case 'm':
            return FastDateParser.MINUTE_STRATEGY;

        case 's':
            return FastDateParser.SECOND_STRATEGY;

        case 'w':
            return FastDateParser.WEEK_OF_YEAR_STRATEGY;

        case 'y':
            return s.length() > 2 ? FastDateParser.LITERAL_YEAR_STRATEGY : FastDateParser.ABBREVIATED_YEAR_STRATEGY;
        }
    }

    private static ConcurrentMap getCache(int i) {
        ConcurrentMap[] aconcurrentmap = FastDateParser.caches;

        synchronized (FastDateParser.caches) {
            if (FastDateParser.caches[i] == null) {
                FastDateParser.caches[i] = new ConcurrentHashMap(3);
            }

            return FastDateParser.caches[i];
        }
    }

    private FastDateParser.Strategy getLocaleSpecificStrategy(int i, Calendar calendar) {
        ConcurrentMap concurrentmap = getCache(i);
        Object object = (FastDateParser.Strategy) concurrentmap.get(this.locale);

        if (object == null) {
            object = i == 15 ? new FastDateParser.TimeZoneStrategy(this.locale) : new FastDateParser.TextStrategy(i, calendar, this.locale);
            FastDateParser.Strategy fastdateparser_strategy = (FastDateParser.Strategy) concurrentmap.putIfAbsent(this.locale, object);

            if (fastdateparser_strategy != null) {
                return fastdateparser_strategy;
            }
        }

        return (FastDateParser.Strategy) object;
    }

    private static class TimeZoneStrategy extends FastDateParser.Strategy {

        private final String validTimeZoneChars;
        private final SortedMap tzNames;
        private static final int ID = 0;
        private static final int LONG_STD = 1;
        private static final int SHORT_STD = 2;
        private static final int LONG_DST = 3;
        private static final int SHORT_DST = 4;

        TimeZoneStrategy(Locale locale) {
            super(null);
            this.tzNames = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            String[][] astring = DateFormatSymbols.getInstance(locale).getZoneStrings();
            String[][] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String[] astring2 = astring1[j];

                if (!astring2[0].startsWith("GMT")) {
                    TimeZone timezone = TimeZone.getTimeZone(astring2[0]);

                    if (!this.tzNames.containsKey(astring2[1])) {
                        this.tzNames.put(astring2[1], timezone);
                    }

                    if (!this.tzNames.containsKey(astring2[2])) {
                        this.tzNames.put(astring2[2], timezone);
                    }

                    if (timezone.useDaylightTime()) {
                        if (!this.tzNames.containsKey(astring2[3])) {
                            this.tzNames.put(astring2[3], timezone);
                        }

                        if (!this.tzNames.containsKey(astring2[4])) {
                            this.tzNames.put(astring2[4], timezone);
                        }
                    }
                }
            }

            StringBuilder stringbuilder = new StringBuilder();

            stringbuilder.append("(GMT[+\\-]\\d{0,1}\\d{2}|[+\\-]\\d{2}:?\\d{2}|");
            Iterator iterator = this.tzNames.keySet().iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                FastDateParser.escapeRegex(stringbuilder, s, false).append('|');
            }

            stringbuilder.setCharAt(stringbuilder.length() - 1, ')');
            this.validTimeZoneChars = stringbuilder.toString();
        }

        boolean addRegex(FastDateParser fastdateparser, StringBuilder stringbuilder) {
            stringbuilder.append(this.validTimeZoneChars);
            return true;
        }

        void setCalendar(FastDateParser fastdateparser, Calendar calendar, String s) {
            TimeZone timezone;

            if (s.charAt(0) != 43 && s.charAt(0) != 45) {
                if (s.startsWith("GMT")) {
                    timezone = TimeZone.getTimeZone(s);
                } else {
                    timezone = (TimeZone) this.tzNames.get(s);
                    if (timezone == null) {
                        throw new IllegalArgumentException(s + " is not a supported timezone name");
                    }
                }
            } else {
                timezone = TimeZone.getTimeZone("GMT" + s);
            }

            calendar.setTimeZone(timezone);
        }
    }

    private static class NumberStrategy extends FastDateParser.Strategy {

        private final int field;

        NumberStrategy(int i) {
            super(null);
            this.field = i;
        }

        boolean isNumber() {
            return true;
        }

        boolean addRegex(FastDateParser fastdateparser, StringBuilder stringbuilder) {
            if (fastdateparser.isNextNumber()) {
                stringbuilder.append("(\\p{Nd}{").append(fastdateparser.getFieldWidth()).append("}+)");
            } else {
                stringbuilder.append("(\\p{Nd}++)");
            }

            return true;
        }

        void setCalendar(FastDateParser fastdateparser, Calendar calendar, String s) {
            calendar.set(this.field, this.modify(Integer.parseInt(s)));
        }

        int modify(int i) {
            return i;
        }
    }

    private static class TextStrategy extends FastDateParser.Strategy {

        private final int field;
        private final Map keyValues;

        TextStrategy(int i, Calendar calendar, Locale locale) {
            super(null);
            this.field = i;
            this.keyValues = FastDateParser.getDisplayNames(i, calendar, locale);
        }

        boolean addRegex(FastDateParser fastdateparser, StringBuilder stringbuilder) {
            stringbuilder.append('(');
            Iterator iterator = this.keyValues.keySet().iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                FastDateParser.escapeRegex(stringbuilder, s, false).append('|');
            }

            stringbuilder.setCharAt(stringbuilder.length() - 1, ')');
            return true;
        }

        void setCalendar(FastDateParser fastdateparser, Calendar calendar, String s) {
            Integer integer = (Integer) this.keyValues.get(s);

            if (integer != null) {
                calendar.set(this.field, integer.intValue());
            } else {
                StringBuilder stringbuilder = new StringBuilder(s);

                stringbuilder.append(" not in (");
                Iterator iterator = this.keyValues.keySet().iterator();

                while (iterator.hasNext()) {
                    String s1 = (String) iterator.next();

                    stringbuilder.append(s1).append(' ');
                }

                stringbuilder.setCharAt(stringbuilder.length() - 1, ')');
                throw new IllegalArgumentException(stringbuilder.toString());
            }
        }
    }

    private static class CopyQuotedStrategy extends FastDateParser.Strategy {

        private final String formatField;

        CopyQuotedStrategy(String s) {
            super(null);
            this.formatField = s;
        }

        boolean isNumber() {
            char c0 = this.formatField.charAt(0);

            if (c0 == 39) {
                c0 = this.formatField.charAt(1);
            }

            return Character.isDigit(c0);
        }

        boolean addRegex(FastDateParser fastdateparser, StringBuilder stringbuilder) {
            FastDateParser.escapeRegex(stringbuilder, this.formatField, true);
            return false;
        }
    }

    private abstract static class Strategy {

        private Strategy() {}

        boolean isNumber() {
            return false;
        }

        void setCalendar(FastDateParser fastdateparser, Calendar calendar, String s) {}

        abstract boolean addRegex(FastDateParser fastdateparser, StringBuilder stringbuilder);

        Strategy(Object object) {
            this();
        }
    }
}
