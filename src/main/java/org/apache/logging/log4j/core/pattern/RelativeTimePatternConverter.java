package org.apache.logging.log4j.core.pattern;

import java.lang.management.ManagementFactory;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "RelativeTimePatternConverter",
    category = "Converter"
)
@ConverterKeys({ "r", "relative"})
public class RelativeTimePatternConverter extends LogEventPatternConverter {

    private long lastTimestamp = Long.MIN_VALUE;
    private final long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
    private String relative;

    public RelativeTimePatternConverter() {
        super("Time", "time");
    }

    public static RelativeTimePatternConverter newInstance(String[] astring) {
        return new RelativeTimePatternConverter();
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        long i = logevent.getMillis();

        synchronized (this) {
            if (i != this.lastTimestamp) {
                this.lastTimestamp = i;
                this.relative = Long.toString(i - this.startTime);
            }
        }

        stringbuilder.append(this.relative);
    }
}
