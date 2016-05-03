package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;

public final class LiteralPatternConverter extends LogEventPatternConverter implements ArrayPatternConverter {

    private final String literal;
    private final Configuration config;
    private final boolean substitute;

    public LiteralPatternConverter(Configuration configuration, String s) {
        super("Literal", "literal");
        this.literal = s;
        this.config = configuration;
        this.substitute = configuration != null && s.contains("${");
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        stringbuilder.append(this.substitute ? this.config.getStrSubstitutor().replace(logevent, this.literal) : this.literal);
    }

    public void format(Object object, StringBuilder stringbuilder) {
        stringbuilder.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
    }

    public void format(StringBuilder stringbuilder, Object... aobject) {
        stringbuilder.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
    }

    public String getLiteral() {
        return this.literal;
    }
}
