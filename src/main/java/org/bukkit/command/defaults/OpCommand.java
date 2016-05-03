package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
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
public class OpCommand extends VanillaCommand {

    public OpCommand() {
        super("op");
        this.description = "Gives the specified player operator status";
        this.usageMessage = "/op <player>";
        this.setPermission("bukkit.command.op.give");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length == 1 && args[0].length() != 0) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

            player.setOp(true);
            Command.broadcastCommandMessage(sender, "Opped " + args[0]);
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
            if (!(sender instanceof Player)) {
                return ImmutableList.of();
            } else {
                String lastWord = args[0];

                if (lastWord.length() == 0) {
                    return ImmutableList.of();
                } else {
                    Player senderPlayer = (Player) sender;
                    ArrayList matchedPlayers = new ArrayList();
                    Iterator iterator = sender.getServer().getOnlinePlayers().iterator();

                    while (iterator.hasNext()) {
                        Player player = (Player) iterator.next();
                        String name = player.getName();

                        if (senderPlayer.canSee(player) && !player.isOp() && StringUtil.startsWithIgnoreCase(name, lastWord)) {
                            matchedPlayers.add(name);
                        }
                    }

                    Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
                    return matchedPlayers;
                }
            }
        } else {
            return ImmutableList.of();
        }
    }
}
