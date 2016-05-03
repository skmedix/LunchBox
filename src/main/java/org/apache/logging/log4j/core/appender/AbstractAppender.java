package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.helpers.Integers;

public abstract class AbstractAppender extends AbstractFilterable implements Appender {

    private final boolean ignoreExceptions;
    private ErrorHandler handler;
    private final Layout layout;
    private final String name;
    private boolean started;

    public static int parseInt(String s, int i) {
        try {
            return Integers.parseInt(s, i);
        } catch (NumberFormatException numberformatexception) {
            AbstractAppender.LOGGER.error("Could not parse \"{}\" as an integer,  using default value {}: {}", new Object[] { s, Integer.valueOf(i), numberformatexception});
            return i;
        }
    }

    protected AbstractAppender(String s, Filter filter, Layout layout) {
        this(s, filter, layout, true);
    }

    protected AbstractAppender(String s, Filter filter, Layout layout, boolean flag) {
        super(filter);
        this.handler = new DefaultErrorHandler(this);
        this.started = false;
        this.name = s;
        this.layout = layout;
        this.ignoreExceptions = flag;
    }

    public void error(String s) {
        this.handler.error(s);
    }

    public void error(String s, LogEvent logevent, Throwable throwable) {
        this.handler.error(s, logevent, throwable);
    }

    public void error(String s, Throwable throwable) {
        this.handler.error(s, throwable);
    }

    public ErrorHandler getHandler() {
        return this.handler;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public String getName() {
        return this.name;
    }

    public boolean ignoreExceptions() {
        return this.ignoreExceptions;
    }

    public boolean isStarted() {
        return this.started;
    }

    public void setHandler(ErrorHandler errorhandler) {
        if (errorhandler == null) {
            AbstractAppender.LOGGER.error("The handler cannot be set to null");
        }

        if (this.isStarted()) {
            AbstractAppender.LOGGER.error("The handler cannot be changed once the appender is started");
        } else {
            this.handler = errorhandler;
        }
    }

    public void start() {
        this.startFilter();
        this.started = true;
    }

    public void stop() {
        this.started = false;
        this.stopFilter();
    }

    public String toString() {
        return this.name;
    }
}
