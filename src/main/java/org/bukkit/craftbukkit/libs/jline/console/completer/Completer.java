package org.bukkit.craftbukkit.libs.jline.console.completer;

import java.util.List;

public interface Completer {

    int complete(String s, int i, List list);
}
