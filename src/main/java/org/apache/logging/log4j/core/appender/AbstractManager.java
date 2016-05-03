package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractManager {

    protected static final Logger LOGGER = StatusLogger.getLogger();
    private static final Map MAP = new HashMap();
    private static final Lock LOCK = new ReentrantLock();
    protected int count;
    private final String name;

    protected AbstractManager(String s) {
        this.name = s;
        AbstractManager.LOGGER.debug("Starting {} {}", new Object[] { this.getClass().getSimpleName(), s});
    }

    public static AbstractManager getManager(String s, ManagerFactory managerfactory, Object object) {
        AbstractManager.LOCK.lock();

        AbstractManager abstractmanager;

        try {
            AbstractManager abstractmanager1 = (AbstractManager) AbstractManager.MAP.get(s);

            if (abstractmanager1 == null) {
                abstractmanager1 = (AbstractManager) managerfactory.createManager(s, object);
                if (abstractmanager1 == null) {
                    throw new IllegalStateException("Unable to create a manager");
                }

                AbstractManager.MAP.put(s, abstractmanager1);
            }

            ++abstractmanager1.count;
            abstractmanager = abstractmanager1;
        } finally {
            AbstractManager.LOCK.unlock();
        }

        return abstractmanager;
    }

    public static boolean hasManager(String s) {
        AbstractManager.LOCK.lock();

        boolean flag;

        try {
            flag = AbstractManager.MAP.containsKey(s);
        } finally {
            AbstractManager.LOCK.unlock();
        }

        return flag;
    }

    protected void releaseSub() {}

    protected int getCount() {
        return this.count;
    }

    public void release() {
        AbstractManager.LOCK.lock();

        try {
            --this.count;
            if (this.count <= 0) {
                AbstractManager.MAP.remove(this.name);
                AbstractManager.LOGGER.debug("Shutting down {} {}", new Object[] { this.getClass().getSimpleName(), this.getName()});
                this.releaseSub();
            }
        } finally {
            AbstractManager.LOCK.unlock();
        }

    }

    public String getName() {
        return this.name;
    }

    public Map getContentFormat() {
        return new HashMap();
    }
}
