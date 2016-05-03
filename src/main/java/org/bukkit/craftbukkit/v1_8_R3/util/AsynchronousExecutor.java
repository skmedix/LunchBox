package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import org.apache.commons.lang.Validate;

public final class AsynchronousExecutor {

    static final AtomicIntegerFieldUpdater STATE_FIELD = AtomicIntegerFieldUpdater.newUpdater(AsynchronousExecutor.Task.class, "state");
    final AsynchronousExecutor.CallBackProvider provider;
    final Queue finished = new ConcurrentLinkedQueue();
    final Map tasks = new HashMap();
    final ThreadPoolExecutor pool;

    private static boolean set(AsynchronousExecutor.Task $this, int expected, int value) {
        return AsynchronousExecutor.STATE_FIELD.compareAndSet($this, expected, value);
    }

    public AsynchronousExecutor(AsynchronousExecutor.CallBackProvider provider, int coreSize) {
        Validate.notNull(provider, "Provider cannot be null");
        this.provider = provider;
        this.pool = new ThreadPoolExecutor(coreSize, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), provider);
    }

    public void add(Object parameter, Object callback) {
        AsynchronousExecutor.Task task = (AsynchronousExecutor.Task) this.tasks.get(parameter);

        if (task == null) {
            this.tasks.put(parameter, task = new AsynchronousExecutor.Task(parameter));
            this.pool.execute(task);
        }

        task.callbacks.add(callback);
    }

    public boolean drop(Object parameter, Object callback) throws IllegalStateException {
        AsynchronousExecutor.Task task = (AsynchronousExecutor.Task) this.tasks.get(parameter);

        if (task == null) {
            return true;
        } else if (!task.callbacks.remove(callback)) {
            throw new IllegalStateException("Unknown " + callback + " for " + parameter);
        } else {
            return task.callbacks.isEmpty() ? task.drop() : false;
        }
    }

    public Object get(Object parameter) throws Throwable, IllegalStateException {
        AsynchronousExecutor.Task task = (AsynchronousExecutor.Task) this.tasks.get(parameter);

        if (task == null) {
            throw new IllegalStateException("Unknown " + parameter);
        } else {
            return task.get();
        }
    }

    public Object getSkipQueue(Object parameter) throws Throwable {
        return this.skipQueue(parameter);
    }

    public Object getSkipQueue(Object parameter, Object callback) throws Throwable {
        Object object = this.skipQueue(parameter);

        this.provider.callStage3(parameter, object, callback);
        return object;
    }

    public Object getSkipQueue(Object parameter, Object... callbacks) throws Throwable {
        AsynchronousExecutor.CallBackProvider provider = this.provider;
        Object object = this.skipQueue(parameter);
        Object[] aobject = callbacks;
        int i = callbacks.length;

        for (int j = 0; j < i; ++j) {
            Object callback = aobject[j];

            provider.callStage3(parameter, object, callback);
        }

        return object;
    }

    public Object getSkipQueue(Object parameter, Iterable callbacks) throws Throwable {
        AsynchronousExecutor.CallBackProvider provider = this.provider;
        Object object = this.skipQueue(parameter);
        Iterator iterator = callbacks.iterator();

        while (iterator.hasNext()) {
            Object callback = (Object) iterator.next();

            provider.callStage3(parameter, object, callback);
        }

        return object;
    }

    private Object skipQueue(Object parameter) throws Throwable {
        AsynchronousExecutor.Task task = (AsynchronousExecutor.Task) this.tasks.get(parameter);

        if (task != null) {
            return task.get();
        } else {
            Object object = this.provider.callStage1(parameter);

            this.provider.callStage2(parameter, object);
            return object;
        }
    }

    public void finishActive() throws Throwable {
        Queue finished = this.finished;

        while (!finished.isEmpty()) {
            ((AsynchronousExecutor.Task) finished.poll()).finish();
        }

    }

    public void setActiveThreads(int coreSize) {
        this.pool.setCorePoolSize(coreSize);
    }

    public interface CallBackProvider extends ThreadFactory {

        Object callStage1(Object object) throws Throwable;

        void callStage2(Object object, Object object1) throws Throwable;

        void callStage3(Object object, Object object1, Object object2) throws Throwable;
    }

    class Task implements Runnable {

        static final int PENDING = 0;
        static final int STAGE_1_ASYNC = 1;
        static final int STAGE_1_SYNC = 2;
        static final int STAGE_1_COMPLETE = 3;
        static final int FINISHED = 4;
        volatile int state = 0;
        final Object parameter;
        Object object;
        final List callbacks = new LinkedList();
        Throwable t = null;

        Task(Object parameter) {
            this.parameter = parameter;
        }

        public void run() {
            if (this.initAsync()) {
                AsynchronousExecutor.this.finished.add(this);
            }

        }

        boolean initAsync() {
            if (AsynchronousExecutor.set(this, 0, 1)) {
                boolean ret = true;

                try {
                    this.init();
                } finally {
                    if (!AsynchronousExecutor.set(this, 1, 3)) {
                        synchronized (this) {
                            if (this.state != 2) {
                                this.notifyAll();
                            }

                            this.state = 3;
                        }

                        ret = false;
                    }

                }

                return ret;
            } else {
                return false;
            }
        }

        void initSync() {
            if (AsynchronousExecutor.set(this, 0, 3)) {
                this.init();
            } else if (AsynchronousExecutor.set(this, 1, 2)) {
                synchronized (this) {
                    if (AsynchronousExecutor.set(this, 2, 0)) {
                        while (this.state != 3) {
                            try {
                                this.wait();
                            } catch (InterruptedException interruptedexception) {
                                Thread.currentThread().interrupt();
                                throw new RuntimeException("Unable to handle interruption on " + this.parameter, interruptedexception);
                            }
                        }
                    }
                }
            }

        }

        void init() {
            try {
                this.object = AsynchronousExecutor.this.provider.callStage1(this.parameter);
            } catch (Throwable throwable) {
                this.t = throwable;
            }

        }

        Object get() throws Throwable {
            this.initSync();
            if (this.callbacks.isEmpty()) {
                this.callbacks.add(this);
            }

            this.finish();
            return this.object;
        }

        void finish() throws Throwable {
            switch (this.state) {
            case 0:
            case 1:
            case 2:
            default:
                throw new IllegalStateException("Attempting to finish unprepared(" + this.state + ") task(" + this.parameter + ")");

            case 3:
                try {
                    if (this.t != null) {
                        throw this.t;
                    }

                    if (!this.callbacks.isEmpty()) {
                        AsynchronousExecutor.CallBackProvider provider = AsynchronousExecutor.this.provider;
                        Object parameter = this.parameter;
                        Object object = this.object;

                        provider.callStage2(parameter, object);
                        Iterator iterator = this.callbacks.iterator();

                        while (iterator.hasNext()) {
                            Object callback = (Object) iterator.next();

                            if (callback != this) {
                                provider.callStage3(parameter, object, callback);
                            }
                        }

                        return;
                    }
                } finally {
                    AsynchronousExecutor.this.tasks.remove(this.parameter);
                    this.state = 4;
                }

                return;

            case 4:
            }
        }

        boolean drop() {
            if (AsynchronousExecutor.set(this, 0, 4)) {
                AsynchronousExecutor.this.tasks.remove(this.parameter);
                return true;
            } else {
                return false;
            }
        }
    }
}
