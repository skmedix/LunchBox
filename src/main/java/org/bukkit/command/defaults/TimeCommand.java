package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class TimeCommand extends VanillaCommand {

    private static final List TABCOMPLETE_ADD_SET = ImmutableList.of("add", "set");
    private static final List TABCOMPLETE_DAY_NIGHT = ImmutableList.of("day", "night");

    public TimeCommand() {
        super("time");
        this.description = "Changes the time on each world";
        this.usageMessage = "/time set <value>\n/time add <value>";
        this.setPermission("bukkit.command.time.add;bukkit.command.time.set");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Incorrect usage. Correct usage:\n" + this.usageMessage);
            return false;
        } else {
            int value;
            Iterator iterator;
            World world;

            if (args[0].equals("set")) {
                if (!sender.hasPermission("bukkit.command.time.set")) {
                    sender.sendMessage(ChatColor.RED + "You don\'t have permission to set the time");
                    return true;
                }

                if (args[1].equals("day")) {
                    value = 0;
                } else if (args[1].equals("night")) {
                    value = 12500;
                } else {
                    value = this.getInteger(sender, args[1], 0);
                }

                iterator = Bukkit.getWorlds().iterator();

                while (iterator.hasNext()) {
                    world = (World) iterator.next();
                    world.setTime((long) value);
                }

                Command.broadcastCommandMessage(sender, "Set time to " + value);
            } else if (args[0].equals("add")) {
                if (!sender.hasPermission("bukkit.command.time.add")) {
                    sender.sendMessage(ChatColor.RED + "You don\'t have permission to set the time");
                    return true;
                }

                value = this.getInteger(sender, args[1], 0);
                iterator = Bukkit.getWorlds().iterator();

                while (iterator.hasNext()) {
                    world = (World) iterator.next();
                    world.setFullTime(world.getFullTime() + (long) value);
                }

                Command.broadcastCommandMessage(sender, "Added " + value + " to time");
            } else {
                sender.sendMessage("Unknown method. Usage: " + this.usageMessage);
            }

            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List) (args.length == 1 ? (List) StringUtil.copyPartialMatches(args[0], TimeCommand.TABCOMPLETE_ADD_SET, new ArrayList(TimeCommand.TABCOMPLETE_ADD_SET.size())) : (args.length == 2 && args[0].equalsIgnoreCase("set") ? (List) StringUtil.copyPartialMatches(args[1], TimeCommand.TABCOMPLETE_DAY_NIGHT, new ArrayList(TimeCommand.TABCOMPLETE_DAY_NIGHT.size())) : ImmutableList.of()));
    }
}
