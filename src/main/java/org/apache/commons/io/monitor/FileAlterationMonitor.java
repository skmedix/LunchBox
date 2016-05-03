package org.apache.commons.io.monitor;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;

public final class FileAlterationMonitor implements Runnable {

    private final long interval;
    private final List observers;
    private Thread thread;
    private ThreadFactory threadFactory;
    private volatile boolean running;

    public FileAlterationMonitor() {
        this(10000L);
    }

    public FileAlterationMonitor(long i) {
        this.observers = new CopyOnWriteArrayList();
        this.thread = null;
        this.running = false;
        this.interval = i;
    }

    public FileAlterationMonitor(long i, FileAlterationObserver... afilealterationobserver) {
        this(i);
        if (afilealterationobserver != null) {
            FileAlterationObserver[] afilealterationobserver1 = afilealterationobserver;
            int j = afilealterationobserver.length;

            for (int k = 0; k < j; ++k) {
                FileAlterationObserver filealterationobserver = afilealterationobserver1[k];

                this.addObserver(filealterationobserver);
            }
        }

    }

    public long getInterval() {
        return this.interval;
    }

    public synchronized void setThreadFactory(ThreadFactory threadfactory) {
        this.threadFactory = threadfactory;
    }

    public void addObserver(FileAlterationObserver filealterationobserver) {
        if (filealterationobserver != null) {
            this.observers.add(filealterationobserver);
        }

    }

    public void removeObserver(FileAlterationObserver filealterationobserver) {
        if (filealterationobserver != null) {
            while (true) {
                if (this.observers.remove(filealterationobserver)) {
                    continue;
                }
            }
        }

    }

    public Iterable getObservers() {
        return this.observers;
    }

    public synchronized void start() throws Exception {
        if (this.running) {
            throw new IllegalStateException("Monitor is already running");
        } else {
            Iterator iterator = this.observers.iterator();

            while (iterator.hasNext()) {
                FileAlterationObserver filealterationobserver = (FileAlterationObserver) iterator.next();

                filealterationobserver.initialize();
            }

            this.running = true;
            if (this.threadFactory != null) {
                this.thread = this.threadFactory.newThread(this);
            } else {
                this.thread = new Thread(this);
            }

            this.thread.start();
        }
    }

    public synchronized void stop() throws Exception {
        this.stop(this.interval);
    }

    public synchronized void stop(long i) throws Exception {
        if (!this.running) {
            throw new IllegalStateException("Monitor is not running");
        } else {
            this.running = false;

            try {
                this.thread.join(i);
            } catch (InterruptedException interruptedexception) {
                Thread.currentThread().interrupt();
            }

            Iterator iterator = this.observers.iterator();

            while (iterator.hasNext()) {
                FileAlterationObserver filealterationobserver = (FileAlterationObserver) iterator.next();

                filealterationobserver.destroy();
            }

        }
    }

    public void run() {
        while (true) {
            if (this.running) {
                Iterator iterator = this.observers.iterator();

                while (iterator.hasNext()) {
                    FileAlterationObserver filealterationobserver = (FileAlterationObserver) iterator.next();

                    filealterationobserver.checkAndNotify();
                }

                if (this.running) {
                    try {
                        Thread.sleep(this.interval);
                    } catch (InterruptedException interruptedexception) {
                        ;
                    }
                    continue;
                }
            }

            return;
        }
    }
}
