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
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

/** @deprecated */
@Deprecated
public class DeopCommand extends VanillaCommand {

    public DeopCommand() {
        super("deop");
        this.description = "Takes the specified player\'s operator status";
        this.usageMessage = "/deop <player>";
        this.setPermission("bukkit.command.op.take");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length == 1 && args[0].length() != 0) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

            player.setOp(false);
            if (player instanceof Player) {
                ((Player) player).sendMessage(ChatColor.YELLOW + "You are no longer op!");
            }

            Command.broadcastCommandMessage(sender, "De-opped " + args[0]);
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
        if (args.length == 1) {
            ArrayList completions = new ArrayList();
            Iterator iterator = Bukkit.getOperators().iterator();

            while (iterator.hasNext()) {
                OfflinePlayer player = (OfflinePlayer) iterator.next();
                String playerName = player.getName();

                if (StringUtil.startsWithIgnoreCase(playerName, args[0])) {
                    completions.add(playerName);
                }
            }

            return completions;
        } else {
            return ImmutableList.of();
        }
    }
}
