package org.apache.logging.log4j.core.pattern;

import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(
    name = "style",
    category = "Converter"
)
@ConverterKeys({ "style"})
public final class StyleConverter extends LogEventPatternConverter {

    private final List patternFormatters;
    private final String style;

    private StyleConverter(List list, String s) {
        super("style", "style");
        this.patternFormatters = list;
        this.style = s;
    }

    public static StyleConverter newInstance(Configuration configuration, String[] astring) {
        if (astring.length < 1) {
            StyleConverter.LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + astring.length);
            return null;
        } else if (astring[0] == null) {
            StyleConverter.LOGGER.error("No pattern supplied on style");
            return null;
        } else if (astring[1] == null) {
            StyleConverter.LOGGER.error("No style attributes provided");
            return null;
        } else {
            PatternParser patternparser = PatternLayout.createPatternParser(configuration);
            List list = patternparser.parse(astring[0]);
            String s = AnsiEscape.createSequence(astring[1].split("\\s*,\\s*"));

            return new StyleConverter(list, s);
        }
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        StringBuilder stringbuilder1 = new StringBuilder();
        Iterator iterator = this.patternFormatters.iterator();

        while (iterator.hasNext()) {
            PatternFormatter patternformatter = (PatternFormatter) iterator.next();

            patternformatter.format(logevent, stringbuilder1);
        }

        if (stringbuilder1.length() > 0) {
            stringbuilder.append(this.style).append(stringbuilder1.toString()).append(AnsiEscape.getDefaultStyle());
        }

    }

    public boolean handlesThrowable() {
        Iterator iterator = this.patternFormatters.iterator();

        PatternFormatter patternformatter;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            patternformatter = (PatternFormatter) iterator.next();
        } while (!patternformatter.handlesThrowable());

        return true;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(super.toString());
        stringbuilder.append("[style=");
        stringbuilder.append(this.style);
        stringbuilder.append(", patternFormatters=");
        stringbuilder.append(this.patternFormatters);
        stringbuilder.append("]");
        return stringbuilder.toString();
    }
}
