package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "LoggerPatternConverter",
    category = "Converter"
)
@ConverterKeys({ "c", "logger"})
public final class LoggerPatternConverter extends NamePatternConverter {

    private static final LoggerPatternConverter INSTANCE = new LoggerPatternConverter((String[]) null);

    private LoggerPatternConverter(String[] astring) {
        super("Logger", "logger", astring);
    }

    public static LoggerPatternConverter newInstance(String[] astring) {
        return astring != null && astring.length != 0 ? new LoggerPatternConverter(astring) : LoggerPatternConverter.INSTANCE;
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        stringbuilder.append(this.abbreviate(logevent.getLoggerName()));
    }
}
