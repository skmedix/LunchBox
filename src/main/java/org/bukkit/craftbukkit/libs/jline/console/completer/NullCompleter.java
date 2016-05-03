package org.bukkit.craftbukkit.libs.jline.console.completer;

import java.util.List;

public final class NullCompleter implements Completer {

    public static final NullCompleter INSTANCE = new NullCompleter();

    public int complete(String buffer, int cursor, List candidates) {
        return -1;
    }
}
