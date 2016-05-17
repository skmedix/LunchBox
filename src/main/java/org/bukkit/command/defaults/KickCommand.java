package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** @deprecated */
@Deprecated
public class KickCommand extends VanillaCommand {

    public KickCommand() {
        super("kick");
        this.description = "Removes the specified player from the server";
        this.usageMessage = "/kick <player> [reason ...]";
        this.setPermission("bukkit.command.kick");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length >= 1 && args[0].length() != 0) {
            Player player = Bukkit.getPlayerExact(args[0]);

            if (player != null) {
                String reason = "Kicked by an operator.";

                if (args.length > 1) {
                    reason = this.createString(args, 1);
                }

                player.kickPlayer(reason);
                Command.broadcastCommandMessage(sender, "Kicked player " + player.getName() + ". With reason:\n" + reason);
            } else {
                sender.sendMessage(args[0] + " not found.");
            }

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List) (args.length >= 1 ? super.tabComplete(sender, alias, args) : ImmutableList.of());
    }
}
