package org.apache.logging.log4j.core.filter;

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
    name = "ThresholdFilter",
    category = "Core",
    elementType = "filter",
    printObject = true
)
public final class ThresholdFilter extends AbstractFilter {

    private final Level level;

    private ThresholdFilter(Level level, Filter.Result filter_result, Filter.Result filter_result1) {
        super(filter_result, filter_result1);
        this.level = level;
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
        return level.isAtLeastAsSpecificAs(this.level) ? this.onMatch : this.onMismatch;
    }

    public String toString() {
        return this.level.toString();
    }

    @PluginFactory
    public static ThresholdFilter createFilter(@PluginAttribute("level") String s, @PluginAttribute("onMatch") String s1, @PluginAttribute("onMismatch") String s2) {
        Level level = Level.toLevel(s, Level.ERROR);
        Filter.Result filter_result = Filter.Result.toResult(s1, Filter.Result.NEUTRAL);
        Filter.Result filter_result1 = Filter.Result.toResult(s2, Filter.Result.DENY);

        return new ThresholdFilter(level, filter_result, filter_result1);
    }
}
