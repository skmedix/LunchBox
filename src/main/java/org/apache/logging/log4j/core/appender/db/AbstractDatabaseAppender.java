package org.apache.logging.log4j.core.appender.db;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;

public abstract class AbstractDatabaseAppender extends AbstractAppender {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock;
    private final Lock writeLock;
    private AbstractDatabaseManager manager;

    protected AbstractDatabaseAppender(String s, Filter filter, boolean flag, AbstractDatabaseManager abstractdatabasemanager) {
        super(s, filter, (Layout) null, flag);
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
        this.manager = abstractdatabasemanager;
    }

    public final Layout getLayout() {
        return null;
    }

    public final AbstractDatabaseManager getManager() {
        return this.manager;
    }

    public final void start() {
        if (this.getManager() == null) {
            AbstractDatabaseAppender.LOGGER.error("No AbstractDatabaseManager set for the appender named [{}].", new Object[] { this.getName()});
        }

        super.start();
        if (this.getManager() != null) {
            this.getManager().connect();
        }

    }

    public final void stop() {
        super.stop();
        if (this.getManager() != null) {
            this.getManager().release();
        }

    }

    public final void append(LogEvent logevent) {
        this.readLock.lock();

        try {
            this.getManager().write(logevent);
        } catch (LoggingException loggingexception) {
            AbstractDatabaseAppender.LOGGER.error("Unable to write to database [{}] for appender [{}].", new Object[] { this.getManager().getName(), this.getName(), loggingexception});
            throw loggingexception;
        } catch (Exception exception) {
            AbstractDatabaseAppender.LOGGER.error("Unable to write to database [{}] for appender [{}].", new Object[] { this.getManager().getName(), this.getName(), exception});
            throw new AppenderLoggingException("Unable to write to database in appender: " + exception.getMessage(), exception);
        } finally {
            this.readLock.unlock();
        }

    }

    protected final void replaceManager(AbstractDatabaseManager abstractdatabasemanager) {
        this.writeLock.lock();

        try {
            AbstractDatabaseManager abstractdatabasemanager1 = this.getManager();

            if (!abstractdatabasemanager.isConnected()) {
                abstractdatabasemanager.connect();
            }

            this.manager = abstractdatabasemanager;
            abstractdatabasemanager1.release();
        } finally {
            this.writeLock.unlock();
        }

    }
}
