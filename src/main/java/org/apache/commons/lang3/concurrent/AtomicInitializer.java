package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AtomicInitializer implements ConcurrentInitializer {

    private final AtomicReference reference = new AtomicReference();

    public Object get() throws ConcurrentException {
        Object object = this.reference.get();

        if (object == null) {
            object = this.initialize();
            if (!this.reference.compareAndSet((Object) null, object)) {
                object = this.reference.get();
            }
        }

        return object;
    }

    protected abstract Object initialize() throws ConcurrentException;
}
