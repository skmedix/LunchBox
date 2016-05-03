package org.apache.logging.log4j.core.filter;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;

@Plugin(
    name = "BurstFilter",
    category = "Core",
    elementType = "filter",
    printObject = true
)
public final class BurstFilter extends AbstractFilter {

    private static final long NANOS_IN_SECONDS = 1000000000L;
    private static final int DEFAULT_RATE = 10;
    private static final int DEFAULT_RATE_MULTIPLE = 100;
    private static final int HASH_SHIFT = 32;
    private final Level level;
    private final long burstInterval;
    private final DelayQueue history = new DelayQueue();
    private final Queue available = new ConcurrentLinkedQueue();

    private BurstFilter(Level level, float f, long i, Filter.Result filter_result, Filter.Result filter_result1) {
        super(filter_result, filter_result1);
        this.level = level;
        this.burstInterval = (long) (1.0E9F * ((float) i / f));

        for (int j = 0; (long) j < i; ++j) {
            this.available.add(new BurstFilter.LogDelay());
        }

    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String s, Object... aobject) {
        return this.filter(level);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Object object, Throwable throwable) {
        return this.filter(level);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return this.filter(level);
    }

    public Filter.Result filter(LogEvent logevent) {
        return this.filter(logevent.getLevel());
    }

    private Filter.Result filter(Level level) {
        if (!this.level.isAtLeastAsSpecificAs(level)) {
            return this.onMatch;
        } else {
            BurstFilter.LogDelay burstfilter_logdelay;

            for (burstfilter_logdelay = (BurstFilter.LogDelay) this.history.poll(); burstfilter_logdelay != null; burstfilter_logdelay = (BurstFilter.LogDelay) this.history.poll()) {
                this.available.add(burstfilter_logdelay);
            }

            burstfilter_logdelay = (BurstFilter.LogDelay) this.available.poll();
            if (burstfilter_logdelay != null) {
                burstfilter_logdelay.setDelay(this.burstInterval);
                this.history.add(burstfilter_logdelay);
                return this.onMatch;
            } else {
                return this.onMismatch;
            }
        }
    }

    public int getAvailable() {
        return this.available.size();
    }

    public void clear() {
        Iterator iterator = this.history.iterator();

        while (iterator.hasNext()) {
            BurstFilter.LogDelay burstfilter_logdelay = (BurstFilter.LogDelay) iterator.next();

            this.history.remove(burstfilter_logdelay);
            this.available.add(burstfilter_logdelay);
        }

    }

    public String toString() {
        return "level=" + this.level.toString() + ", interval=" + this.burstInterval + ", max=" + this.history.size();
    }

    @PluginFactory
    public static BurstFilter createFilter(@PluginAttribute("level") String s, @PluginAttribute("rate") String s1, @PluginAttribute("maxBurst") String s2, @PluginAttribute("onMatch") String s3, @PluginAttribute("onMismatch") String s4) {
        Filter.Result filter_result = Filter.Result.toResult(s3, Filter.Result.NEUTRAL);
        Filter.Result filter_result1 = Filter.Result.toResult(s4, Filter.Result.DENY);
        Level level = Level.toLevel(s, Level.WARN);
        float f = s1 == null ? 10.0F : Float.parseFloat(s1);

        if (f <= 0.0F) {
            f = 10.0F;
        }

        long i = s2 == null ? (long) (f * 100.0F) : Long.parseLong(s2);

        return new BurstFilter(level, f, i, filter_result, filter_result1);
    }

    private class LogDelay implements Delayed {

        private long expireTime;

        public void setDelay(long i) {
            this.expireTime = i + System.nanoTime();
        }

        public long getDelay(TimeUnit timeunit) {
            return timeunit.convert(this.expireTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        public int compareTo(Delayed delayed) {
            return this.expireTime < ((BurstFilter.LogDelay) delayed).expireTime ? -1 : (this.expireTime > ((BurstFilter.LogDelay) delayed).expireTime ? 1 : 0);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (object != null && this.getClass() == object.getClass()) {
                BurstFilter.LogDelay burstfilter_logdelay = (BurstFilter.LogDelay) object;

                return this.expireTime == burstfilter_logdelay.expireTime;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return (int) (this.expireTime ^ this.expireTime >>> 32);
        }
    }
}
