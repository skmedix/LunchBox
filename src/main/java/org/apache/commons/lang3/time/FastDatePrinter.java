package org.apache.commons.lang3.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.Validate;

public class FastDatePrinter implements DatePrinter, Serializable {

    private static final long serialVersionUID = 1L;
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private final String mPattern;
    private final TimeZone mTimeZone;
    private final Locale mLocale;
    private transient FastDatePrinter.Rule[] mRules;
    private transient int mMaxLengthEstimate;
    private static final ConcurrentMap cTimeZoneDisplayCache = new ConcurrentHashMap(7);

    protected FastDatePrinter(String s, TimeZone timezone, Locale locale) {
        this.mPattern = s;
        this.mTimeZone = timezone;
        this.mLocale = locale;
        this.init();
    }

    private void init() {
        List list = this.parsePattern();

        this.mRules = (FastDatePrinter.Rule[]) list.toArray(new FastDatePrinter.Rule[list.size()]);
        int i = 0;
        int j = this.mRules.length;

        while (true) {
            --j;
            if (j < 0) {
                this.mMaxLengthEstimate = i;
                return;
            }

            i += this.mRules[j].estimateLength();
        }
    }

    protected List parsePattern() {
        DateFormatSymbols dateformatsymbols = new DateFormatSymbols(this.mLocale);
        ArrayList arraylist = new ArrayList();
        String[] astring = dateformatsymbols.getEras();
        String[] astring1 = dateformatsymbols.getMonths();
        String[] astring2 = dateformatsymbols.getShortMonths();
        String[] astring3 = dateformatsymbols.getWeekdays();
        String[] astring4 = dateformatsymbols.getShortWeekdays();
        String[] astring5 = dateformatsymbols.getAmPmStrings();
        int i = this.mPattern.length();
        int[] aint = new int[1];

        for (int j = 0; j < i; ++j) {
            aint[0] = j;
            String s = this.parseToken(this.mPattern, aint);

            j = aint[0];
            int k = s.length();

            if (k == 0) {
                break;
            }

            char c0 = s.charAt(0);
            Object object;

            switch (c0) {
            case '\'':
                String s1 = s.substring(1);

                if (s1.length() == 1) {
                    object = new FastDatePrinter.CharacterLiteral(s1.charAt(0));
                } else {
                    object = new FastDatePrinter.StringLiteral(s1);
                }
                break;

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
                throw new IllegalArgumentException("Illegal pattern component: " + s);

            case 'D':
                object = this.selectNumberRule(6, k);
                break;

            case 'E':
                object = new FastDatePrinter.TextField(7, k < 4 ? astring4 : astring3);
                break;

            case 'F':
                object = this.selectNumberRule(8, k);
                break;

            case 'G':
                object = new FastDatePrinter.TextField(0, astring);
                break;

            case 'H':
                object = this.selectNumberRule(11, k);
                break;

            case 'K':
                object = this.selectNumberRule(10, k);
                break;

            case 'M':
                if (k >= 4) {
                    object = new FastDatePrinter.TextField(2, astring1);
                } else if (k == 3) {
                    object = new FastDatePrinter.TextField(2, astring2);
                } else if (k == 2) {
                    object = FastDatePrinter.TwoDigitMonthField.INSTANCE;
                } else {
                    object = FastDatePrinter.UnpaddedMonthField.INSTANCE;
                }
                break;

            case 'S':
                object = this.selectNumberRule(14, k);
                break;

            case 'W':
                object = this.selectNumberRule(4, k);
                break;

            case 'Z':
                if (k == 1) {
                    object = FastDatePrinter.TimeZoneNumberRule.INSTANCE_NO_COLON;
                } else {
                    object = FastDatePrinter.TimeZoneNumberRule.INSTANCE_COLON;
                }
                break;

            case 'a':
                object = new FastDatePrinter.TextField(9, astring5);
                break;

            case 'd':
                object = this.selectNumberRule(5, k);
                break;

            case 'h':
                object = new FastDatePrinter.TwelveHourField(this.selectNumberRule(10, k));
                break;

            case 'k':
                object = new FastDatePrinter.TwentyFourHourField(this.selectNumberRule(11, k));
                break;

            case 'm':
                object = this.selectNumberRule(12, k);
                break;

            case 's':
                object = this.selectNumberRule(13, k);
                break;

            case 'w':
                object = this.selectNumberRule(3, k);
                break;

            case 'y':
                if (k == 2) {
                    object = FastDatePrinter.TwoDigitYearField.INSTANCE;
                } else {
                    object = this.selectNumberRule(1, k < 4 ? 4 : k);
                }
                break;

            case 'z':
                if (k >= 4) {
                    object = new FastDatePrinter.TimeZoneNameRule(this.mTimeZone, this.mLocale, 1);
                } else {
                    object = new FastDatePrinter.TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
                }
            }

            arraylist.add(object);
        }

        return arraylist;
    }

    protected String parseToken(String s, int[] aint) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = aint[0];
        int j = s.length();
        char c0 = s.charAt(i);

        if ((c0 < 65 || c0 > 90) && (c0 < 97 || c0 > 122)) {
            stringbuilder.append('\'');

            for (boolean flag = false; i < j; ++i) {
                c0 = s.charAt(i);
                if (c0 == 39) {
                    if (i + 1 < j && s.charAt(i + 1) == 39) {
                        ++i;
                        stringbuilder.append(c0);
                    } else {
                        flag = !flag;
                    }
                } else {
                    if (!flag && (c0 >= 65 && c0 <= 90 || c0 >= 97 && c0 <= 122)) {
                        --i;
                        break;
                    }

                    stringbuilder.append(c0);
                }
            }
        } else {
            stringbuilder.append(c0);

            while (i + 1 < j) {
                char c1 = s.charAt(i + 1);

                if (c1 != c0) {
                    break;
                }

                stringbuilder.append(c0);
                ++i;
            }
        }

        aint[0] = i;
        return stringbuilder.toString();
    }

    protected FastDatePrinter.NumberRule selectNumberRule(int i, int j) {
        switch (j) {
        case 1:
            return new FastDatePrinter.UnpaddedNumberField(i);

        case 2:
            return new FastDatePrinter.TwoDigitNumberField(i);

        default:
            return new FastDatePrinter.PaddedNumberField(i, j);
        }
    }

    public StringBuffer format(Object object, StringBuffer stringbuffer, FieldPosition fieldposition) {
        if (object instanceof Date) {
            return this.format((Date) object, stringbuffer);
        } else if (object instanceof Calendar) {
            return this.format((Calendar) object, stringbuffer);
        } else if (object instanceof Long) {
            return this.format(((Long) object).longValue(), stringbuffer);
        } else {
            throw new IllegalArgumentException("Unknown class: " + (object == null ? "<null>" : object.getClass().getName()));
        }
    }

    public String format(long i) {
        GregorianCalendar gregoriancalendar = this.newCalendar();

        gregoriancalendar.setTimeInMillis(i);
        return this.applyRulesToString(gregoriancalendar);
    }

    private String applyRulesToString(Calendar calendar) {
        return this.applyRules(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    private GregorianCalendar newCalendar() {
        return new GregorianCalendar(this.mTimeZone, this.mLocale);
    }

    public String format(Date date) {
        GregorianCalendar gregoriancalendar = this.newCalendar();

        gregoriancalendar.setTime(date);
        return this.applyRulesToString(gregoriancalendar);
    }

    public String format(Calendar calendar) {
        return this.format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    public StringBuffer format(long i, StringBuffer stringbuffer) {
        return this.format(new Date(i), stringbuffer);
    }

    public StringBuffer format(Date date, StringBuffer stringbuffer) {
        GregorianCalendar gregoriancalendar = this.newCalendar();

        gregoriancalendar.setTime(date);
        return this.applyRules(gregoriancalendar, stringbuffer);
    }

    public StringBuffer format(Calendar calendar, StringBuffer stringbuffer) {
        return this.applyRules(calendar, stringbuffer);
    }

    protected StringBuffer applyRules(Calendar calendar, StringBuffer stringbuffer) {
        FastDatePrinter.Rule[] afastdateprinter_rule = this.mRules;
        int i = afastdateprinter_rule.length;

        for (int j = 0; j < i; ++j) {
            FastDatePrinter.Rule fastdateprinter_rule = afastdateprinter_rule[j];

            fastdateprinter_rule.appendTo(stringbuffer, calendar);
        }

        return stringbuffer;
    }

    public String getPattern() {
        return this.mPattern;
    }

    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public int getMaxLengthEstimate() {
        return this.mMaxLengthEstimate;
    }

    public boolean equals(Object object) {
        if (!(object instanceof FastDatePrinter)) {
            return false;
        } else {
            FastDatePrinter fastdateprinter = (FastDatePrinter) object;

            return this.mPattern.equals(fastdateprinter.mPattern) && this.mTimeZone.equals(fastdateprinter.mTimeZone) && this.mLocale.equals(fastdateprinter.mLocale);
        }
    }

    public int hashCode() {
        return this.mPattern.hashCode() + 13 * (this.mTimeZone.hashCode() + 13 * this.mLocale.hashCode());
    }

    public String toString() {
        return "FastDatePrinter[" + this.mPattern + "," + this.mLocale + "," + this.mTimeZone.getID() + "]";
    }

    private void readObject(ObjectInputStream objectinputstream) throws IOException, ClassNotFoundException {
        objectinputstream.defaultReadObject();
        this.init();
    }

    static String getTimeZoneDisplay(TimeZone timezone, boolean flag, int i, Locale locale) {
        FastDatePrinter.TimeZoneDisplayKey fastdateprinter_timezonedisplaykey = new FastDatePrinter.TimeZoneDisplayKey(timezone, flag, i, locale);
        String s = (String) FastDatePrinter.cTimeZoneDisplayCache.get(fastdateprinter_timezonedisplaykey);

        if (s == null) {
            s = timezone.getDisplayName(flag, i, locale);
            String s1 = (String) FastDatePrinter.cTimeZoneDisplayCache.putIfAbsent(fastdateprinter_timezonedisplaykey, s);

            if (s1 != null) {
                s = s1;
            }
        }

        return s;
    }

    private static class TimeZoneDisplayKey {

        private final TimeZone mTimeZone;
        private final int mStyle;
        private final Locale mLocale;

        TimeZoneDisplayKey(TimeZone timezone, boolean flag, int i, Locale locale) {
            this.mTimeZone = timezone;
            if (flag) {
                this.mStyle = i | Integer.MIN_VALUE;
            } else {
                this.mStyle = i;
            }

            this.mLocale = locale;
        }

        public int hashCode() {
            return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (!(object instanceof FastDatePrinter.TimeZoneDisplayKey)) {
                return false;
            } else {
                FastDatePrinter.TimeZoneDisplayKey fastdateprinter_timezonedisplaykey = (FastDatePrinter.TimeZoneDisplayKey) object;

                return this.mTimeZone.equals(fastdateprinter_timezonedisplaykey.mTimeZone) && this.mStyle == fastdateprinter_timezonedisplaykey.mStyle && this.mLocale.equals(fastdateprinter_timezonedisplaykey.mLocale);
            }
        }
    }

    private static class TimeZoneNumberRule implements FastDatePrinter.Rule {

        static final FastDatePrinter.TimeZoneNumberRule INSTANCE_COLON = new FastDatePrinter.TimeZoneNumberRule(true);
        static final FastDatePrinter.TimeZoneNumberRule INSTANCE_NO_COLON = new FastDatePrinter.TimeZoneNumberRule(false);
        final boolean mColon;

        TimeZoneNumberRule(boolean flag) {
            this.mColon = flag;
        }

        public int estimateLength() {
            return 5;
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            int i = calendar.get(15) + calendar.get(16);

            if (i < 0) {
                stringbuffer.append('-');
                i = -i;
            } else {
                stringbuffer.append('+');
            }

            int j = i / 3600000;

            stringbuffer.append((char) (j / 10 + 48));
            stringbuffer.append((char) (j % 10 + 48));
            if (this.mColon) {
                stringbuffer.append(':');
            }

            int k = i / '\uea60' - 60 * j;

            stringbuffer.append((char) (k / 10 + 48));
            stringbuffer.append((char) (k % 10 + 48));
        }
    }

    private static class TimeZoneNameRule implements FastDatePrinter.Rule {

        private final Locale mLocale;
        private final int mStyle;
        private final String mStandard;
        private final String mDaylight;

        TimeZoneNameRule(TimeZone timezone, Locale locale, int i) {
            this.mLocale = locale;
            this.mStyle = i;
            this.mStandard = FastDatePrinter.getTimeZoneDisplay(timezone, false, i, locale);
            this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timezone, true, i, locale);
        }

        public int estimateLength() {
            return Math.max(this.mStandard.length(), this.mDaylight.length());
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            TimeZone timezone = calendar.getTimeZone();

            if (timezone.useDaylightTime() && calendar.get(16) != 0) {
                stringbuffer.append(FastDatePrinter.getTimeZoneDisplay(timezone, true, this.mStyle, this.mLocale));
            } else {
                stringbuffer.append(FastDatePrinter.getTimeZoneDisplay(timezone, false, this.mStyle, this.mLocale));
            }

        }
    }

    private static class TwentyFourHourField implements FastDatePrinter.NumberRule {

        private final FastDatePrinter.NumberRule mRule;

        TwentyFourHourField(FastDatePrinter.NumberRule fastdateprinter_numberrule) {
            this.mRule = fastdateprinter_numberrule;
        }

        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            int i = calendar.get(11);

            if (i == 0) {
                i = calendar.getMaximum(11) + 1;
            }

            this.mRule.appendTo(stringbuffer, i);
        }

        public void appendTo(StringBuffer stringbuffer, int i) {
            this.mRule.appendTo(stringbuffer, i);
        }
    }

    private static class TwelveHourField implements FastDatePrinter.NumberRule {

        private final FastDatePrinter.NumberRule mRule;

        TwelveHourField(FastDatePrinter.NumberRule fastdateprinter_numberrule) {
            this.mRule = fastdateprinter_numberrule;
        }

        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            int i = calendar.get(10);

            if (i == 0) {
                i = calendar.getLeastMaximum(10) + 1;
            }

            this.mRule.appendTo(stringbuffer, i);
        }

        public void appendTo(StringBuffer stringbuffer, int i) {
            this.mRule.appendTo(stringbuffer, i);
        }
    }

    private static class TwoDigitMonthField implements FastDatePrinter.NumberRule {

        static final FastDatePrinter.TwoDigitMonthField INSTANCE = new FastDatePrinter.TwoDigitMonthField();

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            this.appendTo(stringbuffer, calendar.get(2) + 1);
        }

        public final void appendTo(StringBuffer stringbuffer, int i) {
            stringbuffer.append((char) (i / 10 + 48));
            stringbuffer.append((char) (i % 10 + 48));
        }
    }

    private static class TwoDigitYearField implements FastDatePrinter.NumberRule {

        static final FastDatePrinter.TwoDigitYearField INSTANCE = new FastDatePrinter.TwoDigitYearField();

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            this.appendTo(stringbuffer, calendar.get(1) % 100);
        }

        public final void appendTo(StringBuffer stringbuffer, int i) {
            stringbuffer.append((char) (i / 10 + 48));
            stringbuffer.append((char) (i % 10 + 48));
        }
    }

    private static class TwoDigitNumberField implements FastDatePrinter.NumberRule {

        private final int mField;

        TwoDigitNumberField(int i) {
            this.mField = i;
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            this.appendTo(stringbuffer, calendar.get(this.mField));
        }

        public final void appendTo(StringBuffer stringbuffer, int i) {
            if (i < 100) {
                stringbuffer.append((char) (i / 10 + 48));
                stringbuffer.append((char) (i % 10 + 48));
            } else {
                stringbuffer.append(Integer.toString(i));
            }

        }
    }

    private static class PaddedNumberField implements FastDatePrinter.NumberRule {

        private final int mField;
        private final int mSize;

        PaddedNumberField(int i, int j) {
            if (j < 3) {
                throw new IllegalArgumentException();
            } else {
                this.mField = i;
                this.mSize = j;
            }
        }

        public int estimateLength() {
            return 4;
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            this.appendTo(stringbuffer, calendar.get(this.mField));
        }

        public final void appendTo(StringBuffer stringbuffer, int i) {
            int j;

            if (i < 100) {
                j = this.mSize;

                while (true) {
                    --j;
                    if (j < 2) {
                        stringbuffer.append((char) (i / 10 + 48));
                        stringbuffer.append((char) (i % 10 + 48));
                        break;
                    }

                    stringbuffer.append('0');
                }
            } else {
                if (i < 1000) {
                    j = 3;
                } else {
                    Validate.isTrue(i > -1, "Negative values should not be possible", (long) i);
                    j = Integer.toString(i).length();
                }

                int k = this.mSize;

                while (true) {
                    --k;
                    if (k < j) {
                        stringbuffer.append(Integer.toString(i));
                        break;
                    }

                    stringbuffer.append('0');
                }
            }

        }
    }

    private static class UnpaddedMonthField implements FastDatePrinter.NumberRule {

        static final FastDatePrinter.UnpaddedMonthField INSTANCE = new FastDatePrinter.UnpaddedMonthField();

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            this.appendTo(stringbuffer, calendar.get(2) + 1);
        }

        public final void appendTo(StringBuffer stringbuffer, int i) {
            if (i < 10) {
                stringbuffer.append((char) (i + 48));
            } else {
                stringbuffer.append((char) (i / 10 + 48));
                stringbuffer.append((char) (i % 10 + 48));
            }

        }
    }

    private static class UnpaddedNumberField implements FastDatePrinter.NumberRule {

        private final int mField;

        UnpaddedNumberField(int i) {
            this.mField = i;
        }

        public int estimateLength() {
            return 4;
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            this.appendTo(stringbuffer, calendar.get(this.mField));
        }

        public final void appendTo(StringBuffer stringbuffer, int i) {
            if (i < 10) {
                stringbuffer.append((char) (i + 48));
            } else if (i < 100) {
                stringbuffer.append((char) (i / 10 + 48));
                stringbuffer.append((char) (i % 10 + 48));
            } else {
                stringbuffer.append(Integer.toString(i));
            }

        }
    }

    private static class TextField implements FastDatePrinter.Rule {

        private final int mField;
        private final String[] mValues;

        TextField(int i, String[] astring) {
            this.mField = i;
            this.mValues = astring;
        }

        public int estimateLength() {
            int i = 0;
            int j = this.mValues.length;

            while (true) {
                --j;
                if (j < 0) {
                    return i;
                }

                int k = this.mValues[j].length();

                if (k > i) {
                    i = k;
                }
            }
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            stringbuffer.append(this.mValues[calendar.get(this.mField)]);
        }
    }

    private static class StringLiteral implements FastDatePrinter.Rule {

        private final String mValue;

        StringLiteral(String s) {
            this.mValue = s;
        }

        public int estimateLength() {
            return this.mValue.length();
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            stringbuffer.append(this.mValue);
        }
    }

    private static class CharacterLiteral implements FastDatePrinter.Rule {

        private final char mValue;

        CharacterLiteral(char c0) {
            this.mValue = c0;
        }

        public int estimateLength() {
            return 1;
        }

        public void appendTo(StringBuffer stringbuffer, Calendar calendar) {
            stringbuffer.append(this.mValue);
        }
    }

    private interface NumberRule extends FastDatePrinter.Rule {

        void appendTo(StringBuffer stringbuffer, int i);
    }

    private interface Rule {

        int estimateLength();

        void appendTo(StringBuffer stringbuffer, Calendar calendar);
    }
}
