package org.apache.logging.log4j.core.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    name = "RegexFilter",
    category = "Core",
    elementType = "filter",
    printObject = true
)
public final class RegexFilter extends AbstractFilter {

    private final Pattern pattern;
    private final boolean useRawMessage;

    private RegexFilter(boolean flag, Pattern pattern, Filter.Result filter_result, Filter.Result filter_result1) {
        super(filter_result, filter_result1);
        this.pattern = pattern;
        this.useRawMessage = flag;
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String s, Object... aobject) {
        return this.filter(s);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Object object, Throwable throwable) {
        return object == null ? this.onMismatch : this.filter(object.toString());
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        if (message == null) {
            return this.onMismatch;
        } else {
            String s = this.useRawMessage ? message.getFormat() : message.getFormattedMessage();

            return this.filter(s);
        }
    }

    public Filter.Result filter(LogEvent logevent) {
        String s = this.useRawMessage ? logevent.getMessage().getFormat() : logevent.getMessage().getFormattedMessage();

        return this.filter(s);
    }

    private Filter.Result filter(String s) {
        if (s == null) {
            return this.onMismatch;
        } else {
            Matcher matcher = this.pattern.matcher(s);

            return matcher.matches() ? this.onMatch : this.onMismatch;
        }
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("useRaw=").append(this.useRawMessage);
        stringbuilder.append(", pattern=").append(this.pattern.toString());
        return stringbuilder.toString();
    }

    @PluginFactory
    public static RegexFilter createFilter(@PluginAttribute("regex") String s, @PluginAttribute("useRawMsg") String s1, @PluginAttribute("onMatch") String s2, @PluginAttribute("onMismatch") String s3) {
        if (s == null) {
            RegexFilter.LOGGER.error("A regular expression must be provided for RegexFilter");
            return null;
        } else {
            boolean flag = Boolean.parseBoolean(s1);

            Pattern pattern;

            try {
                pattern = Pattern.compile(s);
            } catch (Exception exception) {
                RegexFilter.LOGGER.error("RegexFilter caught exception compiling pattern: " + s + " cause: " + exception.getMessage());
                return null;
            }

            Filter.Result filter_result = Filter.Result.toResult(s2);
            Filter.Result filter_result1 = Filter.Result.toResult(s3);

            return new RegexFilter(flag, pattern, filter_result, filter_result1);
        }
    }
}
