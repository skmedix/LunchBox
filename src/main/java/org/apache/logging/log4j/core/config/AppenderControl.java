package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.filter.Filterable;

public class AppenderControl extends AbstractFilterable {

    private final ThreadLocal recursive = new ThreadLocal();
    private final Appender appender;
    private final Level level;
    private final int intLevel;

    public AppenderControl(Appender appender, Level level, Filter filter) {
        super(filter);
        this.appender = appender;
        this.level = level;
        this.intLevel = level == null ? Level.ALL.intLevel() : level.intLevel();
        this.startFilter();
    }

    public Appender getAppender() {
        return this.appender;
    }

    public void callAppender(LogEvent logevent) {
        if (this.getFilter() != null) {
            Filter.Result filter_result = this.getFilter().filter(logevent);

            if (filter_result == Filter.Result.DENY) {
                return;
            }
        }

        if (this.level == null || this.intLevel >= logevent.getLevel().intLevel()) {
            if (this.recursive.get() != null) {
                this.appender.getHandler().error("Recursive call to appender " + this.appender.getName());
            } else {
                try {
                    this.recursive.set(this);
                    if (!this.appender.isStarted()) {
                        this.appender.getHandler().error("Attempted to append to non-started appender " + this.appender.getName());
                        if (!this.appender.ignoreExceptions()) {
                            throw new AppenderLoggingException("Attempted to append to non-started appender " + this.appender.getName());
                        }
                    }

                    if (this.appender instanceof Filterable && ((Filterable) this.appender).isFiltered(logevent)) {
                        return;
                    }

                    try {
                        this.appender.append(logevent);
                    } catch (RuntimeException runtimeexception) {
                        this.appender.getHandler().error("An exception occurred processing Appender " + this.appender.getName(), runtimeexception);
                        if (!this.appender.ignoreExceptions()) {
                            throw runtimeexception;
                        }
                    } catch (Exception exception) {
                        this.appender.getHandler().error("An exception occurred processing Appender " + this.appender.getName(), exception);
                        if (!this.appender.ignoreExceptions()) {
                            throw new AppenderLoggingException(exception);
                        }
                    }
                } finally {
                    this.recursive.set((Object) null);
                }

            }
        }
    }
}
