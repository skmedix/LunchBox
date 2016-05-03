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
    name = "MarkerFilter",
    category = "Core",
    elementType = "filter",
    printObject = true
)
public final class MarkerFilter extends AbstractFilter {

    private final String name;

    private MarkerFilter(String s, Filter.Result filter_result, Filter.Result filter_result1) {
        super(filter_result, filter_result1);
        this.name = s;
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String s, Object... aobject) {
        return this.filter(marker);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Object object, Throwable throwable) {
        return this.filter(marker);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return this.filter(marker);
    }

    public Filter.Result filter(LogEvent logevent) {
        return this.filter(logevent.getMarker());
    }

    private Filter.Result filter(Marker marker) {
        return marker != null && marker.isInstanceOf(this.name) ? this.onMatch : this.onMismatch;
    }

    public String toString() {
        return this.name;
    }

    @PluginFactory
    public static MarkerFilter createFilter(@PluginAttribute("marker") String s, @PluginAttribute("onMatch") String s1, @PluginAttribute("onMismatch") String s2) {
        if (s == null) {
            MarkerFilter.LOGGER.error("A marker must be provided for MarkerFilter");
            return null;
        } else {
            Filter.Result filter_result = Filter.Result.toResult(s1);
            Filter.Result filter_result1 = Filter.Result.toResult(s2);

            return new MarkerFilter(s, filter_result, filter_result1);
        }
    }
}
