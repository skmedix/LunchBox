package org.bukkit.entity.minecart;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;

public interface CommandMinecart extends Minecart, CommandSender {

    String getCommand();

    void setCommand(String s);

    void setName(String s);
}
