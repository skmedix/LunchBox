package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "MethodLocationPatternConverter",
    category = "Converter"
)
@ConverterKeys({ "M", "method"})
public final class MethodLocationPatternConverter extends LogEventPatternConverter {

    private static final MethodLocationPatternConverter INSTANCE = new MethodLocationPatternConverter();

    private MethodLocationPatternConverter() {
        super("Method", "method");
    }

    public static MethodLocationPatternConverter newInstance(String[] astring) {
        return MethodLocationPatternConverter.INSTANCE;
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        StackTraceElement stacktraceelement = logevent.getSource();

        if (stacktraceelement != null) {
            stringbuilder.append(stacktraceelement.getMethodName());
        }

    }
}
