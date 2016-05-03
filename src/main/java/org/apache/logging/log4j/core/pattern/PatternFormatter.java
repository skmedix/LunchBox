package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;

public class PatternFormatter {

    private final LogEventPatternConverter converter;
    private final FormattingInfo field;

    public PatternFormatter(LogEventPatternConverter logeventpatternconverter, FormattingInfo formattinginfo) {
        this.converter = logeventpatternconverter;
        this.field = formattinginfo;
    }

    public void format(LogEvent logevent, StringBuilder stringbuilder) {
        int i = stringbuilder.length();

        this.converter.format(logevent, stringbuilder);
        this.field.format(i, stringbuilder);
    }

    public LogEventPatternConverter getConverter() {
        return this.converter;
    }

    public FormattingInfo getFormattingInfo() {
        return this.field;
    }

    public boolean handlesThrowable() {
        return this.converter.handlesThrowable();
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(super.toString());
        stringbuilder.append("[converter=");
        stringbuilder.append(this.converter);
        stringbuilder.append(", field=");
        stringbuilder.append(this.field);
        stringbuilder.append("]");
        return stringbuilder.toString();
    }
}
