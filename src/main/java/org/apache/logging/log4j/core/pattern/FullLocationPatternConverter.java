package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "FullLocationPatternConverter",
    category = "Converter"
)
@ConverterKeys({ "l", "location"})
public final class FullLocationPatternConverter extends LogEventPatternConverter {

    private static final FullLocationPatternConverter INSTANCE = new FullLocationPatternConverter();

    private FullLocationPatternConverter() {
        super("Full Location", "fullLocation");
    }

    public static FullLocationPatternConverter newInstance(String[] astring) {
        return FullLocationPatternConverter.INSTANCE;
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        StackTraceElement stacktraceelement = logevent.getSource();

        if (stacktraceelement != null) {
            stringbuilder.append(stacktraceelement.toString());
        }

    }
}
