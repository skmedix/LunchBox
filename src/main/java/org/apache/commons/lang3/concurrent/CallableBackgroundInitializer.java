package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class CallableBackgroundInitializer extends BackgroundInitializer {

    private final Callable callable;

    public CallableBackgroundInitializer(Callable callable) {
        this.checkCallable(callable);
        this.callable = callable;
    }

    public CallableBackgroundInitializer(Callable callable, ExecutorService executorservice) {
        super(executorservice);
        this.checkCallable(callable);
        this.callable = callable;
    }

    protected Object initialize() throws Exception {
        return this.callable.call();
    }

    private void checkCallable(Callable callable) {
        if (callable == null) {
            throw new IllegalArgumentException("Callable must not be null!");
        }
    }
}
