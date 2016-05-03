package org.bukkit.command.defaults;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** @deprecated */
@Deprecated
public class TellCommand extends VanillaCommand {

    public TellCommand() {
        super("tell");
        this.description = "Sends a private message to the given player";
        this.usageMessage = "/tell <player> <message>";
        this.setAliases(Arrays.asList(new String[] { "w", "msg"}));
        this.setPermission("bukkit.command.tell");
    }

    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        } else if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
            return false;
        } else {
            Player player = Bukkit.getPlayerExact(args[0]);

            if (player != null && (!(sender instanceof Player) || ((Player) sender).canSee(player))) {
                StringBuilder message = new StringBuilder();

                for (int result = 1; result < args.length; ++result) {
                    if (result > 1) {
                        message.append(" ");
                    }

                    message.append(args[result]);
                }

                String s = ChatColor.GRAY + sender.getName() + " whispers " + message;

                sender.sendMessage("[" + sender.getName() + "->" + player.getName() + "] " + message);
                player.sendMessage(s);
            } else {
                sender.sendMessage("There\'s no player by that name online.");
            }

            return true;
        }
    }

    public List tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return args.length == 0 ? super.tabComplete(sender, alias, args) : Collections.emptyList();
    }
}
