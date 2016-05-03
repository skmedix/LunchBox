package org.apache.commons.lang3.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

public class CompositeFormat extends Format {

    private static final long serialVersionUID = -4329119827877627683L;
    private final Format parser;
    private final Format formatter;

    public CompositeFormat(Format format, Format format1) {
        this.parser = format;
        this.formatter = format1;
    }

    public StringBuffer format(Object object, StringBuffer stringbuffer, FieldPosition fieldposition) {
        return this.formatter.format(object, stringbuffer, fieldposition);
    }

    public Object parseObject(String s, ParsePosition parseposition) {
        return this.parser.parseObject(s, parseposition);
    }

    public Format getParser() {
        return this.parser;
    }

    public Format getFormatter() {
        return this.formatter;
    }

    public String reformat(String s) throws ParseException {
        return this.format(this.parseObject(s));
    }
}
