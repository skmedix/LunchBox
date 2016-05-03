package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "ThreadPatternConverter",
    category = "Converter"
)
@ConverterKeys({ "t", "thread"})
public final class ThreadPatternConverter extends LogEventPatternConverter {

    private static final ThreadPatternConverter INSTANCE = new ThreadPatternConverter();

    private ThreadPatternConverter() {
        super("Thread", "thread");
    }

    public static ThreadPatternConverter newInstance(String[] astring) {
        return ThreadPatternConverter.INSTANCE;
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        stringbuilder.append(logevent.getThreadName());
    }
}
