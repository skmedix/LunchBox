package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class BackgroundInitializer implements ConcurrentInitializer {

    private ExecutorService externalExecutor;
    private ExecutorService executor;
    private Future future;

    protected BackgroundInitializer() {
        this((ExecutorService) null);
    }

    protected BackgroundInitializer(ExecutorService executorservice) {
        this.setExternalExecutor(executorservice);
    }

    public final synchronized ExecutorService getExternalExecutor() {
        return this.externalExecutor;
    }

    public synchronized boolean isStarted() {
        return this.future != null;
    }

    public final synchronized void setExternalExecutor(ExecutorService executorservice) {
        if (this.isStarted()) {
            throw new IllegalStateException("Cannot set ExecutorService after start()!");
        } else {
            this.externalExecutor = executorservice;
        }
    }

    public synchronized boolean start() {
        if (!this.isStarted()) {
            this.executor = this.getExternalExecutor();
            ExecutorService executorservice;

            if (this.executor == null) {
                this.executor = executorservice = this.createExecutor();
            } else {
                executorservice = null;
            }

            this.future = this.executor.submit(this.createTask(executorservice));
            return true;
        } else {
            return false;
        }
    }

    public Object get() throws ConcurrentException {
        try {
            return this.getFuture().get();
        } catch (ExecutionException executionexception) {
            ConcurrentUtils.handleCause(executionexception);
            return null;
        } catch (InterruptedException interruptedexception) {
            Thread.currentThread().interrupt();
            throw new ConcurrentException(interruptedexception);
        }
    }

    public synchronized Future getFuture() {
        if (this.future == null) {
            throw new IllegalStateException("start() must be called first!");
        } else {
            return this.future;
        }
    }

    protected final synchronized ExecutorService getActiveExecutor() {
        return this.executor;
    }

    protected int getTaskCount() {
        return 1;
    }

    protected abstract Object initialize() throws Exception;

    private Callable createTask(ExecutorService executorservice) {
        return new BackgroundInitializer.InitializationTask(executorservice);
    }

    private ExecutorService createExecutor() {
        return Executors.newFixedThreadPool(this.getTaskCount());
    }

    private class InitializationTask implements Callable {

        private final ExecutorService execFinally;

        public InitializationTask(ExecutorService executorservice) {
            this.execFinally = executorservice;
        }

        public Object call() throws Exception {
            Object object;

            try {
                object = BackgroundInitializer.this.initialize();
            } finally {
                if (this.execFinally != null) {
                    this.execFinally.shutdown();
                }

            }

            return object;
        }
    }
}
