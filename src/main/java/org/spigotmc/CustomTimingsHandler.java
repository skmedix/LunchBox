package org.spigotmc;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.defaults.TimingsCommand;

public class CustomTimingsHandler {

    private static Queue HANDLERS = new ConcurrentLinkedQueue();
    private final String name;
    private final CustomTimingsHandler parent;
    private long count;
    private long start;
    private long timingDepth;
    private long totalTime;
    private long curTickTotal;
    private long violations;

    public CustomTimingsHandler(String name) {
        this(name, (CustomTimingsHandler) null);
    }

    public CustomTimingsHandler(String name, CustomTimingsHandler parent) {
        this.count = 0L;
        this.start = 0L;
        this.timingDepth = 0L;
        this.totalTime = 0L;
        this.curTickTotal = 0L;
        this.violations = 0L;
        this.name = name;
        this.parent = parent;
        CustomTimingsHandler.HANDLERS.add(this);
    }

    public static void printTimings(PrintStream printStream) {
        printStream.println("Minecraft");
        Iterator livingEntities = CustomTimingsHandler.HANDLERS.iterator();

        while (livingEntities.hasNext()) {
            CustomTimingsHandler entities = (CustomTimingsHandler) livingEntities.next();
            long time = entities.totalTime;
            long count = entities.count;

            if (count != 0L) {
                long avg = time / count;

                printStream.println("    " + entities.name + " Time: " + time + " Count: " + count + " Avg: " + avg + " Violations: " + entities.violations);
            }
        }

        printStream.println("# Version " + Bukkit.getVersion());
        int entities1 = 0;
        int livingEntities1 = 0;

        World world;

        for (Iterator iterator = Bukkit.getWorlds().iterator(); iterator.hasNext(); livingEntities1 += world.getLivingEntities().size()) {
            world = (World) iterator.next();
            entities1 += world.getEntities().size();
        }

        printStream.println("# Entities " + entities1);
        printStream.println("# LivingEntities " + livingEntities1);
    }

    public static void reload() {
        if (Bukkit.getPluginManager().useTimings()) {
            Iterator iterator = CustomTimingsHandler.HANDLERS.iterator();

            while (iterator.hasNext()) {
                CustomTimingsHandler timings = (CustomTimingsHandler) iterator.next();

                timings.reset();
            }
        }

        TimingsCommand.timingStart = System.nanoTime();
    }

    public static void tick() {
        CustomTimingsHandler timings;

        if (Bukkit.getPluginManager().useTimings()) {
            for (Iterator iterator = CustomTimingsHandler.HANDLERS.iterator(); iterator.hasNext(); timings.timingDepth = 0L) {
                timings = (CustomTimingsHandler) iterator.next();
                if (timings.curTickTotal > 50000000L) {
                    timings.violations = (long) ((double) timings.violations + Math.ceil((double) (timings.curTickTotal / 50000000L)));
                }

                timings.curTickTotal = 0L;
            }
        }

    }

    public void startTiming() {
        if (Bukkit.getPluginManager().useTimings() && ++this.timingDepth == 1L) {
            this.start = System.nanoTime();
            if (this.parent != null && ++this.parent.timingDepth == 1L) {
                this.parent.start = this.start;
            }
        }

    }

    public void stopTiming() {
        if (Bukkit.getPluginManager().useTimings()) {
            if (--this.timingDepth != 0L || this.start == 0L) {
                return;
            }

            long diff = System.nanoTime() - this.start;

            this.totalTime += diff;
            this.curTickTotal += diff;
            ++this.count;
            this.start = 0L;
            if (this.parent != null) {
                this.parent.stopTiming();
            }
        }

    }

    public void reset() {
        this.count = 0L;
        this.violations = 0L;
        this.curTickTotal = 0L;
        this.totalTime = 0L;
        this.start = 0L;
        this.timingDepth = 0L;
    }
}
