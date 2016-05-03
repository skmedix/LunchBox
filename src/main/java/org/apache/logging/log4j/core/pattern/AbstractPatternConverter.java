package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractPatternConverter implements PatternConverter {

    protected static final Logger LOGGER = StatusLogger.getLogger();
    private final String name;
    private final String style;

    protected AbstractPatternConverter(String s, String s1) {
        this.name = s;
        this.style = s1;
    }

    public final String getName() {
        return this.name;
    }

    public String getStyleClass(Object object) {
        return this.style;
    }
}
