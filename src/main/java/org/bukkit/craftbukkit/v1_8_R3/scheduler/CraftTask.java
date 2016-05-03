package org.bukkit.craftbukkit.v1_8_R3.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.CustomTimingsHandler;

public class CraftTask implements BukkitTask, Runnable {

    private volatile CraftTask next;
    private volatile long period;
    private long nextRun;
    private final Runnable task;
    private final Plugin plugin;
    private final int id;
    final CustomTimingsHandler timings;
    public String timingName;

    CraftTask() {
        this((Plugin) null, (Runnable) null, -1, -1L);
    }

    CraftTask(Runnable task) {
        this((Plugin) null, task, -1, -1L);
    }

    CraftTask(String timingName) {
        this(timingName, (Plugin) null, (Runnable) null, -1, -1L);
    }

    CraftTask(String timingName, Runnable task) {
        this(timingName, (Plugin) null, task, -1, -1L);
    }

    CraftTask(String timingName, Plugin plugin, Runnable task, int id, long period) {
        this.next = null;
        this.timingName = null;
        this.plugin = plugin;
        this.task = task;
        this.id = id;
        this.period = period;
        this.timingName = timingName == null && task == null ? "Unknown" : timingName;
        this.timings = this.isSync() ? SpigotTimings.getPluginTaskTimings(this, period) : null;
    }

    CraftTask(Plugin plugin, Runnable task, int id, long period) {
        this((String) null, plugin, task, id, period);
    }

    public final int getTaskId() {
        return this.id;
    }

    public final Plugin getOwner() {
        return this.plugin;
    }

    public boolean isSync() {
        return true;
    }

    public void run() {
        this.task.run();
    }

    long getPeriod() {
        return this.period;
    }

    void setPeriod(long period) {
        this.period = period;
    }

    long getNextRun() {
        return this.nextRun;
    }

    void setNextRun(long nextRun) {
        this.nextRun = nextRun;
    }

    CraftTask getNext() {
        return this.next;
    }

    void setNext(CraftTask next) {
        this.next = next;
    }

    Class getTaskClass() {
        return this.task.getClass();
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.id);
    }

    boolean cancel0() {
        this.setPeriod(-2L);
        return true;
    }

    public String getTaskName() {
        return this.timingName != null ? this.timingName : this.task.getClass().getName();
    }
}
