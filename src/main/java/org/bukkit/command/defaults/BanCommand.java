package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** @deprecated */
@Deprecated
public class BanCommand extends VanillaCommand {

    public BanCommand() {
        super("ban");
        this.description = "Prevents the specified player from using this server";
        this.usageMessage = "/ban <player> [reason ...]";
        this.setPermission("bukkit.command.ban.player");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else {
            String reason = args.length > 0 ? StringUtils.join(args, ' ', 1, args.length) : null;

            Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], reason, (Date) null, sender.getName());
            Player player = Bukkit.getPlayer(args[0]);

            if (player != null) {
                player.kickPlayer("Banned by admin.");
            }

            Command.broadcastCommandMessage(sender, "Banned player " + args[0]);
            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List) (args.length >= 1 ? super.tabComplete(sender, alias, args) : ImmutableList.of());
    }
}
