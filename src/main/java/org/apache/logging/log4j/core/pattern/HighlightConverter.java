package org.apache.logging.log4j.core.pattern;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(
    name = "highlight",
    category = "Converter"
)
@ConverterKeys({ "highlight"})
public final class HighlightConverter extends LogEventPatternConverter {

    private static final EnumMap DEFAULT_STYLES = new EnumMap(Level.class);
    private static final EnumMap LOGBACK_STYLES = new EnumMap(Level.class);
    private static final String STYLE_KEY = "STYLE";
    private static final String STYLE_KEY_DEFAULT = "DEFAULT";
    private static final String STYLE_KEY_LOGBACK = "LOGBACK";
    private static final Map STYLES = new HashMap();
    private final EnumMap levelStyles;
    private final List patternFormatters;

    private static EnumMap createLevelStyleMap(String[] astring) {
        if (astring.length < 2) {
            return HighlightConverter.DEFAULT_STYLES;
        } else {
            Map map = AnsiEscape.createMap(astring[1], new String[] { "STYLE"});
            EnumMap enummap = new EnumMap(HighlightConverter.DEFAULT_STYLES);
            Iterator iterator = map.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                String s = ((String) entry.getKey()).toUpperCase(Locale.ENGLISH);
                String s1 = (String) entry.getValue();

                if ("STYLE".equalsIgnoreCase(s)) {
                    EnumMap enummap1 = (EnumMap) HighlightConverter.STYLES.get(s1.toUpperCase(Locale.ENGLISH));

                    if (enummap1 == null) {
                        HighlightConverter.LOGGER.error("Unknown level style: " + s1 + ". Use one of " + Arrays.toString(HighlightConverter.STYLES.keySet().toArray()));
                    } else {
                        enummap.putAll(enummap1);
                    }
                } else {
                    Level level = Level.valueOf(s);

                    if (level == null) {
                        HighlightConverter.LOGGER.error("Unknown level name: " + s + ". Use one of " + Arrays.toString(HighlightConverter.DEFAULT_STYLES.keySet().toArray()));
                    } else {
                        enummap.put(level, s1);
                    }
                }
            }

            return enummap;
        }
    }

    public static HighlightConverter newInstance(Configuration configuration, String[] astring) {
        if (astring.length < 1) {
            HighlightConverter.LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + astring.length);
            return null;
        } else if (astring[0] == null) {
            HighlightConverter.LOGGER.error("No pattern supplied on style");
            return null;
        } else {
            PatternParser patternparser = PatternLayout.createPatternParser(configuration);
            List list = patternparser.parse(astring[0]);

            return new HighlightConverter(list, createLevelStyleMap(astring));
        }
    }

    private HighlightConverter(List list, EnumMap enummap) {
        super("style", "style");
        this.patternFormatters = list;
        this.levelStyles = enummap;
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        StringBuilder stringbuilder1 = new StringBuilder();
        Iterator iterator = this.patternFormatters.iterator();

        while (iterator.hasNext()) {
            PatternFormatter patternformatter = (PatternFormatter) iterator.next();

            patternformatter.format(logevent, stringbuilder1);
        }

        if (stringbuilder1.length() > 0) {
            stringbuilder.append((String) this.levelStyles.get(logevent.getLevel())).append(stringbuilder1.toString()).append(AnsiEscape.getDefaultStyle());
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

    static {
        HighlightConverter.DEFAULT_STYLES.put(Level.FATAL, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED"}));
        HighlightConverter.DEFAULT_STYLES.put(Level.ERROR, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED"}));
        HighlightConverter.DEFAULT_STYLES.put(Level.WARN, AnsiEscape.createSequence(new String[] { "YELLOW"}));
        HighlightConverter.DEFAULT_STYLES.put(Level.INFO, AnsiEscape.createSequence(new String[] { "GREEN"}));
        HighlightConverter.DEFAULT_STYLES.put(Level.DEBUG, AnsiEscape.createSequence(new String[] { "CYAN"}));
        HighlightConverter.DEFAULT_STYLES.put(Level.TRACE, AnsiEscape.createSequence(new String[] { "BLACK"}));
        HighlightConverter.LOGBACK_STYLES.put(Level.FATAL, AnsiEscape.createSequence(new String[] { "BLINK", "BRIGHT", "RED"}));
        HighlightConverter.LOGBACK_STYLES.put(Level.ERROR, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED"}));
        HighlightConverter.LOGBACK_STYLES.put(Level.WARN, AnsiEscape.createSequence(new String[] { "RED"}));
        HighlightConverter.LOGBACK_STYLES.put(Level.INFO, AnsiEscape.createSequence(new String[] { "BLUE"}));
        HighlightConverter.LOGBACK_STYLES.put(Level.DEBUG, AnsiEscape.createSequence((String[]) null));
        HighlightConverter.LOGBACK_STYLES.put(Level.TRACE, AnsiEscape.createSequence((String[]) null));
        HighlightConverter.STYLES.put("DEFAULT", HighlightConverter.DEFAULT_STYLES);
        HighlightConverter.STYLES.put("LOGBACK", HighlightConverter.LOGBACK_STYLES);
    }
}
