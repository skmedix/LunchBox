package org.spigotmc;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;

public class WatchdogThread extends Thread {

    private static WatchdogThread instance;
    private final long timeoutTime;
    private final boolean restart;
    private volatile long lastTick;
    private volatile boolean stopping;

    private WatchdogThread(long timeoutTime, boolean restart) {
        super("Spigot Watchdog Thread");
        this.timeoutTime = timeoutTime;
        this.restart = restart;
    }

    public static void doStart(int timeoutTime, boolean restart) {
        if (WatchdogThread.instance == null) {
            WatchdogThread.instance = new WatchdogThread((long) timeoutTime * 1000L, restart);
            WatchdogThread.instance.start();
        }

    }

    public static void tick() {
        WatchdogThread.instance.lastTick = System.currentTimeMillis();
    }

    public static void doStop() {
        if (WatchdogThread.instance != null) {
            WatchdogThread.instance.stopping = true;
        }

    }

    public void run() {
        while (true) {
            if (!this.stopping) {
                if (this.lastTick == 0L || System.currentTimeMillis() <= this.lastTick + this.timeoutTime) {
                    try {
                        sleep(10000L);
                    } catch (InterruptedException interruptedexception) {
                        this.interrupt();
                    }
                    continue;
                }

                Logger log = Bukkit.getServer().getLogger();

                log.log(Level.SEVERE, "The server has stopped responding!");
                log.log(Level.SEVERE, "Please report this to http://www.spigotmc.org/");
                log.log(Level.SEVERE, "Be sure to include ALL relevant console errors and Minecraft crash reports");
                log.log(Level.SEVERE, "Spigot version: " + Bukkit.getServer().getVersion());
                if (World.haveWeSilencedAPhysicsCrash) {
                    log.log(Level.SEVERE, "------------------------------");
                    log.log(Level.SEVERE, "During the run of the server, a physics stackoverflow was supressed");
                    log.log(Level.SEVERE, "near " + World.blockLocation);
                }

                log.log(Level.SEVERE, "------------------------------");
                log.log(Level.SEVERE, "Server thread dump (Look for plugins here before reporting to Spigot!):");
                dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(MinecraftServer.getServer().primaryThread.getId(), Integer.MAX_VALUE), log);
                log.log(Level.SEVERE, "------------------------------");
                log.log(Level.SEVERE, "Entire Thread Dump:");
                ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
                ThreadInfo[] athreadinfo = threads;
                int i = threads.length;

                for (int j = 0; j < i; ++j) {
                    ThreadInfo thread = athreadinfo[j];

                    dumpThread(thread, log);
                }

                log.log(Level.SEVERE, "------------------------------");
                if (this.restart) {
                    RestartCommand.restart();
                }
            }

            return;
        }
    }

    private static void dumpThread(ThreadInfo thread, Logger log) {
        log.log(Level.SEVERE, "------------------------------");
        log.log(Level.SEVERE, "Current Thread: " + thread.getThreadName());
        log.log(Level.SEVERE, "\tPID: " + thread.getThreadId() + " | Suspended: " + thread.isSuspended() + " | Native: " + thread.isInNative() + " | State: " + thread.getThreadState());
        int i;
        int j;

        if (thread.getLockedMonitors().length != 0) {
            log.log(Level.SEVERE, "\tThread is waiting on monitor(s):");
            MonitorInfo[] amonitorinfo;

            i = (amonitorinfo = thread.getLockedMonitors()).length;

            for (j = 0; j < i; ++j) {
                MonitorInfo stack = amonitorinfo[j];

                log.log(Level.SEVERE, "\t\tLocked on:" + stack.getLockedStackFrame());
            }
        }

        log.log(Level.SEVERE, "\tStack:");
        StackTraceElement[] astacktraceelement;

        i = (astacktraceelement = thread.getStackTrace()).length;

        for (j = 0; j < i; ++j) {
            StackTraceElement stacktraceelement = astacktraceelement[j];

            log.log(Level.SEVERE, "\t\t" + stacktraceelement);
        }

    }
}
