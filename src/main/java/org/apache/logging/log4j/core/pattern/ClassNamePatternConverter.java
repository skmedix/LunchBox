package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "ClassNamePatternConverter",
    category = "Converter"
)
@ConverterKeys({ "C", "class"})
public final class ClassNamePatternConverter extends NamePatternConverter {

    private static final String NA = "?";

    private ClassNamePatternConverter(String[] astring) {
        super("Class Name", "class name", astring);
    }

    public static ClassNamePatternConverter newInstance(String[] astring) {
        return new ClassNamePatternConverter(astring);
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        StackTraceElement stacktraceelement = logevent.getSource();

        if (stacktraceelement == null) {
            stringbuilder.append("?");
        } else {
            stringbuilder.append(this.abbreviate(stacktraceelement.getClassName()));
        }

    }
}
