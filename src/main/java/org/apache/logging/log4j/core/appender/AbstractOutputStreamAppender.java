package org.apache.logging.log4j.core.appender;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;

public abstract class AbstractOutputStreamAppender extends AbstractAppender {

    protected final boolean immediateFlush;
    private volatile OutputStreamManager manager;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock;
    private final Lock writeLock;

    protected AbstractOutputStreamAppender(String s, Layout layout, Filter filter, boolean flag, boolean flag1, OutputStreamManager outputstreammanager) {
        super(s, filter, layout, flag);
        this.readLock = this.rwLock.readLock();
        this.writeLock = this.rwLock.writeLock();
        this.manager = outputstreammanager;
        this.immediateFlush = flag1;
    }

    protected OutputStreamManager getManager() {
        return this.manager;
    }

    protected void replaceManager(OutputStreamManager outputstreammanager) {
        this.writeLock.lock();

        try {
            OutputStreamManager outputstreammanager1 = this.manager;

            this.manager = outputstreammanager;
            outputstreammanager1.release();
        } finally {
            this.writeLock.unlock();
        }

    }

    public void start() {
        if (this.getLayout() == null) {
            AbstractOutputStreamAppender.LOGGER.error("No layout set for the appender named [" + this.getName() + "].");
        }

        if (this.manager == null) {
            AbstractOutputStreamAppender.LOGGER.error("No OutputStreamManager set for the appender named [" + this.getName() + "].");
        }

        super.start();
    }

    public void stop() {
        super.stop();
        this.manager.release();
    }

    public void append(LogEvent logevent) {
        this.readLock.lock();

        try {
            byte[] abyte = this.getLayout().toByteArray(logevent);

            if (abyte.length > 0) {
                this.manager.write(abyte);
                if (this.immediateFlush || logevent.isEndOfBatch()) {
                    this.manager.flush();
                }
            }
        } catch (AppenderLoggingException appenderloggingexception) {
            this.error("Unable to write to stream " + this.manager.getName() + " for appender " + this.getName());
            throw appenderloggingexception;
        } finally {
            this.readLock.unlock();
        }

    }
}
