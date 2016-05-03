package org.bukkit.command;

import java.util.List;

public interface TabCompleter {

    List onTabComplete(CommandSender commandsender, Command command, String s, String[] astring);
}
