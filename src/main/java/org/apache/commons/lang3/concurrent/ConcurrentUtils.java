package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConcurrentUtils {

    public static ConcurrentException extractCause(ExecutionException executionexception) {
        if (executionexception != null && executionexception.getCause() != null) {
            throwCause(executionexception);
            return new ConcurrentException(executionexception.getMessage(), executionexception.getCause());
        } else {
            return null;
        }
    }

    public static ConcurrentRuntimeException extractCauseUnchecked(ExecutionException executionexception) {
        if (executionexception != null && executionexception.getCause() != null) {
            throwCause(executionexception);
            return new ConcurrentRuntimeException(executionexception.getMessage(), executionexception.getCause());
        } else {
            return null;
        }
    }

    public static void handleCause(ExecutionException executionexception) throws ConcurrentException {
        ConcurrentException concurrentexception = extractCause(executionexception);

        if (concurrentexception != null) {
            throw concurrentexception;
        }
    }

    public static void handleCauseUnchecked(ExecutionException executionexception) {
        ConcurrentRuntimeException concurrentruntimeexception = extractCauseUnchecked(executionexception);

        if (concurrentruntimeexception != null) {
            throw concurrentruntimeexception;
        }
    }

    static Throwable checkedException(Throwable throwable) {
        if (throwable != null && !(throwable instanceof RuntimeException) && !(throwable instanceof Error)) {
            return throwable;
        } else {
            throw new IllegalArgumentException("Not a checked exception: " + throwable);
        }
    }

    private static void throwCause(ExecutionException executionexception) {
        if (executionexception.getCause() instanceof RuntimeException) {
            throw (RuntimeException) executionexception.getCause();
        } else if (executionexception.getCause() instanceof Error) {
            throw (Error) executionexception.getCause();
        }
    }

    public static Object initialize(ConcurrentInitializer concurrentinitializer) throws ConcurrentException {
        return concurrentinitializer != null ? concurrentinitializer.get() : null;
    }

    public static Object initializeUnchecked(ConcurrentInitializer concurrentinitializer) {
        try {
            return initialize(concurrentinitializer);
        } catch (ConcurrentException concurrentexception) {
            throw new ConcurrentRuntimeException(concurrentexception.getCause());
        }
    }

    public static Object putIfAbsent(ConcurrentMap concurrentmap, Object object, Object object1) {
        if (concurrentmap == null) {
            return null;
        } else {
            Object object2 = concurrentmap.putIfAbsent(object, object1);

            return object2 != null ? object2 : object1;
        }
    }

    public static Object createIfAbsent(ConcurrentMap concurrentmap, Object object, ConcurrentInitializer concurrentinitializer) throws ConcurrentException {
        if (concurrentmap != null && concurrentinitializer != null) {
            Object object1 = concurrentmap.get(object);

            return object1 == null ? putIfAbsent(concurrentmap, object, concurrentinitializer.get()) : object1;
        } else {
            return null;
        }
    }

    public static Object createIfAbsentUnchecked(ConcurrentMap concurrentmap, Object object, ConcurrentInitializer concurrentinitializer) {
        try {
            return createIfAbsent(concurrentmap, object, concurrentinitializer);
        } catch (ConcurrentException concurrentexception) {
            throw new ConcurrentRuntimeException(concurrentexception.getCause());
        }
    }

    public static Future constantFuture(Object object) {
        return new ConcurrentUtils.ConstantFuture(object);
    }

    static final class ConstantFuture implements Future {

        private final Object value;

        ConstantFuture(Object object) {
            this.value = object;
        }

        public boolean isDone() {
            return true;
        }

        public Object get() {
            return this.value;
        }

        public Object get(long i, TimeUnit timeunit) {
            return this.value;
        }

        public boolean isCancelled() {
            return false;
        }

        public boolean cancel(boolean flag) {
            return false;
        }
    }
}
