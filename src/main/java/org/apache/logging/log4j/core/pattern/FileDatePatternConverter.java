package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
    name = "FileDatePatternConverter",
    category = "FileConverter"
)
@ConverterKeys({ "d", "date"})
public final class FileDatePatternConverter {

    public static PatternConverter newInstance(String[] astring) {
        return astring != null && astring.length != 0 ? DatePatternConverter.newInstance(astring) : DatePatternConverter.newInstance(new String[] { "yyyy-MM-dd"});
    }
}
