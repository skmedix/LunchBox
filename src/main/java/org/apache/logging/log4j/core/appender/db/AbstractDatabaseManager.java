package org.apache.logging.log4j.core.appender.db;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public abstract class AbstractDatabaseManager extends AbstractManager {

    private final ArrayList buffer;
    private final int bufferSize;
    private boolean connected = false;

    protected AbstractDatabaseManager(String s, int i) {
        super(s);
        this.bufferSize = i;
        this.buffer = new ArrayList(i + 1);
    }

    protected abstract void connectInternal() throws Exception;

    public final synchronized void connect() {
        if (!this.isConnected()) {
            try {
                this.connectInternal();
                this.connected = true;
            } catch (Exception exception) {
                AbstractDatabaseManager.LOGGER.error("Could not connect to database using logging manager [{}].", new Object[] { this.getName(), exception});
            }
        }

    }

    protected abstract void disconnectInternal() throws Exception;

    public final synchronized void disconnect() {
        this.flush();
        if (this.isConnected()) {
            try {
                this.disconnectInternal();
            } catch (Exception exception) {
                AbstractDatabaseManager.LOGGER.warn("Error while disconnecting from database using logging manager [{}].", new Object[] { this.getName(), exception});
            } finally {
                this.connected = false;
            }
        }

    }

    public final boolean isConnected() {
        return this.connected;
    }

    protected abstract void writeInternal(LogEvent logevent);

    public final synchronized void flush() {
        if (this.isConnected() && this.buffer.size() > 0) {
            Iterator iterator = this.buffer.iterator();

            while (iterator.hasNext()) {
                LogEvent logevent = (LogEvent) iterator.next();

                this.writeInternal(logevent);
            }

            this.buffer.clear();
        }

    }

    public final synchronized void write(LogEvent logevent) {
        if (this.bufferSize > 0) {
            this.buffer.add(logevent);
            if (this.buffer.size() >= this.bufferSize || logevent.isEndOfBatch()) {
                this.flush();
            }
        } else {
            this.writeInternal(logevent);
        }

    }

    public final void releaseSub() {
        this.disconnect();
    }

    public final String toString() {
        return this.getName();
    }

    protected static AbstractDatabaseManager getManager(String s, AbstractDatabaseManager.AbstractFactoryData abstractdatabasemanager_abstractfactorydata, ManagerFactory managerfactory) {
        return (AbstractDatabaseManager) AbstractManager.getManager(s, managerfactory, abstractdatabasemanager_abstractfactorydata);
    }

    protected abstract static class AbstractFactoryData {

        private final int bufferSize;

        protected AbstractFactoryData(int i) {
            this.bufferSize = i;
        }

        public int getBufferSize() {
            return this.bufferSize;
        }
    }
}
