package org.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class BukkitRunnable implements Runnable {

    private int taskId = -1;

    public synchronized void cancel() throws IllegalStateException {
        Bukkit.getScheduler().cancelTask(this.getTaskId());
    }

    public synchronized BukkitTask runTask(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTask(plugin, (Runnable) this));
    }

    public synchronized BukkitTask runTaskAsynchronously(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTaskAsynchronously(plugin, (Runnable) this));
    }

    public synchronized BukkitTask runTaskLater(Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTaskLater(plugin, (Runnable) this, delay));
    }

    public synchronized BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, (Runnable) this, delay));
    }

    public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTaskTimer(plugin, (Runnable) this, delay, period));
    }

    public synchronized BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, (Runnable) this, delay, period));
    }

    public synchronized int getTaskId() throws IllegalStateException {
        int id = this.taskId;

        if (id == -1) {
            throw new IllegalStateException("Not scheduled yet");
        } else {
            return id;
        }
    }

    private void checkState() {
        if (this.taskId != -1) {
            throw new IllegalStateException("Already scheduled as " + this.taskId);
        }
    }

    private BukkitTask setupId(BukkitTask task) {
        this.taskId = task.getTaskId();
        return task;
    }
}
