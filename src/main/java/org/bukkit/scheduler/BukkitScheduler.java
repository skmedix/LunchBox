package org.bukkit.scheduler;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.bukkit.plugin.Plugin;

public interface BukkitScheduler {

    int scheduleSyncDelayedTask(Plugin plugin, Runnable runnable, long i);

    /** @deprecated */
    @Deprecated
    int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable bukkitrunnable, long i);

    int scheduleSyncDelayedTask(Plugin plugin, Runnable runnable);

    /** @deprecated */
    @Deprecated
    int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable bukkitrunnable);

    int scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long i, long j);

    /** @deprecated */
    @Deprecated
    int scheduleSyncRepeatingTask(Plugin plugin, BukkitRunnable bukkitrunnable, long i, long j);

    /** @deprecated */
    @Deprecated
    int scheduleAsyncDelayedTask(Plugin plugin, Runnable runnable, long i);

    /** @deprecated */
    @Deprecated
    int scheduleAsyncDelayedTask(Plugin plugin, Runnable runnable);

    /** @deprecated */
    @Deprecated
    int scheduleAsyncRepeatingTask(Plugin plugin, Runnable runnable, long i, long j);

    Future callSyncMethod(Plugin plugin, Callable callable);

    void cancelTask(int i);

    void cancelTasks(Plugin plugin);

    void cancelAllTasks();

    boolean isCurrentlyRunning(int i);

    boolean isQueued(int i);

    List getActiveWorkers();

    List getPendingTasks();

    BukkitTask runTask(Plugin plugin, Runnable runnable) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    BukkitTask runTask(Plugin plugin, BukkitRunnable bukkitrunnable) throws IllegalArgumentException;

    BukkitTask runTaskAsynchronously(Plugin plugin, Runnable runnable) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    BukkitTask runTaskAsynchronously(Plugin plugin, BukkitRunnable bukkitrunnable) throws IllegalArgumentException;

    BukkitTask runTaskLater(Plugin plugin, Runnable runnable, long i) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    BukkitTask runTaskLater(Plugin plugin, BukkitRunnable bukkitrunnable, long i) throws IllegalArgumentException;

    BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long i) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    BukkitTask runTaskLaterAsynchronously(Plugin plugin, BukkitRunnable bukkitrunnable, long i) throws IllegalArgumentException;

    BukkitTask runTaskTimer(Plugin plugin, Runnable runnable, long i, long j) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable bukkitrunnable, long i, long j) throws IllegalArgumentException;

    BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long i, long j) throws IllegalArgumentException;

    /** @deprecated */
    @Deprecated
    BukkitTask runTaskTimerAsynchronously(Plugin plugin, BukkitRunnable bukkitrunnable, long i, long j) throws IllegalArgumentException;
}
