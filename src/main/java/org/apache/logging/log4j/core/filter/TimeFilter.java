package org.apache.logging.log4j.core.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(
    name = "TimeFilter",
    category = "Core",
    elementType = "filter",
    printObject = true
)
public final class TimeFilter extends AbstractFilter {

    private static final long HOUR_MS = 3600000L;
    private static final long MINUTE_MS = 60000L;
    private static final long SECOND_MS = 1000L;
    private final long start;
    private final long end;
    private final TimeZone timezone;

    private TimeFilter(long i, long j, TimeZone timezone, Filter.Result filter_result, Filter.Result filter_result1) {
        super(filter_result, filter_result1);
        this.start = i;
        this.end = j;
        this.timezone = timezone;
    }

    public Filter.Result filter(LogEvent logevent) {
        Calendar calendar = Calendar.getInstance(this.timezone);

        calendar.setTimeInMillis(logevent.getMillis());
        long i = (long) calendar.get(11) * 3600000L + (long) calendar.get(12) * 60000L + (long) calendar.get(13) * 1000L + (long) calendar.get(14);

        return i >= this.start && i < this.end ? this.onMatch : this.onMismatch;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("start=").append(this.start);
        stringbuilder.append(", end=").append(this.end);
        stringbuilder.append(", timezone=").append(this.timezone.toString());
        return stringbuilder.toString();
    }

    @PluginFactory
    public static TimeFilter createFilter(@PluginAttribute("start") String s, @PluginAttribute("end") String s1, @PluginAttribute("timezone") String s2, @PluginAttribute("onMatch") String s3, @PluginAttribute("onMismatch") String s4) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("HH:mm:ss");
        long i = 0L;

        if (s != null) {
            simpledateformat.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                i = simpledateformat.parse(s).getTime();
            } catch (ParseException parseexception) {
                TimeFilter.LOGGER.warn("Error parsing start value " + s, (Throwable) parseexception);
            }
        }

        long j = Long.MAX_VALUE;

        if (s1 != null) {
            simpledateformat.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                j = simpledateformat.parse(s1).getTime();
            } catch (ParseException parseexception1) {
                TimeFilter.LOGGER.warn("Error parsing start value " + s1, (Throwable) parseexception1);
            }
        }

        TimeZone timezone = s2 == null ? TimeZone.getDefault() : TimeZone.getTimeZone(s2);
        Filter.Result filter_result = Filter.Result.toResult(s3, Filter.Result.NEUTRAL);
        Filter.Result filter_result1 = Filter.Result.toResult(s4, Filter.Result.DENY);

        return new TimeFilter(i, j, timezone, filter_result, filter_result1);
    }
}
