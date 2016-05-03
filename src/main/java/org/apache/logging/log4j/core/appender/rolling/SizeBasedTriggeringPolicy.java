package org.apache.logging.log4j.core.appender.rolling;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "SizeBasedTriggeringPolicy",
    category = "Core",
    printObject = true
)
public class SizeBasedTriggeringPolicy implements TriggeringPolicy {

    protected static final Logger LOGGER = StatusLogger.getLogger();
    private static final long KB = 1024L;
    private static final long MB = 1048576L;
    private static final long GB = 1073741824L;
    private static final long MAX_FILE_SIZE = 10485760L;
    private static final Pattern VALUE_PATTERN = Pattern.compile("([0-9]+([\\.,][0-9]+)?)\\s*(|K|M|G)B?", 2);
    private final long maxFileSize;
    private RollingFileManager manager;

    protected SizeBasedTriggeringPolicy() {
        this.maxFileSize = 10485760L;
    }

    protected SizeBasedTriggeringPolicy(long i) {
        this.maxFileSize = i;
    }

    public void initialize(RollingFileManager rollingfilemanager) {
        this.manager = rollingfilemanager;
    }

    public boolean isTriggeringEvent(LogEvent logevent) {
        return this.manager.getFileSize() > this.maxFileSize;
    }

    public String toString() {
        return "SizeBasedTriggeringPolicy(size=" + this.maxFileSize + ")";
    }

    @PluginFactory
    public static SizeBasedTriggeringPolicy createPolicy(@PluginAttribute("size") String s) {
        long i = s == null ? 10485760L : valueOf(s);

        return new SizeBasedTriggeringPolicy(i);
    }

    private static long valueOf(String s) {
        Matcher matcher = SizeBasedTriggeringPolicy.VALUE_PATTERN.matcher(s);

        if (matcher.matches()) {
            try {
                long i = NumberFormat.getNumberInstance(Locale.getDefault()).parse(matcher.group(1)).longValue();
                String s1 = matcher.group(3);

                if (s1.equalsIgnoreCase("")) {
                    return i;
                } else if (s1.equalsIgnoreCase("K")) {
                    return i * 1024L;
                } else if (s1.equalsIgnoreCase("M")) {
                    return i * 1048576L;
                } else if (s1.equalsIgnoreCase("G")) {
                    return i * 1073741824L;
                } else {
                    SizeBasedTriggeringPolicy.LOGGER.error("Units not recognized: " + s);
                    return 10485760L;
                }
            } catch (ParseException parseexception) {
                SizeBasedTriggeringPolicy.LOGGER.error("Unable to parse numeric part: " + s, (Throwable) parseexception);
                return 10485760L;
            }
        } else {
            SizeBasedTriggeringPolicy.LOGGER.error("Unable to parse bytes: " + s);
            return 10485760L;
        }
    }
}
