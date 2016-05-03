package org.bukkit.command;

public interface CommandExecutor {

    boolean onCommand(CommandSender commandsender, Command command, String s, String[] astring);
}
