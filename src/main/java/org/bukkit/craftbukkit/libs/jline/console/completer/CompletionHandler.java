package org.bukkit.craftbukkit.libs.jline.console.completer;

import java.io.IOException;
import java.util.List;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;

public interface CompletionHandler {

    boolean complete(ConsoleReader consolereader, List list, int i) throws IOException;
}
