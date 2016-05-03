package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;

public abstract class LogEventPatternConverter extends AbstractPatternConverter {

    protected LogEventPatternConverter(String s, String s1) {
        super(s, s1);
    }

    public abstract void format(LogEvent logevent, StringBuilder stringbuilder);

    public void format(Object object, StringBuilder stringbuilder) {
        if (object instanceof LogEvent) {
            this.format((LogEvent) object, stringbuilder);
        }

    }

    public boolean handlesThrowable() {
        return false;
    }
}
