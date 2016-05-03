package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Integers;

@Plugin(
    name = "TimeBasedTriggeringPolicy",
    category = "Core",
    printObject = true
)
public final class TimeBasedTriggeringPolicy implements TriggeringPolicy {

    private long nextRollover;
    private final int interval;
    private final boolean modulate;
    private RollingFileManager manager;

    private TimeBasedTriggeringPolicy(int i, boolean flag) {
        this.interval = i;
        this.modulate = flag;
    }

    public void initialize(RollingFileManager rollingfilemanager) {
        this.manager = rollingfilemanager;
        this.nextRollover = rollingfilemanager.getPatternProcessor().getNextTime(rollingfilemanager.getFileTime(), this.interval, this.modulate);
    }

    public boolean isTriggeringEvent(LogEvent logevent) {
        if (this.manager.getFileSize() == 0L) {
            return false;
        } else {
            long i = System.currentTimeMillis();

            if (i > this.nextRollover) {
                this.nextRollover = this.manager.getPatternProcessor().getNextTime(i, this.interval, this.modulate);
                return true;
            } else {
                return false;
            }
        }
    }

    public String toString() {
        return "TimeBasedTriggeringPolicy";
    }

    @PluginFactory
    public static TimeBasedTriggeringPolicy createPolicy(@PluginAttribute("interval") String s, @PluginAttribute("modulate") String s1) {
        int i = Integers.parseInt(s, 1);
        boolean flag = Boolean.parseBoolean(s1);

        return new TimeBasedTriggeringPolicy(i, flag);
    }
}
