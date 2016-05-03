package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "FileLocationPatternConverter",
    category = "Converter"
)
@ConverterKeys({ "F", "file"})
public final class FileLocationPatternConverter extends LogEventPatternConverter {

    private static final FileLocationPatternConverter INSTANCE = new FileLocationPatternConverter();

    private FileLocationPatternConverter() {
        super("File Location", "file");
    }

    public static FileLocationPatternConverter newInstance(String[] astring) {
        return FileLocationPatternConverter.INSTANCE;
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        StackTraceElement stacktraceelement = logevent.getSource();

        if (stacktraceelement != null) {
            stringbuilder.append(stacktraceelement.getFileName());
        }

    }
}
