package org.bukkit.craftbukkit.libs.jline.internal;

import java.io.PrintStream;

public final class Log {

    public static final boolean TRACE = Boolean.getBoolean(Log.class.getName() + ".trace");
    public static final boolean DEBUG = Log.TRACE || Boolean.getBoolean(Log.class.getName() + ".debug");
    private static PrintStream output = System.err;

    public static PrintStream getOutput() {
        return Log.output;
    }

    public static void setOutput(PrintStream out) {
        Log.output = (PrintStream) Preconditions.checkNotNull(out);
    }

    @TestAccessible
    static void render(PrintStream out, Object message) {
        if (message.getClass().isArray()) {
            Object[] array = (Object[]) ((Object[]) message);

            out.print("[");

            for (int i = 0; i < array.length; ++i) {
                out.print(array[i]);
                if (i + 1 < array.length) {
                    out.print(",");
                }
            }

            out.print("]");
        } else {
            out.print(message);
        }

    }

    @TestAccessible
    static void log(Log.Level level, Object... messages) {
        PrintStream printstream = Log.output;

        synchronized (Log.output) {
            Log.output.format("[%s] ", new Object[] { level});

            for (int i = 0; i < messages.length; ++i) {
                if (i + 1 == messages.length && messages[i] instanceof Throwable) {
                    Log.output.println();
                    ((Throwable) messages[i]).printStackTrace(Log.output);
                } else {
                    render(Log.output, messages[i]);
                }
            }

            Log.output.println();
            Log.output.flush();
        }
    }

    public static void trace(Object... messages) {
        if (Log.TRACE) {
            log(Log.Level.TRACE, messages);
        }

    }

    public static void debug(Object... messages) {
        if (Log.TRACE || Log.DEBUG) {
            log(Log.Level.DEBUG, messages);
        }

    }

    public static void info(Object... messages) {
        log(Log.Level.INFO, messages);
    }

    public static void warn(Object... messages) {
        log(Log.Level.WARN, messages);
    }

    public static void error(Object... messages) {
        log(Log.Level.ERROR, messages);
    }

    public static enum Level {

        TRACE, DEBUG, INFO, WARN, ERROR;
    }
}
