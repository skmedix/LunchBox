package org.bukkit.command;

import java.util.List;

public interface CommandMap {

    void registerAll(String s, List list);

    boolean register(String s, String s1, Command command);

    boolean register(String s, Command command);

    boolean dispatch(CommandSender commandsender, String s) throws CommandException;

    void clearCommands();

    Command getCommand(String s);

    List tabComplete(CommandSender commandsender, String s) throws IllegalArgumentException;
}
