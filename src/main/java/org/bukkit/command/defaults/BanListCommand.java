package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class BanListCommand extends VanillaCommand {

    private static final List BANLIST_TYPES = ImmutableList.of("ips", "players");

    public BanListCommand() {
        super("banlist");
        this.description = "View all players banned from this server";
        this.usageMessage = "/banlist [ips|players]";
        this.setPermission("bukkit.command.ban.list");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else {
            BanList.Type banType = BanList.Type.NAME;

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("ips")) {
                    banType = BanList.Type.IP;
                } else if (!args[0].equalsIgnoreCase("players")) {
                    sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
                    return false;
                }
            }

            StringBuilder message = new StringBuilder();
            BanEntry[] banlist = (BanEntry[]) Bukkit.getBanList(banType).getBanEntries().toArray(new BanEntry[0]);

            for (int x = 0; x < banlist.length; ++x) {
                if (x != 0) {
                    if (x == banlist.length - 1) {
                        message.append(" and ");
                    } else {
                        message.append(", ");
                    }
                }

                message.append(banlist[x].getTarget());
            }

            sender.sendMessage("There are " + banlist.length + " total banned players:");
            sender.sendMessage(message.toString());
            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List) (args.length == 1 ? (List) StringUtil.copyPartialMatches(args[0], BanListCommand.BANLIST_TYPES, new ArrayList(BanListCommand.BANLIST_TYPES.size())) : ImmutableList.of());
    }
}
