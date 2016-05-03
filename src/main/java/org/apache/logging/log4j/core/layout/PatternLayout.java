package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.OptionConverter;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.core.pattern.RegexReplacement;

@Plugin(
    name = "PatternLayout",
    category = "Core",
    elementType = "layout",
    printObject = true
)
public final class PatternLayout extends AbstractStringLayout {

    public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";
    public static final String TTCC_CONVERSION_PATTERN = "%r [%t] %p %c %x - %m%n";
    public static final String SIMPLE_CONVERSION_PATTERN = "%d [%t] %p %c - %m%n";
    public static final String KEY = "Converter";
    private List formatters;
    private final String conversionPattern;
    private final Configuration config;
    private final RegexReplacement replace;
    private final boolean alwaysWriteExceptions;

    private PatternLayout(Configuration configuration, RegexReplacement regexreplacement, String s, Charset charset, boolean flag) {
        super(charset);
        this.replace = regexreplacement;
        this.conversionPattern = s;
        this.config = configuration;
        this.alwaysWriteExceptions = flag;
        PatternParser patternparser = createPatternParser(configuration);

        this.formatters = patternparser.parse(s == null ? "%m%n" : s, this.alwaysWriteExceptions);
    }

    public void setConversionPattern(String s) {
        String s1 = OptionConverter.convertSpecialChars(s);

        if (s1 != null) {
            PatternParser patternparser = createPatternParser(this.config);

            this.formatters = patternparser.parse(s1, this.alwaysWriteExceptions);
        }
    }

    public String getConversionPattern() {
        return this.conversionPattern;
    }

    public Map getContentFormat() {
        HashMap hashmap = new HashMap();

        hashmap.put("structured", "false");
        hashmap.put("formatType", "conversion");
        hashmap.put("format", this.conversionPattern);
        return hashmap;
    }

    public String toSerializable(LogEvent logevent) {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = this.formatters.iterator();

        while (iterator.hasNext()) {
            PatternFormatter patternformatter = (PatternFormatter) iterator.next();

            patternformatter.format(logevent, stringbuilder);
        }

        String s = stringbuilder.toString();

        if (this.replace != null) {
            s = this.replace.format(s);
        }

        return s;
    }

    public static PatternParser createPatternParser(Configuration configuration) {
        if (configuration == null) {
            return new PatternParser(configuration, "Converter", LogEventPatternConverter.class);
        } else {
            PatternParser patternparser = (PatternParser) configuration.getComponent("Converter");

            if (patternparser == null) {
                patternparser = new PatternParser(configuration, "Converter", LogEventPatternConverter.class);
                configuration.addComponent("Converter", patternparser);
                patternparser = (PatternParser) configuration.getComponent("Converter");
            }

            return patternparser;
        }
    }

    public String toString() {
        return this.conversionPattern;
    }

    @PluginFactory
    public static PatternLayout createLayout(@PluginAttribute("pattern") String s, @PluginConfiguration Configuration configuration, @PluginElement("Replace") RegexReplacement regexreplacement, @PluginAttribute("charset") String s1, @PluginAttribute("alwaysWriteExceptions") String s2) {
        Charset charset = Charsets.getSupportedCharset(s1);
        boolean flag = Booleans.parseBoolean(s2, true);

        return new PatternLayout(configuration, regexreplacement, s == null ? "%m%n" : s, charset, flag);
    }
}
