package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class WhitelistCommand extends VanillaCommand {

    private static final List WHITELIST_SUBCOMMANDS = ImmutableList.of("add", "remove", "on", "off", "list", "reload");

    public WhitelistCommand() {
        super("whitelist");
        this.description = "Manages the list of players allowed to use this server";
        this.usageMessage = "/whitelist (add|remove) <player>\n/whitelist (on|off|list|reload)";
        this.setPermission("bukkit.command.whitelist.reload;bukkit.command.whitelist.enable;bukkit.command.whitelist.disable;bukkit.command.whitelist.list;bukkit.command.whitelist.add;bukkit.command.whitelist.remove");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (this.badPerm(sender, "reload")) {
                        return true;
                    }

                    Bukkit.reloadWhitelist();
                    Command.broadcastCommandMessage(sender, "Reloaded white-list from file");
                    return true;
                }

                if (args[0].equalsIgnoreCase("on")) {
                    if (this.badPerm(sender, "enable")) {
                        return true;
                    }

                    Bukkit.setWhitelist(true);
                    Command.broadcastCommandMessage(sender, "Turned on white-listing");
                    return true;
                }

                if (args[0].equalsIgnoreCase("off")) {
                    if (this.badPerm(sender, "disable")) {
                        return true;
                    }

                    Bukkit.setWhitelist(false);
                    Command.broadcastCommandMessage(sender, "Turned off white-listing");
                    return true;
                }

                if (args[0].equalsIgnoreCase("list")) {
                    if (this.badPerm(sender, "list")) {
                        return true;
                    }

                    StringBuilder result = new StringBuilder();

                    OfflinePlayer player;

                    for (Iterator iterator = Bukkit.getWhitelistedPlayers().iterator(); iterator.hasNext(); result.append(player.getName())) {
                        player = (OfflinePlayer) iterator.next();
                        if (result.length() > 0) {
                            result.append(", ");
                        }
                    }

                    sender.sendMessage("White-listed players: " + result.toString());
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add")) {
                    if (this.badPerm(sender, "add")) {
                        return true;
                    }

                    Bukkit.getOfflinePlayer(args[1]).setWhitelisted(true);
                    Command.broadcastCommandMessage(sender, "Added " + args[1] + " to white-list");
                    return true;
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    if (this.badPerm(sender, "remove")) {
                        return true;
                    }

                    Bukkit.getOfflinePlayer(args[1]).setWhitelisted(false);
                    Command.broadcastCommandMessage(sender, "Removed " + args[1] + " from white-list");
                    return true;
                }
            }

            sender.sendMessage(ChatColor.RED + "Correct command usage:\n" + this.usageMessage);
            return false;
        }
    }

    private boolean badPerm(CommandSender sender, String perm) {
        if (!sender.hasPermission("bukkit.command.whitelist." + perm)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to perform this action.");
            return true;
        } else {
            return false;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        if (args.length == 1) {
            return (List) StringUtil.copyPartialMatches(args[0], WhitelistCommand.WHITELIST_SUBCOMMANDS, new ArrayList(WhitelistCommand.WHITELIST_SUBCOMMANDS.size()));
        } else {
            if (args.length == 2) {
                ArrayList completions;
                Iterator iterator;
                OfflinePlayer player;
                String name;

                if (args[0].equalsIgnoreCase("add")) {
                    completions = new ArrayList();
                    iterator = Bukkit.getOnlinePlayers().iterator();

                    while (iterator.hasNext()) {
                        player = (OfflinePlayer) iterator.next();
                        name = player.getName();
                        if (StringUtil.startsWithIgnoreCase(name, args[1]) && !player.isWhitelisted()) {
                            completions.add(name);
                        }
                    }

                    return completions;
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    completions = new ArrayList();
                    iterator = Bukkit.getWhitelistedPlayers().iterator();

                    while (iterator.hasNext()) {
                        player = (OfflinePlayer) iterator.next();
                        name = player.getName();
                        if (StringUtil.startsWithIgnoreCase(name, args[1])) {
                            completions.add(name);
                        }
                    }

                    return completions;
                }
            }

            return ImmutableList.of();
        }
    }
}