package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AtomicSafeInitializer implements ConcurrentInitializer {

    private final AtomicReference factory = new AtomicReference();
    private final AtomicReference reference = new AtomicReference();

    public final Object get() throws ConcurrentException {
        Object object;

        while ((object = this.reference.get()) == null) {
            if (this.factory.compareAndSet((Object) null, this)) {
                this.reference.set(this.initialize());
            }
        }

        return object;
    }

    protected abstract Object initialize() throws ConcurrentException;
}
