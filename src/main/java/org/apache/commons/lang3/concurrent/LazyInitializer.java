package org.apache.commons.lang3.concurrent;

public abstract class LazyInitializer implements ConcurrentInitializer {

    private volatile Object object;

    public Object get() throws ConcurrentException {
        Object object = this.object;

        if (object == null) {
            synchronized (this) {
                object = this.object;
                if (object == null) {
                    this.object = object = this.initialize();
                }
            }
        }

        return object;
    }

    protected abstract Object initialize() throws ConcurrentException;
}
