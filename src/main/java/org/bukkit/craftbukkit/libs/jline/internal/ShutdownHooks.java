package org.bukkit.craftbukkit.libs.jline.internal;

import java.util.ArrayList;
import java.util.List;

public class ShutdownHooks {

    public static final String JLINE_SHUTDOWNHOOK = "org.bukkit.craftbukkit.libs.jline.shutdownhook";
    private static final boolean enabled = Configuration.getBoolean("org.bukkit.craftbukkit.libs.jline.shutdownhook", true);
    private static final List tasks = new ArrayList();
    private static Thread hook;

    public static synchronized ShutdownHooks.Task add(ShutdownHooks.Task task) {
        Preconditions.checkNotNull(task);
        if (!ShutdownHooks.enabled) {
            Log.debug(new Object[] { "Shutdown-hook is disabled; not installing: ", task});
            return task;
        } else {
            if (ShutdownHooks.hook == null) {
                ShutdownHooks.hook = addHook(new Thread("JLine Shutdown Hook") {
                    public void run() {
                        ShutdownHooks.runTasks();
                    }
                });
            }

            Log.debug(new Object[] { "Adding shutdown-hook task: ", task});
            ShutdownHooks.tasks.add(task);
            return task;
        }
    }

    private static synchronized void runTasks() {
        Log.debug(new Object[] { "Running all shutdown-hook tasks"});
        ShutdownHooks.Task[] arr$ = (ShutdownHooks.Task[]) ShutdownHooks.tasks.toArray(new ShutdownHooks.Task[ShutdownHooks.tasks.size()]);
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            ShutdownHooks.Task task = arr$[i$];

            Log.debug(new Object[] { "Running task: ", task});

            try {
                task.run();
            } catch (Throwable throwable) {
                Log.warn(new Object[] { "Task failed", throwable});
            }
        }

        ShutdownHooks.tasks.clear();
    }

    private static Thread addHook(Thread thread) {
        Log.debug(new Object[] { "Registering shutdown-hook: ", thread});

        try {
            Runtime.getRuntime().addShutdownHook(thread);
        } catch (AbstractMethodError abstractmethoderror) {
            Log.debug(new Object[] { "Failed to register shutdown-hook", abstractmethoderror});
        }

        return thread;
    }

    public static synchronized void remove(ShutdownHooks.Task task) {
        Preconditions.checkNotNull(task);
        if (ShutdownHooks.enabled && ShutdownHooks.hook != null) {
            ShutdownHooks.tasks.remove(task);
            if (ShutdownHooks.tasks.isEmpty()) {
                removeHook(ShutdownHooks.hook);
                ShutdownHooks.hook = null;
            }

        }
    }

    private static void removeHook(Thread thread) {
        Log.debug(new Object[] { "Removing shutdown-hook: ", thread});

        try {
            Runtime.getRuntime().removeShutdownHook(thread);
        } catch (AbstractMethodError abstractmethoderror) {
            Log.debug(new Object[] { "Failed to remove shutdown-hook", abstractmethoderror});
        } catch (IllegalStateException illegalstateexception) {
            ;
        }

    }

    public interface Task {

        void run() throws Exception;
    }
}
