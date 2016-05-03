package org.bukkit.craftbukkit.v1_8_R3.command;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import org.bukkit.craftbukkit.libs.jline.console.completer.Completer;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.util.Waitable;

public class ConsoleCommandCompleter implements Completer {

    private final CraftServer server;

    public ConsoleCommandCompleter(CraftServer server) {
        this.server = server;
    }

    public int complete(final String buffer, int cursor, List candidates) {
        Waitable waitable = new Waitable() {
            protected List evaluate() {
                return ConsoleCommandCompleter.this.server.getCommandMap().tabComplete(ConsoleCommandCompleter.this.server.getConsoleSender(), buffer);
            }
        };

        this.server.getServer().processQueue.add(waitable);

        try {
            List e = (List) waitable.get();

            if (e == null) {
                return cursor;
            }

            candidates.addAll(e);
            int lastSpace = buffer.lastIndexOf(32);

            if (lastSpace == -1) {
                return cursor - buffer.length();
            }

            return cursor - (buffer.length() - lastSpace - 1);
        } catch (ExecutionException executionexception) {
            this.server.getLogger().log(Level.WARNING, "Unhandled exception when tab completing", executionexception);
        } catch (InterruptedException interruptedexception) {
            Thread.currentThread().interrupt();
        }

        return cursor;
    }
}
