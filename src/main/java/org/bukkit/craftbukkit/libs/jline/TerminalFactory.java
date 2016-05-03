package org.bukkit.craftbukkit.libs.jline;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.craftbukkit.libs.jline.internal.Configuration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;

public class TerminalFactory {

    public static final String JLINE_TERMINAL = "org.bukkit.craftbukkit.libs.jline.terminal";
    public static final String AUTO = "auto";
    public static final String UNIX = "unix";
    public static final String WIN = "win";
    public static final String WINDOWS = "windows";
    public static final String NONE = "none";
    public static final String OFF = "off";
    public static final String FALSE = "false";
    private static Terminal term = null;
    private static final Map FLAVORS = new HashMap();

    public static synchronized Terminal create() {
        if (Log.TRACE) {
            Log.trace(new Object[] { new Throwable("CREATE MARKER")});
        }

        String type = Configuration.getString("org.bukkit.craftbukkit.libs.jline.terminal", "auto");

        if ("dumb".equals(System.getenv("TERM"))) {
            type = "none";
            Log.debug(new Object[] { "$TERM=dumb; setting type=", type});
        }

        Log.debug(new Object[] { "Creating terminal; type=", type});

        Object t;

        try {
            String e = type.toLowerCase();

            if (e.equals("unix")) {
                t = getFlavor(TerminalFactory.Flavor.UNIX);
            } else if (e.equals("win") | e.equals("windows")) {
                t = getFlavor(TerminalFactory.Flavor.WINDOWS);
            } else if (!e.equals("none") && !e.equals("off") && !e.equals("false")) {
                if (e.equals("auto")) {
                    String e1 = Configuration.getOsName();
                    TerminalFactory.Flavor flavor = TerminalFactory.Flavor.UNIX;

                    if (e1.contains("windows")) {
                        flavor = TerminalFactory.Flavor.WINDOWS;
                    }

                    t = getFlavor(flavor);
                } else {
                    try {
                        t = (Terminal) Thread.currentThread().getContextClassLoader().loadClass(type).newInstance();
                    } catch (Exception exception) {
                        throw new IllegalArgumentException(MessageFormat.format("Invalid terminal type: {0}", new Object[] { type}), exception);
                    }
                }
            } else {
                t = new UnsupportedTerminal();
            }
        } catch (Exception exception1) {
            Log.error(new Object[] { "Failed to construct terminal; falling back to unsupported", exception1});
            t = new UnsupportedTerminal();
        }

        Log.debug(new Object[] { "Created Terminal: ", t});

        try {
            ((Terminal) t).init();
            return (Terminal) t;
        } catch (Throwable throwable) {
            Log.error(new Object[] { "Terminal initialization failed; falling back to unsupported", throwable});
            return new UnsupportedTerminal();
        }
    }

    public static synchronized void reset() {
        TerminalFactory.term = null;
    }

    public static synchronized void resetIf(Terminal t) {
        if (t == TerminalFactory.term) {
            reset();
        }

    }

    public static synchronized void configure(String type) {
        Preconditions.checkNotNull(type);
        System.setProperty("org.bukkit.craftbukkit.libs.jline.terminal", type);
    }

    public static synchronized void configure(TerminalFactory.Type type) {
        Preconditions.checkNotNull(type);
        configure(type.name().toLowerCase());
    }

    public static synchronized Terminal get() {
        if (TerminalFactory.term == null) {
            TerminalFactory.term = create();
        }

        return TerminalFactory.term;
    }

    public static Terminal getFlavor(TerminalFactory.Flavor flavor) throws Exception {
        Class type = (Class) TerminalFactory.FLAVORS.get(flavor);

        if (type != null) {
            return (Terminal) type.newInstance();
        } else {
            throw new InternalError();
        }
    }

    public static void registerFlavor(TerminalFactory.Flavor flavor, Class type) {
        TerminalFactory.FLAVORS.put(flavor, type);
    }

    static {
        registerFlavor(TerminalFactory.Flavor.WINDOWS, AnsiWindowsTerminal.class);
        registerFlavor(TerminalFactory.Flavor.UNIX, UnixTerminal.class);
    }

    public static enum Flavor {

        WINDOWS, UNIX;
    }

    public static enum Type {

        AUTO, WINDOWS, UNIX, NONE;
    }
}
