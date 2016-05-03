package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "LineLocationPatternConverter",
    category = "Converter"
)
@ConverterKeys({ "L", "line"})
public final class LineLocationPatternConverter extends LogEventPatternConverter {

    private static final LineLocationPatternConverter INSTANCE = new LineLocationPatternConverter();

    private LineLocationPatternConverter() {
        super("Line", "line");
    }

    public static LineLocationPatternConverter newInstance(String[] astring) {
        return LineLocationPatternConverter.INSTANCE;
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        StackTraceElement stacktraceelement = logevent.getSource();

        if (stacktraceelement != null) {
            stringbuilder.append(stacktraceelement.getLineNumber());
        }

    }
}
