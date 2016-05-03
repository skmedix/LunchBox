package org.apache.logging.log4j.core.pattern;

import java.util.UUID;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.helpers.UUIDUtil;

@Plugin(
    name = "UUIDPatternConverter",
    category = "Converter"
)
@ConverterKeys({ "u", "uuid"})
public final class UUIDPatternConverter extends LogEventPatternConverter {

    private final boolean isRandom;

    private UUIDPatternConverter(boolean flag) {
        super("u", "uuid");
        this.isRandom = flag;
    }

    public static UUIDPatternConverter newInstance(String[] astring) {
        if (astring.length == 0) {
            return new UUIDPatternConverter(false);
        } else {
            if (astring.length > 1 || !astring[0].equalsIgnoreCase("RANDOM") && !astring[0].equalsIgnoreCase("Time")) {
                UUIDPatternConverter.LOGGER.error("UUID Pattern Converter only accepts a single option with the value \"RANDOM\" or \"TIME\"");
            }

            return new UUIDPatternConverter(astring[0].equalsIgnoreCase("RANDOM"));
        }
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        UUID uuid = this.isRandom ? UUID.randomUUID() : UUIDUtil.getTimeBasedUUID();

        stringbuilder.append(uuid.toString());
    }
}
