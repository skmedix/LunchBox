package org.apache.logging.log4j.core.appender.rolling;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.pattern.ArrayPatternConverter;
import org.apache.logging.log4j.core.pattern.DatePatternConverter;
import org.apache.logging.log4j.core.pattern.FormattingInfo;
import org.apache.logging.log4j.core.pattern.PatternParser;

public class PatternProcessor {

    private static final String KEY = "FileConverter";
    private static final char YEAR_CHAR = 'y';
    private static final char MONTH_CHAR = 'M';
    private static final char[] WEEK_CHARS = new char[] { 'w', 'W'};
    private static final char[] DAY_CHARS = new char[] { 'D', 'd', 'F', 'E'};
    private static final char[] HOUR_CHARS = new char[] { 'H', 'K', 'h', 'k'};
    private static final char MINUTE_CHAR = 'm';
    private static final char SECOND_CHAR = 's';
    private static final char MILLIS_CHAR = 'S';
    private final ArrayPatternConverter[] patternConverters;
    private final FormattingInfo[] patternFields;
    private long prevFileTime = 0L;
    private long nextFileTime = 0L;
    private RolloverFrequency frequency = null;

    public PatternProcessor(String s) {
        PatternParser patternparser = this.createPatternParser();
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();

        patternparser.parse(s, arraylist, arraylist1);
        FormattingInfo[] aformattinginfo = new FormattingInfo[arraylist1.size()];

        this.patternFields = (FormattingInfo[]) arraylist1.toArray(aformattinginfo);
        ArrayPatternConverter[] aarraypatternconverter = new ArrayPatternConverter[arraylist.size()];

        this.patternConverters = (ArrayPatternConverter[]) arraylist.toArray(aarraypatternconverter);
        ArrayPatternConverter[] aarraypatternconverter1 = this.patternConverters;
        int i = aarraypatternconverter1.length;

        for (int j = 0; j < i; ++j) {
            ArrayPatternConverter arraypatternconverter = aarraypatternconverter1[j];

            if (arraypatternconverter instanceof DatePatternConverter) {
                DatePatternConverter datepatternconverter = (DatePatternConverter) arraypatternconverter;

                this.frequency = this.calculateFrequency(datepatternconverter.getPattern());
            }
        }

    }

    public long getNextTime(long i, int j, boolean flag) {
        this.prevFileTime = this.nextFileTime;
        if (this.frequency == null) {
            throw new IllegalStateException("Pattern does not contain a date");
        } else {
            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(i);
            Calendar calendar1 = Calendar.getInstance();

            calendar1.set(calendar.get(1), 0, 1, 0, 0, 0);
            calendar1.set(14, 0);
            long k;

            if (this.frequency == RolloverFrequency.ANNUALLY) {
                this.increment(calendar1, 1, j, flag);
                k = calendar1.getTimeInMillis();
                calendar1.add(1, -1);
                this.nextFileTime = calendar1.getTimeInMillis();
                return k;
            } else if (this.frequency == RolloverFrequency.MONTHLY) {
                this.increment(calendar1, 2, j, flag);
                k = calendar1.getTimeInMillis();
                calendar1.add(2, -1);
                this.nextFileTime = calendar1.getTimeInMillis();
                return k;
            } else if (this.frequency == RolloverFrequency.WEEKLY) {
                this.increment(calendar1, 3, j, flag);
                k = calendar1.getTimeInMillis();
                calendar1.add(3, -1);
                this.nextFileTime = calendar1.getTimeInMillis();
                return k;
            } else {
                calendar1.set(6, calendar.get(6));
                if (this.frequency == RolloverFrequency.DAILY) {
                    this.increment(calendar1, 6, j, flag);
                    k = calendar1.getTimeInMillis();
                    calendar1.add(6, -1);
                    this.nextFileTime = calendar1.getTimeInMillis();
                    return k;
                } else {
                    calendar1.set(10, calendar.get(10));
                    if (this.frequency == RolloverFrequency.HOURLY) {
                        this.increment(calendar1, 10, j, flag);
                        k = calendar1.getTimeInMillis();
                        calendar1.add(10, -1);
                        this.nextFileTime = calendar1.getTimeInMillis();
                        return k;
                    } else {
                        calendar1.set(12, calendar.get(12));
                        if (this.frequency == RolloverFrequency.EVERY_MINUTE) {
                            this.increment(calendar1, 12, j, flag);
                            k = calendar1.getTimeInMillis();
                            calendar1.add(12, -1);
                            this.nextFileTime = calendar1.getTimeInMillis();
                            return k;
                        } else {
                            calendar1.set(13, calendar.get(13));
                            if (this.frequency == RolloverFrequency.EVERY_SECOND) {
                                this.increment(calendar1, 13, j, flag);
                                k = calendar1.getTimeInMillis();
                                calendar1.add(13, -1);
                                this.nextFileTime = calendar1.getTimeInMillis();
                                return k;
                            } else {
                                this.increment(calendar1, 14, j, flag);
                                k = calendar1.getTimeInMillis();
                                calendar1.add(14, -1);
                                this.nextFileTime = calendar1.getTimeInMillis();
                                return k;
                            }
                        }
                    }
                }
            }
        }
    }

    private void increment(Calendar calendar, int i, int j, boolean flag) {
        int k = flag ? j - calendar.get(i) % j : j;

        calendar.add(i, k);
    }

    public final void formatFileName(StringBuilder stringbuilder, Object object) {
        long i = this.prevFileTime == 0L ? System.currentTimeMillis() : this.prevFileTime;

        this.formatFileName(stringbuilder, new Object[] { new Date(i), object});
    }

    public final void formatFileName(StrSubstitutor strsubstitutor, StringBuilder stringbuilder, Object object) {
        long i = this.prevFileTime == 0L ? System.currentTimeMillis() : this.prevFileTime;

        this.formatFileName(stringbuilder, new Object[] { new Date(i), object});
        Log4jLogEvent log4jlogevent = new Log4jLogEvent(i);
        String s = strsubstitutor.replace((LogEvent) log4jlogevent, stringbuilder);

        stringbuilder.setLength(0);
        stringbuilder.append(s);
    }

    protected final void formatFileName(StringBuilder stringbuilder, Object... aobject) {
        for (int i = 0; i < this.patternConverters.length; ++i) {
            int j = stringbuilder.length();

            this.patternConverters[i].format(stringbuilder, aobject);
            if (this.patternFields[i] != null) {
                this.patternFields[i].format(j, stringbuilder);
            }
        }

    }

    private RolloverFrequency calculateFrequency(String s) {
        return this.patternContains(s, 'S') ? RolloverFrequency.EVERY_MILLISECOND : (this.patternContains(s, 's') ? RolloverFrequency.EVERY_SECOND : (this.patternContains(s, 'm') ? RolloverFrequency.EVERY_MINUTE : (this.patternContains(s, PatternProcessor.HOUR_CHARS) ? RolloverFrequency.HOURLY : (this.patternContains(s, PatternProcessor.DAY_CHARS) ? RolloverFrequency.DAILY : (this.patternContains(s, PatternProcessor.WEEK_CHARS) ? RolloverFrequency.WEEKLY : (this.patternContains(s, 'M') ? RolloverFrequency.MONTHLY : (this.patternContains(s, 'y') ? RolloverFrequency.ANNUALLY : null)))))));
    }

    private PatternParser createPatternParser() {
        return new PatternParser((Configuration) null, "FileConverter", (Class) null);
    }

    private boolean patternContains(String s, char... achar) {
        char[] achar1 = achar;
        int i = achar.length;

        for (int j = 0; j < i; ++j) {
            char c0 = achar1[j];

            if (this.patternContains(s, c0)) {
                return true;
            }
        }

        return false;
    }

    private boolean patternContains(String s, char c0) {
        return s.indexOf(c0) >= 0;
    }
}
