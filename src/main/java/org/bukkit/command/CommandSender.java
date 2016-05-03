package org.bukkit.command;

import org.bukkit.Server;
import org.bukkit.permissions.Permissible;

public interface CommandSender extends Permissible {

    void sendMessage(String s);

    void sendMessage(String[] astring);

    Server getServer();

    String getName();
}
