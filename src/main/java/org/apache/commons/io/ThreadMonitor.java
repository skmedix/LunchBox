package org.apache.commons.io;

class ThreadMonitor implements Runnable {

    private final Thread thread;
    private final long timeout;

    public static Thread start(long i) {
        return start(Thread.currentThread(), i);
    }

    public static Thread start(Thread thread, long i) {
        Thread thread1 = null;

        if (i > 0L) {
            ThreadMonitor threadmonitor = new ThreadMonitor(thread, i);

            thread1 = new Thread(threadmonitor, ThreadMonitor.class.getSimpleName());
            thread1.setDaemon(true);
            thread1.start();
        }

        return thread1;
    }

    public static void stop(Thread thread) {
        if (thread != null) {
            thread.interrupt();
        }

    }

    private ThreadMonitor(Thread thread, long i) {
        this.thread = thread;
        this.timeout = i;
    }

    public void run() {
        try {
            Thread.sleep(this.timeout);
            this.thread.interrupt();
        } catch (InterruptedException interruptedexception) {
            ;
        }

    }
}
