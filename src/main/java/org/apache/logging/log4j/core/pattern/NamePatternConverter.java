package org.apache.logging.log4j.core.pattern;

public abstract class NamePatternConverter extends LogEventPatternConverter {

    private final NameAbbreviator abbreviator;

    protected NamePatternConverter(String s, String s1, String[] astring) {
        super(s, s1);
        if (astring != null && astring.length > 0) {
            this.abbreviator = NameAbbreviator.getAbbreviator(astring[0]);
        } else {
            this.abbreviator = NameAbbreviator.getDefaultAbbreviator();
        }

    }

    protected final String abbreviate(String s) {
        return this.abbreviator.abbreviate(s);
    }
}
