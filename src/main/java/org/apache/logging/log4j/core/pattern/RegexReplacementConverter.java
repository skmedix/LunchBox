package org.apache.logging.log4j.core.pattern;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(
    name = "replace",
    category = "Converter"
)
@ConverterKeys({ "replace"})
public final class RegexReplacementConverter extends LogEventPatternConverter {

    private final Pattern pattern;
    private final String substitution;
    private final List formatters;

    private RegexReplacementConverter(List list, Pattern pattern, String s) {
        super("replace", "replace");
        this.pattern = pattern;
        this.substitution = s;
        this.formatters = list;
    }

    public static RegexReplacementConverter newInstance(Configuration configuration, String[] astring) {
        if (astring.length != 3) {
            RegexReplacementConverter.LOGGER.error("Incorrect number of options on replace. Expected 3 received " + astring.length);
            return null;
        } else if (astring[0] == null) {
            RegexReplacementConverter.LOGGER.error("No pattern supplied on replace");
            return null;
        } else if (astring[1] == null) {
            RegexReplacementConverter.LOGGER.error("No regular expression supplied on replace");
            return null;
        } else if (astring[2] == null) {
            RegexReplacementConverter.LOGGER.error("No substitution supplied on replace");
            return null;
        } else {
            Pattern pattern = Pattern.compile(astring[1]);
            PatternParser patternparser = PatternLayout.createPatternParser(configuration);
            List list = patternparser.parse(astring[0]);

            return new RegexReplacementConverter(list, pattern, astring[2]);
        }
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        StringBuilder stringbuilder1 = new StringBuilder();
        Iterator iterator = this.formatters.iterator();

        while (iterator.hasNext()) {
            PatternFormatter patternformatter = (PatternFormatter) iterator.next();

            patternformatter.format(logevent, stringbuilder1);
        }

        stringbuilder.append(this.pattern.matcher(stringbuilder1.toString()).replaceAll(this.substitution));
    }
}
